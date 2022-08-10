package de.uni_passau.data_race_repair;

import spoon.Launcher;

public class Main {

	private static void printHelp() {
		System.out.println("Usage:" +
			"\n\tINPUT_DIR OUTPUT_DIR");
	}

	private static void addAnnotations(
		final String sourceDir,
		@SuppressWarnings("SameParameterValue") final String targetDir,
		final String... qualifiedAnnotationNames
	) {
		final var launcher = new Launcher();
		launcher.addInputResource(sourceDir);
		launcher.setSourceOutputDirectory(targetDir);

		for (final var qualifiedAnnotationName : qualifiedAnnotationNames) {
			launcher.addProcessor(new AnnotationAdderProcessor(qualifiedAnnotationName));
		}

		launcher.run();
	}

	public static void main(final String[] args) {
		if (args.length > 0) {
			final var sourceDir = args[0];
			final var outputDir = args[1];

			addAnnotations(
				sourceDir,
				"temp/annotated/",
				"com.facebook.infer.annotation.ThreadSafe"
			);

			// TODO: remaining processing + remove 'temp' dir
		} else {
			printHelp();
		}
	}

}
