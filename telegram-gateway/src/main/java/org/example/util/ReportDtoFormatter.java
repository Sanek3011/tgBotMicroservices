package org.example.util;

import lombok.experimental.UtilityClass;
import org.example.entity.dto.ReportAdminDto;
import org.example.entity.dto.ReportDto;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class ReportDtoFormatter {

    public static String format(ReportAdminDto reportAdminDto) {
        String dateStr = reportAdminDto.getDateOfCreation().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return String.format("ID отчета:%d \n Автор отчета: %s\nТип отчета: %s\nОписание: %s\nСкрин: %s\nДата загрузки: %s\n-------------------\n",
                reportAdminDto.getId(), reportAdminDto.getUserName(), reportAdminDto.getType(),
                reportAdminDto.getDesc(), reportAdminDto.getImageURL(), dateStr);
    }

    public static String format(ReportDto reportDto) {
        String dateStr = reportDto.getDateOfCreation().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return String.format("Дата: %s,\nтип отчета: %s,\nописание %s\n",
                    dateStr, reportDto.getType(), reportDto.getDesc());

    }
}
