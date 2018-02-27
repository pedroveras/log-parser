package com.ef;

public enum Duration {
	HOURLY(1), DAILY(24);
	
	private int d;
	
	private Duration(int d) {
		this.d = d;
	}
	
	public static int getDuration(String duration) {
		return Duration.valueOf(duration.toUpperCase()).d;
	}
	
}
