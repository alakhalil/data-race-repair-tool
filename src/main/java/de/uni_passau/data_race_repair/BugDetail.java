package de.uni_passau.data_race_repair;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)

public class BugDetail {

    private String bug_type;
    private String qualifier;
    private String severity;
    private String line;
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

    public String getLine() {
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

