package com.lambda.example.readinglist.model;

import javax.validation.constraints.NotNull;

import com.lambda.example.readinglist.dataaccess.BookDynamoDataAccess;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
/****
 * 
 * Class representing a Book in the reading list
 * used by {@link BookDynamoDataAccess} to populate and make requests to the DB
 * 
 */
public class Book {

	@NotNull(message = "title must not be null")
	private String title;
	@NotNull(message = "user must not be null")
	private String user;
	private Integer id;	
	private String review;
	private boolean completed;


	public Book() {
		
	}
    public Book(int id,  String username, String title, String review, boolean completed) {
        this.id = id;
        this.user = username;
        this.title = title;
        this.review = review;
        this.completed = completed;
    }
    
    @DynamoDbPartitionKey
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}