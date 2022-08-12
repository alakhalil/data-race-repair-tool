package de.uni_passau.data_race_repair.access;

public class Trace {

	public final String methodName;
	public final int location;

	public Trace(
		final String methodName,
		final int location
	) {
		this.methodName = methodName;
		this.location = location;
	}

	public String toString() {
		return methodName;
	}

}
