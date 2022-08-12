package de.uni_passau.data_race_repair;

import de.uni_passau.data_race_repair.access.Snapshot;
import de.uni_passau.data_race_repair.analysis.AnnotationAdderProcessor;
import de.uni_passau.data_race_repair.analysis.BugDetail;
import de.uni_passau.data_race_repair.analysis.FaultDetector;
import de.uni_passau.data_race_repair.fix.Fix;
import de.uni_passau.data_race_repair.fix.FixGenerationProcessor;
import spoon.Launcher;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("SameParameterValue")
public class Main {

	private static final String ANALYSIS_ANNOTATION_QUALIFIED_NAME = "com.facebook.infer.annotation.ThreadSafe";

	private static final String TEMP_DIR = "temp/";
	private static final String ANNOTATED_SOURCES_DIR = TEMP_DIR + "annotated/";


	private static void printHelp() {
		System.out.println("Usage:" +
			"\n\tINPUT_DIR OUTPUT_DIR");
	}


	private static void addAnnotations(
		final String sourceDir,
		final String targetDir,
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


	private static Map<File, Set<Snapshot>> findBugsByFile(final String sourceDir) throws IOException, URISyntaxException {
		final var bugsByFile = new HashMap<File, Set<Snapshot>>();

		final var faultDetector = new FaultDetector(sourceDir);
		faultDetector.detectFaults().forEach((file, bugs) ->
			bugsByFile.put(
				file,
				Arrays.stream(bugs)
					.map(BugDetail::toSnapshot)
					.collect(Collectors.toSet())
			)
		);

		return bugsByFile;
	}


	private static void repairClassFile(
		final Map<String, Set<Fix>> fixesByFile,
		final String analyzedSourcesDir,
		final String outputDir
	) {
		final var launcher = new Launcher();
		launcher.addInputResource(analyzedSourcesDir);
		launcher.setSourceOutputDirectory(outputDir);
		launcher.addProcessor(new FixGenerationProcessor(fixesByFile));

		launcher.run();
	}


	private static void fixBugs(
		final Map<File, Set<Snapshot>> bugsByFile,
		final String analyzedSourcesDir,
		final String outputDir
	) {
		final var fixesByFile = new HashMap<String, Set<Fix>>();

		for (final var file : bugsByFile.keySet()) {
			final var bugs = bugsByFile.get(file);
			
			final Map<String, List<Snapshot>> bugsByAccessPath = bugs
				.stream()
				.collect(Collectors.groupingBy(bug -> bug.accessPath));

			final Set<Fix> fixes = new HashSet<>(); // TODO: implement the algorithm

			fixesByFile.put(file.getAbsolutePath(), fixes);
		}

		repairClassFile(fixesByFile, analyzedSourcesDir, outputDir);
	}


	private static void deleteRecursively(final File file) {
		if (file.exists()) {
			final var children = file.listFiles();
			if (children != null) {
				for (final var child : children) {
					deleteRecursively(child);
				}
			}

			//noinspection ResultOfMethodCallIgnored
			file.delete();
		}
	}


	private static void deleteRecursively(final String path) {
		deleteRecursively(new File(path));
	}


	public static void main(final String[] args) throws IOException, URISyntaxException {
		if (args.length > 0) {
			final var sourceDir = args[0];
			final var outputDir = args[1];

			addAnnotations(sourceDir, ANNOTATED_SOURCES_DIR, ANALYSIS_ANNOTATION_QUALIFIED_NAME);

			final var bugsByFile = findBugsByFile(ANNOTATED_SOURCES_DIR);

			fixBugs(bugsByFile, ANNOTATED_SOURCES_DIR, outputDir);

			deleteRecursively(TEMP_DIR);
		} else {
			printHelp();
		}
	}

}
