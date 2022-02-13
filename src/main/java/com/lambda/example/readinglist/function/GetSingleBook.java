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
/***
 * 
 * Handle a request to retrieve a single book by ID
 */
public class GetSingleBook extends BaseHTTPRequestHandler {
   
	protected DataAccess<Book> dataAccess;
    private static final Logger log = LogManager.getLogger(GetSingleBook.class);

	
	public GetSingleBook() {
		
		// TODO Auto-generated constructor stub
		dataAccess = new BookDynamoDataAccess();
	}
	


	@Override

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
		        context.getLogger().log("Rcvd event " + event.toString() + " request type " + event.getHttpMethod());
		        try {
		            String id = event.getPathParameters().get("id");
		            if (StringUtils.isEmpty(id)) {
		                return badRequest("id is missing");
		            }

		            Book book = dataAccess.get(id);
		            if (book == null) {
		                return notFound("item "+ id +" not found");
		            }

		            return ok(mapper.writeValueAsString(book));

		        }  catch (Exception e) {
		            log.error("Internal Error", e);
		            return error();
		        }//		        
	}
	 



}
