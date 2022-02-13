package com.lambda.example.readinglist.function;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lambda.example.readinglist.dataaccess.BookDynamoDataAccess;
import com.lambda.example.readinglist.dataaccess.DataAccess;
import com.lambda.example.readinglist.dataaccess.PaginatedList;
import com.lambda.example.readinglist.model.Book;

/***
 * 
 * Handle a request to retrieve all the books in the database
 */
public class GetBooks extends BaseHTTPRequestHandler {

	protected DataAccess<Book> dataAccess;
	private static final Logger log = LogManager.getLogger(GetBooks.class);


	public GetBooks() {

		dataAccess = new BookDynamoDataAccess();
	}



	@Override

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
		context.getLogger().log("Rcvd event " + event.toString() + " request type " + event.getHttpMethod());
		String nextToken = event.getHeaders().get("X-next-token");

		try {
			PaginatedList<Book> books = dataAccess.list(nextToken);

			Map<String, String> headers = new HashMap<>();
			headers.put("X-max-results", String.valueOf(books.getTotal()));
			if (books.getNextToken() != null){
				headers.put("X-next-token", books.getNextToken());
			}
			context.getLogger().log("Number of books received: " + books.getItems().size());
			context.getLogger().log(books.getItems().toString());
			return response(headers).withStatusCode(200).withBody(mapper.writeValueAsString(books.getItems()));
		} catch (Exception e) {
			log.error("Internal Error", e);
			return error();
		}
	}



}
