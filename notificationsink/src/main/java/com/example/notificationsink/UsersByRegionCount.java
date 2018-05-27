package com.example.notificationsink;

public class UsersByRegionCount {
	String region;

	long count;

	long start;

	long end;

	public UsersByRegionCount(String region, long count, long start, long end) {
		this.region = region;
		this.count = count;
		this.start = start;
		this.end = end;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}
}
