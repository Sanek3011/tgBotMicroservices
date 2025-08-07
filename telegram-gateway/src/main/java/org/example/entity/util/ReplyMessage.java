package org.example.entity.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
@Data
@Builder
@AllArgsConstructor

public class ReplyMessage {

    private final Long chatId;
    private final String text;
    private final InlineKeyboardMarkup keyboard;

}
