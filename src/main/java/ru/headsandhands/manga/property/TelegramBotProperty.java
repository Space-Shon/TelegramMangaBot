package ru.headsandhands.manga.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Data
@ConfigurationProperties(prefix = "telegram-bot.config")
public class TelegramBotProperty {

    private String name;
    private String token;

}
