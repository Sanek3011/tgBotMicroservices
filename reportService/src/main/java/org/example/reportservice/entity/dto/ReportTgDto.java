package org.example.reportservice.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.reportservice.entity.Report;
import org.example.reportservice.entity.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportTgDto {
    String type;
    String desc;
    LocalDate dateOfCreation;

    public static List<ReportTgDto> toDtoList(List<Report> list) {
        List<ReportTgDto> result = new ArrayList<>();
        for (Report report : list) {
            ReportTgDto dto = toDto(report);
            result.add(dto);
        }
        return result;
    }
    public static ReportTgDto toDto(Report report) {
        ReportTgDto build = ReportTgDto.builder()
                .desc(report.getDescription())
                .type(report.getReportActivity().getDescription())
                .dateOfCreation(report.getDateOfCreation())
                .build();
        return build;
    }
}
