package org.example.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportAdminDto {
    Long id;
    String type;
    String desc;
    String imageURL;
    LocalDate dateOfCreation;
    String userName;
    Long userId;


}
