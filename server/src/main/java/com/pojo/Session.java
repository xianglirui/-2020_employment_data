package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "session")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    @Id
    private ObjectId id;
    private String city;
    private String key;
}
