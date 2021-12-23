package com.xatkit.plugins.telegram.platform;

import com.xatkit.plugins.telegram.platform.io.TelegramIntentProvider;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * A generic {@link org.telegram.telegrambots.meta.generics.TelegramBot} class that will act as proxy of the bot in
 * Telegram.
 *
 * The bot uses the LongPolling method where the bot periodically pings Telegram for updates.
 * An alternative method offered by Telegram would be to use WebHooks {@see https://core.telegram
 * .org/bots/api#setwebhook}
 */
public class TelegramBotWrapperLongPolling extends TelegramLongPollingBot {

    final private String BOT_TOKEN;
    final private String BOT_NAME;
    final private String BOT_USERNAME;
    private TelegramIntentProvider telegramIntentProvider;

    public TelegramBotWrapperLongPolling(String botToken, String botName, String botUsername,
                                         TelegramIntentProvider telegramIntentProvider) {
        super();
        this.BOT_TOKEN = botToken;
        this.BOT_NAME = botName;
        this.BOT_USERNAME = botUsername;
        this.telegramIntentProvider = telegramIntentProvider;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    public String getBotName() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramIntentProvider.handle(update);
    }
}
