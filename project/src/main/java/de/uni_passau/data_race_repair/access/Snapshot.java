package de.uni_passau.data_race_repair.access;

import java.util.Set;

public class Snapshot {

	public final String accessPath;
	public final AccessType accessType;
	public final Set<String> applicableLocks;
	public final Trace trace;

	public Snapshot(
		final String accessPath,
		final AccessType accessType,
		final Set<String> applicableLocks,
		final Trace trace
	) {
		this.accessPath = accessPath;
		this.accessType = accessType;
		this.applicableLocks = applicableLocks;
		this.trace = trace;
	}

	@Override
	public String toString() {
		return "<" + accessPath + ", " + accessType + ", " + trace + ">";
	}
}
