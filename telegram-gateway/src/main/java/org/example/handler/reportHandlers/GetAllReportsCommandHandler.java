package org.example.handler.reportHandlers;

import lombok.RequiredArgsConstructor;
import org.example.entity.Role;
import org.example.entity.State;
import org.example.entity.TelegramUser;
import org.example.entity.dto.ReportAdminDto;
import org.example.entity.dto.ReportDto;
import org.example.entity.util.ReplyMessage;
import org.example.handler.CommandHandler;
import org.example.service.KeyboardService;
import org.example.service.PaginationService;
import org.example.service.TelegramBaseService;
import org.example.service.output.ReportOutputService;
import org.example.service.output.UserOutputService;
import org.example.tempStorage.TempStorage;
import org.example.util.ReportDtoFormatter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAllReportsCommandHandler implements CommandHandler {

    private final PaginationService paginationService;
    private final TelegramBaseService baseService;
    private final ReportOutputService outputService;
    private final UserOutputService userOutputService;
    private final KeyboardService keyboardService;

    @Override
    public String getCommandName() {
        return "getAllReports";
    }

    @Override
    public List<ReplyMessage> handle(String update, TelegramUser user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            return List.of(new ReplyMessage(user.getTgId(), "НЕТ ДОСТУПА.", null));
        }
        if (!user.getState().equals(State.CHECK_REPORTS)) {
            baseService.updateUserState(user.getTgId(), State.CHECK_REPORTS);
        }

        int page = paginationService.getCurrentPage(user.getTgId());
        List<ReportAdminDto> allReportsByPage = outputService.getAllReportsByPage(page);
        setNameInReports(allReportsByPage);
        StringBuilder sb = new StringBuilder();
        for (ReportAdminDto dto : allReportsByPage) {
            sb.append(ReportDtoFormatter.format(dto));
        }

        return List.of(new ReplyMessage(user.getTgId(), sb.toString(), keyboardService.getPaginationKeyboard()));
    }

    private void setNameInReports(List<ReportAdminDto> dtos) {
        Set<Long> collect = dtos.stream()
                .map(ReportAdminDto::getUserId)
                .collect(Collectors.toSet());
        Map<Long, String> namesById = userOutputService.getNamesById(collect);
        dtos.forEach(s -> s.setUserName(namesById.getOrDefault(s.getUserId(), "Unknown user")));
    }
}
