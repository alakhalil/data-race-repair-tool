package de.uni_passau.data_race_repair.fix;

import de.uni_passau.data_race_repair.access.Trace;

import java.util.HashSet;
import java.util.Set;

public class Fix {

	private final Set<Trace> methodsToLock = new HashSet<>();

	public void addTraceToLock(Trace methodTrace) {
		methodsToLock.add(methodTrace);
	}

	public Set<Trace> getTracesToLock() {
		return new HashSet<>(methodsToLock);
	}

}
