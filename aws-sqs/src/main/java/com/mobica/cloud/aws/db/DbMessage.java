package com.mobica.cloud.aws.db;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import static com.mobica.cloud.aws.db.DbMessage.TABLE_NAME;

@DynamoDBTable(tableName = TABLE_NAME)
public class DbMessage {
    public static final String TABLE_NAME = "opendoors17-dev-test";
    public static final String ID_NAME = "id";

    @DynamoDBHashKey(attributeName = ID_NAME)
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String kind1;
    private String kind2;
    private String kind3;
    private String keyword1;
    private String keyword2;

    public DbMessage() {
    }

    public DbMessage(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getKind1() {
        return kind1;
    }

    public void setKind1(String kind1) {
        this.kind1 = kind1;
    }

    public String getKind2() {
        return kind2;
    }

    public void setKind2(String kind2) {
        this.kind2 = kind2;
    }

    public String getKind3() {
        return kind3;
    }

    public void setKind3(String kind3) {
        this.kind3 = kind3;
    }

    public String getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }

    public String getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }

    @Override
    public String toString() {
        return "DbMessage{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", kind1='" + kind1 + '\'' +
                ", kind2='" + kind2 + '\'' +
                ", kind3='" + kind3 + '\'' +
                ", keyword1='" + keyword1 + '\'' +
                ", keyword2='" + keyword2 + '\'' +
                '}';
    }
}
