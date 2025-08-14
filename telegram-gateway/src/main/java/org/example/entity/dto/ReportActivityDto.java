package org.example.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReportActivityDto {
    String type;
    String description;
    Boolean coefficient;

}
