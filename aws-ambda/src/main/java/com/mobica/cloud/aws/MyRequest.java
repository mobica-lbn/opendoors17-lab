package com.mobica.cloud.aws;

/**
 * Custom request object
 */
public class MyRequest {

	private String key;

	private String val;

	public MyRequest() {
	}

	public MyRequest(String name, String desc) {
		this.key = name;
		this.val = desc;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
}
