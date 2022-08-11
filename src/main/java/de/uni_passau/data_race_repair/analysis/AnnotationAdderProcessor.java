package de.uni_passau.data_race_repair.analysis;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtAnnotation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;

import java.lang.annotation.Annotation;

/**
 * Adds a given annotation to all class declarations where it was missing
 * @author Maximilian Seitz
 */
public class AnnotationAdderProcessor extends AbstractProcessor<CtClass<?>> {

	public final String annotationQualifiedName;

	/**
	 * @param annotationQualifiedName Qualified name of the annotation to add
	 */
	public AnnotationAdderProcessor(final String annotationQualifiedName) {
		this.annotationQualifiedName = annotationQualifiedName;
	}

	/**
     * {@inheritDoc}
     * @param element the candidate
     * @return <code>true</code> if the candidate is to be processed by the {@link #process(CtClass)}
     */
    @Override
    public boolean isToBeProcessed(final CtClass<?> element) {
    	return element.getAnnotations().stream().noneMatch(annotation -> {
    		final var annotationType = annotation.getAnnotationType();
    		return annotationType.getQualifiedName().equals(annotationQualifiedName);
	    });
    }

    /**
     * {@inheritDoc}
     * @param element the element that is currently being scanned
     */
    @Override
    public void process(final CtClass<?> element) {
	    final CtTypeReference<Annotation> threadSafeTypeReference = getFactory().Core().createTypeReference();
	    threadSafeTypeReference.setSimpleName(annotationQualifiedName);

	    final CtAnnotation<?> threadSafeAnnotation = getFactory().Core().createAnnotation();
	    threadSafeAnnotation.setAnnotationType(threadSafeTypeReference);

    	element.addAnnotation(threadSafeAnnotation);
    }

}
