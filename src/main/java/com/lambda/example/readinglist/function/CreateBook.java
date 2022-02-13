package com.lambda.example.readinglist.function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lambda.example.readinglist.dataaccess.BookDynamoDataAccess;
import com.lambda.example.readinglist.dataaccess.DataAccess;
import com.lambda.example.readinglist.model.Book;

public class CreateBook extends BaseHTTPRequestHandler {

	protected DataAccess<Book> dataAccess;
	private static final Logger log = LogManager.getLogger(CreateBook.class);

	public CreateBook() {

		dataAccess = new BookDynamoDataAccess();
	}



	@Override

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
		context.getLogger().log("Rcvd event " + event.toString() + " request type " + event.getHttpMethod());
		return createBook( event, context) ;

	}


	public APIGatewayProxyResponseEvent createBook (final APIGatewayProxyRequestEvent event, final Context context) {
		try {

			log.info("Attempting to create new book ");
			Book book = mapper.readValue(event.getBody(), Book.class);

			dataAccess.create(book);

			return created("{\"message\":\"item " + book.getId() + " created\"}");

		} catch (JsonParseException |
				JsonMappingException e) {
			log.error(e.getMessage());
			return badRequest("Malformed input");
		} catch (
				Exception e) {
			log.error("Internal Error", e);
			return error();
		}

	}

}
