package org.example.bff.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportBigDto implements HasUser {

    Long id;
    String type;
    String desc;
    String imageURL;
    LocalDate dateOfCreation;
    String userName;
    Long userId;
    String status;
    Integer cost;
}
