package com.lambda.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.lambda.example.model.ReadingListDBManager;

public class ReadingListRequestHandler implements RequestHandler<Object, String> {

	private static ReadingListDBManager dbManager;
    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Rcvd event " + input.toString());
        return "Hello from Lambda!";
    }

}
