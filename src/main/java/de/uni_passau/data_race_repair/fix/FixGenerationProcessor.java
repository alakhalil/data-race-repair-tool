package de.uni_passau.data_race_repair.fix;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtExpression;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.ModifierKind;

import java.io.File;
import java.util.*;

/**
 * Applies previously generated fixes to the source code
 * @author Maximilian Seitz
 */
public class FixGenerationProcessor extends AbstractProcessor<CtClass<?>> {

	private final Map<String, Set<Fix>> fixesByFile;

	/**
	 * @param fixesByFile Fixes to apply per each file
	 */
	public FixGenerationProcessor(
		final Map<String, Set<Fix>> fixesByFile
	) {
		this.fixesByFile = fixesByFile;
	}

	/**
	 * {@inheritDoc}
	 * @param clazz the class that is currently being scanned
	 */
	@Override
	public void process(final CtClass<?> clazz) {
		final var file = clazz.getPosition().getCompilationUnit().getFile().getAbsolutePath();
		final var fixes = fixesByFile.get(file);

		if (fixes != null) {
			final var className = clazz.getQualifiedName();
			System.out.println("Locking methods in " + className + ":");

			var lockId = 0;
			for (final var fix : fixes) {
				final var tracesToLock = fix.getTracesToLock();

				final Set<CtMethod<?>> methodsToLock = new HashSet<>();
				for (final var traceToLock : tracesToLock) {
					final var fullMethodSignature = traceToLock.methodName;
					final var traceLineNum = traceToLock.location;

					if (fullMethodSignature.startsWith(className + ".")) {
						final var methodSignature = fullMethodSignature.substring(className.length() + 1);

						final var firstPeriod = methodSignature.indexOf(".");
						final var argsBlockStart = methodSignature.indexOf("(");
						if (firstPeriod < 0 || (argsBlockStart < firstPeriod)) {
							final var methodName = methodSignature.substring(0, argsBlockStart);

							for (final var method : clazz.getMethodsByName(methodName)) {
								final var methodPosition = method.getPosition();
								
								if (methodPosition.getLine() <= traceLineNum && traceLineNum <= methodPosition.getEndLine()) {
									methodsToLock.add(method);
								}
							}
						}
					}
				}

				if (!methodsToLock.isEmpty()) {
					final var lockName = getLockName(lockId++);
					System.out.println("\tUsing: " + lockName + ":");

					final var lockField = getFactory().createCtField(
						lockName,
						getFactory().createCtTypeReference(Object.class),
						"new java.lang.Object()",
						ModifierKind.PRIVATE,
						ModifierKind.FINAL
					);

					final var lockRead = getFactory().createFieldRead();
					final var lockReference = getFactory().createFieldReference();
					lockReference.setSimpleName(lockName);
					lockRead.setVariable(lockReference);

					for (final var method : methodsToLock) {
						System.out.println("\t\t" + method.getSimpleName());
						final var synchronizedBody = makeSynchronized(lockRead, method.getBody());
						method.setBody(synchronizedBody);
					}

					clazz.addFieldAtTop(lockField);
				}
			}
		}
	}

	private String getLockName(final int lockId) {
		return "lock_" + lockId;
	}

	private CtStatement makeSynchronized(final CtExpression<?> lock, final CtBlock<?> block) {
		final var synchronizedStmt = getFactory().createSynchronized();
		synchronizedStmt.setExpression(lock);
		synchronizedStmt.setBlock(block);
		return synchronizedStmt;
	}

}
