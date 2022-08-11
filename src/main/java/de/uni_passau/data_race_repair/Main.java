package de.uni_passau.data_race_repair;

import spoon.Launcher;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Main {
	private static List<List<BugDetail>> bugDetails;

	private static void printHelp() {
		System.out.println("Usage:" +
			"\n\tINPUT_DIR OUTPUT_DIR");
	}

	private static void addAnnotations(
		final String sourceDir,
		@SuppressWarnings("SameParameterValue") final String targetDir,
		final String... qualifiedAnnotationNames
	)  {
		final var launcher = new Launcher();
		launcher.addInputResource(sourceDir);
		launcher.setSourceOutputDirectory(targetDir);

		for (final var qualifiedAnnotationName : qualifiedAnnotationNames) {
			launcher.addProcessor(new AnnotationAdderProcessor(qualifiedAnnotationName));
		}

		launcher.run();


	}

	public static void main(final String[] args) throws IOException, URISyntaxException {
		if (args.length > 0) {
			final var sourceDir = args[0];
			final var outputDir = args[1];

			addAnnotations(
				sourceDir,
				"temp/annotated/",
				"com.facebook.infer.annotation.ThreadSafe"
			);

			// TODO: remaining processing + remove 'temp' dir

			// concurrency bugs - fault localization
			FaultDetector faultDetector = new FaultDetector(sourceDir);
			faultDetector.detectFaults();
			bugDetails = faultDetector.readReports();
		} else {
			printHelp();
		}
	}

}
