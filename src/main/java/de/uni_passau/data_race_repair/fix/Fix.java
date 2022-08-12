package de.uni_passau.data_race_repair.fix;

import java.util.HashSet;
import java.util.Set;

public class Fix {

	private final Set<String> methodsToLock = new HashSet<>();

	public void addMethodToLock(String methodTrace) {
		methodsToLock.add(methodTrace);
	}

	public Set<String> getMethodsToLock() {
		return new HashSet<>(methodsToLock);
	}

}
