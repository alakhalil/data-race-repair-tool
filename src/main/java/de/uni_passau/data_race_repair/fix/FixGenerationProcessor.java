package de.uni_passau.data_race_repair.fix;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

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
	 * @param element the element that is currently being scanned
	 */
	@Override
	public void process(final CtClass<?> element) {
		final var file = element.getPosition().getCompilationUnit().getFile().getAbsolutePath();
		final var fixes = fixesByFile.get(file);

		if (fixes != null) {
			var nextLockId = 0;
			for (final var fix : fixes) {
				final var lockId = nextLockId++;
				final var methodsToLock = fix.getMethodsToLock();

				System.out.println("Using lock" + lockId + " to lock methods:");
				methodsToLock.forEach(method -> {
					System.out.println("\t" + method);
				});
			}
		}

		element.getMethods();
	}

}
