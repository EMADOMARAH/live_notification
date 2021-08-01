package com.emad.live_notification.auth.Models.Request;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("id")
	private String id = "87";

	@SerializedName("type")
	private String type = "order";

	@SerializedName("click_action")
	private String clickAction = "ANDROID_NOTIFICATION_CLICKED";

	public String getId(){
		return id;
	}

	public String getType(){
		return type;
	}

	public String getClickAction(){
		return clickAction;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setClickAction(String clickAction) {
		this.clickAction = clickAction;
	}
}