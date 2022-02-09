package com.lambda.example.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Book")
public class Book {

	private Integer id;
	private String title;
	private String review;
	private boolean completed;

	@DynamoDBHashKey(attributeName = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@DynamoDBAttribute(attributeName = "Title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@DynamoDBAttribute(attributeName = "review")
	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	@DynamoDBAttribute(attributeName = "completed")
	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}