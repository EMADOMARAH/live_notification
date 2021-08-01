package com.emad.live_notification.auth.Models.Request;

import com.google.gson.annotations.SerializedName;

public class Notification{


	public Notification(String title, String body) {
		this.title = title;
		this.body = body;
	}

	@SerializedName("sound")
	private String sound = "default";

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	public String getSound(){
		return sound;
	}

	public String getTitle(){
		return title;
	}

	public String getBody(){
		return body;
	}

	public void setSound(String sound) {
		this.sound = sound;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setBody(String body) {
		this.body = body;
	}
}