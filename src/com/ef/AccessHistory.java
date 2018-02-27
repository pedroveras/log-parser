package com.ef;

import java.time.LocalDateTime;

public class AccessHistory {
	private String ip;
	private LocalDateTime date;
	private LocalDateTime endDate;
	private Integer threshold;
	
	public AccessHistory(String ip, LocalDateTime date) {
		this.ip = ip;
		this.date = date;
	}
	
	public AccessHistory(LocalDateTime date, 
			LocalDateTime endDate, Integer threshold) {
		this.date = date;
		this.endDate = endDate;
		this.threshold = threshold;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}
}
