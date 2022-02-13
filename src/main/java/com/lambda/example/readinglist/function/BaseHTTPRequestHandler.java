package com.lambda.example.readinglist.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambda.example.readinglist.dataaccess.BookDynamoDataAccess;
import com.lambda.example.readinglist.dataaccess.DataAccess;
import com.lambda.example.readinglist.model.Book;

public abstract class BaseHTTPRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

   
	protected DataAccess<Book> dataAccess;
	protected final ObjectMapper mapper = new ObjectMapper();



    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    
    
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String PUT = "PUT";
	public static final String DELETE = "DELETE";
	
	
	public BaseHTTPRequestHandler() {
		
		dataAccess = new BookDynamoDataAccess();
	}
    private APIGatewayProxyResponseEvent response() {
        return response(null);
    }

    
    protected APIGatewayProxyResponseEvent response(Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put("Content-Type", "application/json");

        return new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
    }

    protected APIGatewayProxyResponseEvent ok(String body) {
        return response().withStatusCode(200).withBody(body);
    }

    protected APIGatewayProxyResponseEvent badRequest(String message) {
        return response().withStatusCode(400).withBody("{\"error\":\"Bad request\", \"message\":\"" + message + "\"}");
    }

    protected APIGatewayProxyResponseEvent notFound(String message) {
        return response().withStatusCode(404).withBody("{\"error\":\"Not found\", \"message\":\"" + message + "\"}");
    }

    protected APIGatewayProxyResponseEvent created(String body) {
        return response().withStatusCode(201).withBody(body);
    }

    protected APIGatewayProxyResponseEvent error() {
        return response().withStatusCode(500).withBody("{\"error\":\"Internal Server Error\", \"message\":\"Unexpected error\"}");
    }


    protected void validateBook(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        if (!violations.isEmpty()) {
            throw new ValidationException("Invalid Book: " + violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", ")));
        }
    }
   
}
