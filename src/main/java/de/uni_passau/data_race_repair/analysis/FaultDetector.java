package de.uni_passau.data_race_repair.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

public class FaultDetector {

    private final File sourceFilesDir;

    public FaultDetector(String sourceFilesPath) {
        this.sourceFilesDir = new File(sourceFilesPath);
    }

    /**
     * via a shell command use infer for fault localization for programs located in @sourceFilesPath
     *
     * @throws IOException
     */
    public Map<File, BugDetail[]> detectFaults() throws IOException, URISyntaxException {
        final Map<File, BugDetail[]> faults = new HashMap<>();

        final var sources = getSourceFiles();

        if (sources.size() != 0) {
            System.out.println("############################# Start fault localization with infer #############################");

            for (File source : sources) {
                System.out.println("Currently processing program: " + source.getName());

                String libDir = "./resources/lib/infer-annotation-0.18.0.jar";
                String buildDir = "./temp/build/";
                String analysisDir = "./temp/analysis/";
                String target = source.getAbsolutePath();
                String srcPath = sourceFilesDir.getAbsolutePath();

                String cmd = "infer --racerd-only -o " +  analysisDir + " -- javac -d " + buildDir + " -cp " + libDir + " -sourcepath " + srcPath + " " + target;
                System.out.println(cmd);
                Process process = Runtime.getRuntime().exec(cmd, null, null);
                printResults(process);

                faults.put(source, readBugReport(analysisDir));
            }
        } else {
            throw new IllegalArgumentException("ERROR: Directory is empty");
        }

        return faults;
    }

    /**
     * read reported bugs in json files into list of list bugDetail
     * @return
     */
    private BugDetail[] readBugReport(final String analysisDirPath) {
        try {
            final var reportPath = analysisDirPath + "/report.json";

            if (Paths.get(reportPath).toFile().isFile()) {
                final var mapper = new ObjectMapper();
                return mapper.readValue(Paths.get(reportPath).toFile(), BugDetail[].class);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return new BugDetail[0];
    }

    /**
     * get paths to the programs located in the provided path @sourceFilesPath
     */
    private Set<File> getSourceFiles() {
        return findAllFilesWithin(sourceFilesDir);
    }
    
    private Set<File> findAllFilesWithin(File dir) {
        final var fileTree = new HashSet<File>();
        
        if (dir == null || dir.listFiles() == null) {
            return fileTree;
        }
        
        for (final File entry : dir.listFiles()) {
            if (entry.isFile()) {
                fileTree.add(entry);
            } else {
                fileTree.addAll(findAllFilesWithin(entry));
            }
        }
        
        return fileTree;
    }

    /**
     * read the entry points for each program
     *
     * @param filePath
     * @return map of package -> class entry point
     */
    public static Map<String, String> readEntryPoints(String filePath) {
        HashMap<String, String> entryPointsMap = new HashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            while ((line = reader.readLine()) != null) {
                String[] keyValuePair = line.split("\\.", 2);
                if (keyValuePair.length > 1) {
                    String key = keyValuePair[0];
                    String value = keyValuePair[1];
                    entryPointsMap.put(key, value);
                    entryPointsMap.putIfAbsent(key, value);

                } else {
                    System.out.println("ERROR: Fail reading the entry point in line " + line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return entryPointsMap;
    }

    /**
     * print the output of the shell commands
     *
     * @param process
     * @throws IOException
     */

    public static void printResults(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = "";
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }


}
