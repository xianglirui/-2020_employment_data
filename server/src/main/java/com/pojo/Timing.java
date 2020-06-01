package com.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "timing")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timing {
    @Id
    private ObjectId id;
    private String date;
    private Boolean am;
    private Boolean pm;
}
