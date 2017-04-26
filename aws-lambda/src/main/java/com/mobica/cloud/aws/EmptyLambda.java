package com.mobica.cloud.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import static java.lang.String.format;

/**
 * Empty lambda class
 */
public class EmptyLambda implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object in, Context context) {
        context.getLogger().log(format("Execute emptyLambda: %s", System.currentTimeMillis()));
        return null;
    }

}
