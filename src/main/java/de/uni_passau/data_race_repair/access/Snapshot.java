package de.uni_passau.data_race_repair.access;

import java.util.Set;

public record Snapshot(
	String accessPath,
	AccessType accessType,
	Set<String> applicableLocks,
	String trace
) {}
