package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Document(collection = "trend")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trend {
    @Id
    private ObjectId id;
    private String key;
    private ArrayList<Integer> baidu;
    private ArrayList<Integer> sougou;
    private ArrayList<Integer> _360;
}
