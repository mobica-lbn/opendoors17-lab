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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import static java.lang.String.format;

public class RequestResponseLambda implements RequestHandler<MyRequest, MyResponse> {

	@Override
	public MyResponse handleRequest(MyRequest request, Context context) {
		final String key = request != null ? request.getKey() : "null";
		final String val = request != null ? request.getVal() : "null";
		return new MyResponse(format("[%s]->[%s]", key, val));
	}
}
