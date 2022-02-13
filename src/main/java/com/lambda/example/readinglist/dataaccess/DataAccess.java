package com.lambda.example.readinglist.dataaccess;

public interface DataAccess<T> {
    void create(T item);

    T get(String id);

    void update(T item);

    void delete(String id);

    PaginatedList<T> list(String nextToken);
}
