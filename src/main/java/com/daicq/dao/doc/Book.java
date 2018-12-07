package com.daicq.dao.doc;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Document
public class Book implements Serializable {
    @Id
    private String key;
    @Field
    private String mappingId;
    @Field
    private Long id;
    @Field
    private String type;
    @Field
    private List<Map<String, String>> connection;
    @Field
    private String mappingName;
    @Field
    private List<List<String>> sourceId;
    @Field
    private String description;
    @Field
    private String mappingType;
}
