/******************************************************************************
 * Copyright (c) 2017, T-Mobile US.
 * <p>
 * All Rights Reserved
 * <p>
 * This is unpublished proprietary source code of T-Mobile US.
 * <p>
 * The copyright notice above does not evidence any actual or intended publication of such source code.
 *******************************************************************************/
package com.mobica.cloud.aws;

import java.util.Objects;

/**
 * Custom response object
 */
public class MyResponse {

	private String message;

	public MyResponse() {
	}

	public MyResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof MyResponse)) {
			return false;
		}
		final MyResponse that = (MyResponse) o;
		return Objects.equals(message, that.message);
	}

	@Override
	public int hashCode() {
		return Objects.hash(message);
	}
}
