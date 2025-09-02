package org.dis.gamedata.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "jobs")
@Data
public class Job {
   @Id
   private String id;
   private LocalDate date;
}
