package com.lambda.example.readinglist.dataaccess;

import java.util.List;

public class PaginatedList<T> {

    private final List<T> items;
    private final int total;
    private final String nextToken;

    public PaginatedList(List<T> items, int total) {
        this(items, total, null);
    }

    public PaginatedList(List<T> items, int total, String nextToken) {
        this.items = items;
        this.total = total;
        this.nextToken = nextToken;
    }

    public List<T> getItems() {
        return items;
    }

    public int getTotal() {
        return total;
    }

    public String getNextToken() {
        return nextToken;
    }
}
