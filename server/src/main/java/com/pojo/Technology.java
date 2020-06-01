package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "technology")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Technology {
    @Id
    private ObjectId id;
    private String key;
    private String city;
    private List<String> hot_name;
    private List<Integer> hot_num;
    private List<String> stop_list;
}
