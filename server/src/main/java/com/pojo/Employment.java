package com.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Document(collection = "lagouInfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employment implements Serializable {
    @Id
    private ObjectId id;
    private String city;
    private String key;
    private String money;
    private String grade;
    private String exper;
    private LinkedHashMap<String,String> place;
    private String rongzi;
    private String guimo;
    private String require;

}
