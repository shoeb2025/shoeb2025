package com.alamaanah.nisaarnadiadwala;

import java.sql.Timestamp;


public class SingleEvent {

	public String title;
    public Timestamp time;
	public String venue;
	public String description;

	public SingleEvent() {
		super();
	}

	public SingleEvent(String title, Timestamp time, String venue,String d) {
		this.title = title;
		this.time = time;
		this.venue=venue;
		description=d;
	}
}
