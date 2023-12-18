package ru.headsandhands.manga.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.headsandhands.manga.handler.BotHandler;

@RestController
@RequestMapping("/bot")
public class BotController {

    private final BotHandler botHandler;

    public BotController(BotHandler botHandler) {
        this.botHandler = botHandler;
    }

    @PostMapping("/sendMessage")
    public Message sendMessage(@RequestParam Long chatId){
        return botHandler.sendMessage(chatId);
    }




}
