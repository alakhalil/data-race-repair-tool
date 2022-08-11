package de.uni_passau.data_race_repair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class CommandLine {


    private static List<List<BugDetail>> bugDetails;

    public static void main(String[] args) throws IOException, URISyntaxException {
        if (args.length != 0) {
            String SourceFilesPath = args[0];

            // Annotate the classes

            // concurrency bugs - fault localization
            FaultDetector faultDetector = new FaultDetector(SourceFilesPath);
            faultDetector.detectFaults();
            bugDetails = faultDetector.readReports();

            // generate repair
        } else
            throw new IllegalArgumentException("ERROR: The absolute path to the directory of the test files is needed");
    }


}
