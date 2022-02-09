package com.lambda.example.model;

import java.io.IOException;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class ReadingListDBManager {
	
	private static final DynamoDBMapper _mapper;
    static {
    	AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();    
    	
    	_mapper = new DynamoDBMapper(client);
    }
    
    public static void main(String[] args) throws IOException {
        testCRUDOperations();
        System.out.println("Example complete!");
    }
    
    public Book createItem (int id, String title, String review,  boolean completed) {
    	Book item = new Book();

        item.setId(id);
        item.setTitle(title);
        item.setReview(review);
        item.setCompleted(completed);
        return item;
    }
    
    public void insertBook(int id, String title, String review, boolean completed) {
    	_mapper.save(createItem(id, title, review, completed));
    }
    
    public Book getBook (int id) {
    	return getBook(id, false);
    }
    public Book getBook(int id, boolean consistent) {
    	Book book; 
    	if (consistent) {
    		DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
    	            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
    	        .build();
    		book = _mapper.load(Book.class, id, config);
    	} else {
    		book = _mapper.load(Book.class, id);
    	}
    	
    	
    	if (book == null) {
        	System.out.println("Could not find book");
        }
    	return book;
    }
    
    public void updateBook(int id, String title, String review, boolean completed) {
    	Book book = getBook(id, true);
    	if (book != null) {
    		updateBook(book, title, review, completed);
    	}
    }
    
    public void updateBook(Book book, String title, String review, boolean completed) {
    	if (title != null) {
    		book.setTitle(title);
    	}
    	if (review != null) {
    		book.setReview(review);
    	}
  		book.setCompleted(completed);
    	_mapper.save(book);
    }
    
    public void deleteBook(int id) {
    	Book book = getBook(id);
    	if (book != null) {
    		_mapper.delete(book);
    	}
    }
    
    
    private static void testCRUDOperations() {

    	ReadingListDBManager tester = new ReadingListDBManager();
    	tester.insertBook(6551, "All the Light We Cannot See", null, false);


        // Retrieve the item.
        Book book = tester.getBook(601);
        
        

        // Update the item.
        tester.updateBook(601, null, "Great sequel", true);
        System.out.println("Item updated:");
        System.out.println(book);

        // Retrieve the updated item.
        
        Book updatedItem = tester.getBook(601, true);
        System.out.println("Retrieved the previously updated item:");
        System.out.println(updatedItem);

        // Delete the item.
//       tester.deleteBook(601);

        // Try to retrieve deleted item.
//        Book deletedItem = tester.getBook(601, true);
//        if (deletedItem == null) {
//            System.out.println("Done - Sample item is deleted.");
//        }
    }
}
