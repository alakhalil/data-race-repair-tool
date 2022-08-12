package de.uni_passau.data_race_repair;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

public class FaultDetector {

    private final String sourceFilesPath;
    private File[] directories;
    private final List<List<BugDetail>> bugDetails = new ArrayList<>();

    public FaultDetector(String sourceFilesPath) {
        this.sourceFilesPath = sourceFilesPath;
    }

    /**
     * via a shell command use infer for fault localization for programs located in @sourceFilesPath
     *
     * @throws IOException
     */
    public void detectFaultsUsingEntryPoints() throws IOException, URISyntaxException {

        this.getProgramsPaths();
        assert directories != null;
        if (directories.length != 0) {
            System.out.println("############################# start fault localization with infer #############################");
            String entriesFilePath = sourceFilesPath + "/entries.txt";
            Map<String, String> entryPoints = readEntryPoints(entriesFilePath);

            for (File program : directories) {
                System.out.println("Currently processing program:" + program.getName());
                String libDir = "./resources/lib/infer-annotation-0.18.0.jar";
                String outputDir = "./out";
                String classpath = "./" + entryPoints.get(program.getName()) + ".java";
                String srcPath = "./";

                String cmd = "infer --racerd-only -o " + outputDir + " -- javac -d " + outputDir + " -cp " + libDir + " -sourcepath " + srcPath + " " + classpath;
                Process process = Runtime.getRuntime().exec(cmd, null, new File(sourceFilesPath + "/" + program.getName()));
                System.out.println(cmd);
                printResults(process);
            }
        } else throw new IllegalArgumentException("ERROR: Directory is empty");
    }


    /**
     * via a shell command use infer for fault localization for programs located in @sourceFilesPath
     *
     * @throws IOException
     */
    public void detectFaults() throws IOException, URISyntaxException {

        this.getProgramsPaths();
        assert directories != null;
        if (directories.length != 0) {
            System.out.println("############################# start fault localization with infer #############################");

            // relative paths
            String libDir = "/home/alaa/Desktop/lib/infer-annotation-0.18.0.jar";
            String outputDir = "./out";
            String srcPath = "./";

            for (File program : directories) {
                System.out.println("Currently processing program:" + program.getName());

                File[] classes = program.listFiles();
                if (classes != null) {
                    for (File javaclass : classes) {
                        String classpath = "./" + javaclass.getName();
                        String cmd = "infer --racerd-only -o " + outputDir + " -- javac -d " + outputDir + " -cp " + libDir + " -sourcepath " + srcPath + " " + classpath;
                        Process process = Runtime.getRuntime().exec(cmd, null, new File(sourceFilesPath + "/" + program.getName()));
                        System.out.println(cmd);
                        printResults(process);
                    }
                } else {
                    System.out.println("Warning: current processing program has no classes");
                }
            }
        } else throw new IllegalArgumentException("ERROR: The provided path is empty");
    }

    /**
     * read reported bugs in json files into list of list bugDetail
     *
     * @return
     */
    public List<List<BugDetail>> readReports() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            for (File program : this.directories) {
                String reportPath = sourceFilesPath + "/" + program.getName() + "/infer-out/report.json";
                if (Paths.get(reportPath).toFile().isFile())
                    bugDetails.add(Arrays.asList(mapper.readValue(Paths.get(reportPath).toFile(), BugDetail[].class)));
            }
            return bugDetails;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * get paths to the programs located in the provided path @sourceFilesPath
     */
    private void getProgramsPaths() {
        this.directories = new File(sourceFilesPath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
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
