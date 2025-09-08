package org.example.handler.reportHandlers;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.TelegramUser;
import org.example.entity.dto.ReportDto;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.service.output.ReportOutputService;
import org.example.util.ReportDtoFormatter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetMyReportsCommandHandler implements CommandHandler {

    private final ReportOutputService reportOutputService;

    @Override
    public String getCommandName() {
        return "getMyReports";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (user.getRole().equals(Role.GUEST)) {
            return List.of(new ReplyMessage(user.getTgId(), "НЕТ ДОСТУПА.", null));
        }
        List<ReportDto> reportsByUserId = reportOutputService.getReportsByUserId(user.getUserId());
        if (reportsByUserId.isEmpty()) {
            return List.of(new ReplyMessage(user.getTgId(), "Отчеты не найдены", null));
        }
        StringBuilder sb = new StringBuilder();
        for (ReportDto dto : reportsByUserId) {
            if (sb.length() > 1000) {
                break;
            }
            sb.append(ReportDtoFormatter.format(dto));
            sb.append("----------------------------\n");
        }
        return List.of(new ReplyMessage(user.getTgId(), sb.toString(), null));
    }
}
