package ru.headsandhands.manga.sender;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.headsandhands.manga.property.TelegramBotProperty;

@Component
public class TelegramBotSender extends DefaultAbsSender {

    public TelegramBotSender(TelegramBotProperty telegramBotProperty){
        super(new DefaultBotOptions(), telegramBotProperty.getToken());
    }

    public Message sendMessageBy(Long chatId, Integer messageId, String text) throws TelegramApiException {
        return execute(SendMessage
                .builder()
                .chatId(chatId)
                .replyToMessageId(messageId)
                .text(text)
                .build()
        );
    }


}
