package com.lambda.example.readinglist.function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lambda.example.readinglist.dataaccess.BookDynamoDataAccess;
import com.lambda.example.readinglist.dataaccess.DataAccess;
import com.lambda.example.readinglist.model.Book;

import software.amazon.awssdk.utils.StringUtils;

public class DeleteBook extends BaseHTTPRequestHandler {
   
	protected DataAccess<Book> dataAccess;
    private static final Logger log = LogManager.getLogger(DeleteBook.class);

    
	public DeleteBook() {
		
		dataAccess = new BookDynamoDataAccess();
	}
	


	@Override

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
		        context.getLogger().log("Rcvd event " + event.toString() + " request type " + event.getHttpMethod());
		        return deleteBook(event, context);
		        
	}
	 

    public APIGatewayProxyResponseEvent deleteBook(final APIGatewayProxyRequestEvent event, final Context context) {
        try {
            String id = event.getPathParameters().get("id");
            log.info("Attempting to DELETE "+ id);
            if (StringUtils.isEmpty(id)) {
                return badRequest("id is missing");
            }
           
            dataAccess.delete(id);


            return ok("");

        }  catch (Exception e) {
            log.error("Internal Error", e);
            return error();
        }
    }

}
