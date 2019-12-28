package com.aws.tutorials.gateway;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GatewayDemoHandler implements RequestHandler<GatewayPayload, String> {

    public String handleRequest(GatewayPayload input, Context context) {
        context.getLogger().log("Name: " + input.getName());
        return "Received name - " + input.getName();
    }
}