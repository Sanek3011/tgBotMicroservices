package org.example.bff.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReportActivityDto {
    Integer id;
    String type;
    String description;
    Integer basePrice;
    Boolean coefficient;

}
