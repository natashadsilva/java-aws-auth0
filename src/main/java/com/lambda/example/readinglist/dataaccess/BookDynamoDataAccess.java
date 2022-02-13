package com.lambda.example.readinglist.dataaccess;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.xray.interceptors.TracingInterceptor;
import com.lambda.example.readinglist.model.Book;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.Select;

public class BookDynamoDataAccess implements DataAccess<Book> {

    private static final String DDB_TABLE = "Book";
    private static final String PAGE_SIZE_STR = System.getenv("PAGE_SIZE");
    private static final Integer PAGE_SIZE = PAGE_SIZE_STR != null ? Integer.parseInt(PAGE_SIZE_STR) : 10;

    private static final DynamoDbClient ddb;

    static {
        DynamoDbClientBuilder ddbBuilder = DynamoDbClient.builder()
                .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
                .httpClient(UrlConnectionHttpClient.builder().build());

            ddbBuilder.region(Region.of(System.getenv("AWS_REGION")))
                    .overrideConfiguration(ClientOverrideConfiguration.builder()
                            .addExecutionInterceptor(new TracingInterceptor()).build());
        ddb = ddbBuilder.build();
    }

    private static final DynamoDbEnhancedClient client = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(ddb)
            .build();

    private static final DynamoDbTable<Book> bookTable = client.table(DDB_TABLE, TableSchema.fromBean(Book.class));

    @Override
    public void create(Book book) {
        bookTable.putItem(book);
    }

    @Override
    public Book get(String id) {
        return bookTable.getItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public void update(Book book) {
        bookTable.updateItem(book);
    }

    @Override
    public void delete(String id) {
        bookTable.deleteItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public PaginatedList<Book> list(String nextToken) {
        ScanResponse total = ddb.scan(builder -> builder.tableName(DDB_TABLE).select(Select.COUNT));

        ScanRequest.Builder builder = ScanRequest.builder().tableName(DDB_TABLE).limit(PAGE_SIZE);
        if (nextToken != null) {
            Map<String, AttributeValue> start = new HashMap<>();
            start.put("id", AttributeValue.builder().s(nextToken).build());
            builder.exclusiveStartKey(start);
        }
        ScanResponse response = ddb.scan(builder.build());

        return new PaginatedList<Book>(
                response.items().stream().map(this::mapBook).collect(Collectors.toList()),
                total.count(),
                response.hasLastEvaluatedKey() ? response.lastEvaluatedKey().get("id").s() : null
        );
    }

    private Book mapBook(Map<String, AttributeValue> item) {
        return new Book(
                Integer.parseUnsignedInt(item.get("id").n()),
                item.get("user").s(),
                item.get("title").s(),
                item.get("review").s(),
                item.get("completed").bool());
    }
}
