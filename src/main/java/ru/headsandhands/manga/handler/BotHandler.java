package ru.headsandhands.manga.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.headsandhands.manga.sender.TelegramBotSender;

@Component
@Slf4j
public class BotHandler {

    private final TelegramBotSender telegramBotSender;


    public BotHandler(TelegramBotSender telegramBotSender) {
        this.telegramBotSender = telegramBotSender;
    }

    public Message sendMessage(Long chatId){
        try {
            return telegramBotSender.sendMessageBy(chatId, null, "Hi!");
        }   catch (TelegramApiException e){
            log.info(e.getMessage());
            return null;
        }
    }

}
