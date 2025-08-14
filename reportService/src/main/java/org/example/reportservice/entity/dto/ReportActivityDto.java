package org.example.reportservice.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.reportservice.entity.ReportActivity;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ReportActivityDto {
    String type;
    String description;
    Boolean coefficient;

    public static ReportActivityDto toDto(ReportActivity reportActivity) {
        if (reportActivity == null) {
            return null;
        }
        return ReportActivityDto.builder()
                .coefficient(reportActivity.getCoefficient())
                .description(reportActivity.getDescription())
                .type(reportActivity.getType()).build();
    }
    public static List<ReportActivityDto> toDtoList(List<ReportActivity> reportActivityList) {
        List<ReportActivityDto> reportActivities = new ArrayList<>();
        for (ReportActivity reportActivity : reportActivityList) {
            reportActivities.add(toDto(reportActivity));
        }
        return reportActivities;
    }
}
