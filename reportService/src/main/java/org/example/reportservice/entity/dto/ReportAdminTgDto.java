package org.example.reportservice.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.reportservice.entity.Report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportAdminTgDto {
    Long id;
    String type;
    String desc;
    String imageURL;
    LocalDate dateOfCreation;
    String userName;
    Long userId;
    String status;
    Integer cost;

    public static List<ReportAdminTgDto> toDtoList(List<Report> list) {
        List<ReportAdminTgDto> result = new ArrayList<>();
        for (Report report : list) {
            ReportAdminTgDto dto = toDto(report);
            result.add(dto);
        }
        return result;
    }
    public static ReportAdminTgDto toDto(Report report) {
        ReportAdminTgDto build = ReportAdminTgDto.builder()
                .id(report.getId())
                .desc(report.getDescription())
                .type(report.getReportActivity().getDescription())
                .dateOfCreation(report.getDateOfCreation())
                .imageURL(report.getUrl())
                .dateOfCreation(report.getDateOfCreation())
                .userName("Юзер не подгрузился")
                .userId(report.getUserId())
                .status(report.getStatus().toString())
                .cost(report.getCost())
                .build();
        return build;
    }


}
