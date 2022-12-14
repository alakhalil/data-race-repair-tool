package de.uni_passau.data_race_repair.analysis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.uni_passau.data_race_repair.access.AccessType;
import de.uni_passau.data_race_repair.access.Snapshot;
import de.uni_passau.data_race_repair.access.Trace;

import java.util.Set;


@JsonIgnoreProperties(ignoreUnknown = true)
public class BugDetail {

    private String bug_type;
    private String qualifier;
    private String severity;
    private int line;
    private String column;
    private String procedure;
    private String procedure_start_line;
    private String file;
    @JsonProperty("bug_trace")
    private BugTrace [] bug_trace;
    private String key;
    private String hash;
    private String bug_type_hum;
    private String access;


    public BugDetail() {

    }

	public Snapshot toSnapshot() {
    	final var accessType = qualifier.startsWith("Read/Write race.") ? AccessType.RD : AccessType.WR;

        final String accessPathPreText;
        if (accessType == AccessType.RD) {
            accessPathPreText = "reads without synchronization from `";
	    } else {
            accessPathPreText = "writes to field `";
	    }

        final int accessPathStartLocation = qualifier.indexOf(accessPathPreText) + accessPathPreText.length();
        final int accessPathLength = qualifier.substring(accessPathStartLocation).indexOf("`");

        final String accessPath = qualifier.substring(accessPathStartLocation, accessPathStartLocation + accessPathLength);

		return new Snapshot(accessPath, accessType, Set.of(), new Trace(procedure, line));
	}

    static class BugTrace {

        @JsonProperty("level")
        private String level;

        @JsonProperty("filename")
        private String filename;

        @JsonProperty("line_number")
        private String line_number;

        @JsonProperty("column_number")
        private String column_number;

        @JsonProperty("description")
        private String description;

        public String getLevel() {
            return level;
        }

        public String getFilename() {
            return filename;
        }

        public String getLine_number() {
            return line_number;
        }

        public String getColumn_number() {
            return column_number;
        }

        public String getDescription() {
            return description;
        }
    }

    public String getBug_type() {
        return bug_type;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getSeverity() {
        return severity;
    }

    public int getLine() {
        return line;
    }

    public String getColumn() {
        return column;
    }

    public String getProcedure() {
        return procedure;
    }

    public String getProcedure_start_line() {
        return procedure_start_line;
    }

    public String getFile() {
        return file;
    }

    public String getKey() {
        return key;
    }

    public String getHash() {
        return hash;
    }

    public String getBug_type_hum() {
        return bug_type_hum;
    }

    public String getAccess() {
        return access;
    }

    public BugTrace[] getBug_trace() {
        return bug_trace;
    }
}

