package com.xatkit.plugins.telegram.platform;

import com.xatkit.plugins.chat.platform.ChatPlatform;
import com.xatkit.plugins.telegram.platform.io.TelegramIntentProvider;
import org.telegram.telegrambots.bots.DefaultBotOptions;
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

    String botToken;
    String botName;
    String botUsername;
    TelegramIntentProvider telegramIntentProvider;

    public TelegramBotWrapperLongPolling(String botToken, String botName, String botUsername,
                                         TelegramIntentProvider telegramIntentProvider) {
        super();
        this.botToken = botToken;
        this.botName = botName;
        this.botUsername = botUsername;
        this.telegramIntentProvider = telegramIntentProvider;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public String getBotName() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        telegramIntentProvider.handle(update);
    }
}
