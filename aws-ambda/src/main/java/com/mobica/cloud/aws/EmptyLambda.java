package com.mobica.cloud.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * Empty lambda class
 */
public class EmptyLambda implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String in, Context context) {
        context.getLogger().log("Execute emptyLambda with parameter: " + in);
        return in;
    }

}
