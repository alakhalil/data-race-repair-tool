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

	public final Map<File, Set<Fix>> fixesByFile;

	/**
	 * @param fixesByFile Fixes to apply per each file
	 */
	public FixGenerationProcessor(final Map<File, Set<Fix>> fixesByFile) {
		this.fixesByFile = fixesByFile;
	}

	/**
     * {@inheritDoc}
     * @param element the candidate
     * @return <code>true</code> if the candidate is to be processed by the {@link #process(CtClass)}
     */
    @Override
    public boolean isToBeProcessed(final CtClass<?> element) {
    	return true;
    }

    /**
     * {@inheritDoc}
     * @param element the element that is currently being scanned
     */
    @Override
    public void process(final CtClass<?> element) {
	    
    }

}
