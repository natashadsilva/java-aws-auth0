package com.lambda.example.readinglist.function;

import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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

import software.amazon.awssdk.utils.StringUtils;

public class UpdateBook extends BaseHTTPRequestHandler {

	protected DataAccess<Book> dataAccess;
	private static final Logger log = LogManager.getLogger(UpdateBook.class);



	public UpdateBook() {

		dataAccess = new BookDynamoDataAccess();
	}



	@Override

	public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent event, final Context context) {
		context.getLogger().log("Rcvd event " + event.toString() + " request type " + event.getHttpMethod());
		return updateBook(event, context);

	}
	

	public APIGatewayProxyResponseEvent updateBook(final APIGatewayProxyRequestEvent event, final Context context) {
		try {
			//get the id of the book we want to update
			String id = event.getPathParameters().get("id");
			if (StringUtils.isEmpty(id)) {
				return badRequest("id is missing");
			}

			Book book = mapper.readValue(event.getBody(), Book.class);
			validateBook(book);

			Book oldBook = dataAccess.get(id);
			if (oldBook == null) {
				return notFound("item "+ id +" not found");
			}

			book.setId(oldBook.getId());
			dataAccess.update(book);


			return ok("{\"message\":\"item " + book.getId() + " updated\"}");

		} catch (JsonParseException | JsonMappingException e) {
			return badRequest("Book is malformed");
		} catch (ValidationException e) {
			return badRequest(e.getMessage());
		} catch (Exception e) {
			log.error("Internal Error", e);
			return error();
		}
	}

}
