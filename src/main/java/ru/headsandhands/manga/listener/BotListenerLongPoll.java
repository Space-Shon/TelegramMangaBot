package ru.headsandhands.manga.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.headsandhands.manga.GeneratePassword.PasswordGenerator;
import ru.headsandhands.manga.Model.User;
import ru.headsandhands.manga.property.TelegramBotProperty;
import ru.headsandhands.manga.repository.UserRepositories;
import ru.headsandhands.manga.sender.TelegramBotSender;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BotListenerLongPoll extends TelegramLongPollingBot {

    private final TelegramBotProperty telegramBotProperty;
    private final TelegramBotSender telegramBotSender;
    private final UserRepositories userRepositories;

    static final String Help_Text =
            "Dear user, With the help of this bot you can buy manga, comics in full, or chapter by chapter if you don't know what to choosen\n\n"
            + "You can execute commands from the main menu on the left or by typing the command:\n\n"
            + "Type /start to see the welcome message\n\n"
            + "Enter /comics to select the desired comics\n\n"
            + "Type /help to see this message again.";
    static final String Manga_Button = "Manga";
    static final String Error_Text = "Error occurred: ";

    public BotListenerLongPoll(TelegramBotProperty telegramBotProperty, TelegramBotSender telegramBotSender, UserRepositories userRepositories) {
        super(telegramBotProperty.getToken());
        this.telegramBotProperty = telegramBotProperty;
        this.telegramBotSender = telegramBotSender;
        this.userRepositories = userRepositories;
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand("/start", "Welcome Message"));
        botCommandList.add(new BotCommand("/help", "Info about this bot"));
        botCommandList.add(new BotCommand("/comics", "Choose Comics"));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){

            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (message){
                case "/start":
                    registerUser(update.getMessage());
                    startCommand(chatId, update.getMessage().getChat().getUserName());
                    log.info(String.format("ChatId : %s", update.getMessage().getChatId().toString()));
                    break;
                case "/help":
                    sendMessage(chatId, Help_Text);
                    log.info("Replied to user " + update.getMessage().getChat().getUserName());
                    log.info(String.format("ChatId : %s", update.getMessage().getChatId().toString()));
                    break;
                case "/comics":
                    getComics(chatId);
                    break;
                default: sendMessage(chatId, "Error");
            }

        }else if(update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            if (callbackData.equals(Manga_Button)) {
                String text = "data from database manga";
                getManga(chatId);
                executeMessageText(text, chatId, messageId);
            }
        }

    }
    public void getManga(Long chatId){
        System.out.println("Hello" + chatId);
    }

    private void getComics(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("What type of comics do you want to choose?");

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInList = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var MangaButton = new InlineKeyboardButton();
        MangaButton.setText("Manga");
        MangaButton.setCallbackData(Manga_Button);

        rowInLine.add(MangaButton);

        rowsInList.add(rowInLine);
        markup.setKeyboard(rowsInList);
        message.setReplyMarkup(markup);

        executeMessage(message);

    }

    private void executeMessageText(String text, long chatId, long messageId) {
        EditMessageText messageText = new EditMessageText();
        messageText.setChatId(String.valueOf(chatId));
        messageText.setText(text);
        messageText.setMessageId((int) messageId);

        try {
            execute(messageText);
        } catch (TelegramApiException e) {
            log.error(Error_Text + e.getMessage());
        }
    }

    private void startCommand(long chatId, String name) {

        String answer = "Hi, Resident of the Hidden Leaf Village " + name + " nice to meet you!\n";
        sendMessage(chatId, answer);
        log.info("Replied to user " + name);

    }

    private void registerUser(Message message) {
        if (userRepositories.findById(message.getChatId()).isEmpty()) {
            var chatId = message.getChatId();
            var chat = message.getChat();

            User user = new User();
            user.setId(chatId);
            user.setUsername(chat.getUserName());
            user.setPassword(PasswordGenerator.generatePassword());

            userRepositories.save(user);
            log.info("User saved: " + user);
        }
    }
    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        executeMessage(message);
    }
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(Error_Text + e.getMessage());
        }
    }


    @Override
    public String getBotUsername() {
        return telegramBotProperty.getName();
    }
}
