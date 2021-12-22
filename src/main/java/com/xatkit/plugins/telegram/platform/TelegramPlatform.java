package com.xatkit.plugins.telegram.platform;

import com.xatkit.core.XatkitBot;
import com.xatkit.core.XatkitException;
import com.xatkit.core.platform.RuntimePlatform;
import com.xatkit.plugins.chat.platform.ChatPlatform;
import com.xatkit.plugins.chat.platform.io.ChatIntentProvider;
import com.xatkit.plugins.telegram.platform.io.TelegramIntentProvider;
import fr.inria.atlanmod.commons.log.Log;
import org.apache.commons.configuration2.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import com.xatkit.plugins.telegram.TelegramUtils;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.util.HashMap;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 * A {@link ChatPlatform} class that connects and interacts with the Telegram API.
 */
public class TelegramPlatform extends ChatPlatform {

    TelegramBotWrapperLongPolling telegramBot;
    TelegramBotsApi telegramBotsAPI;
    TelegramIntentProvider telegramIntentProvider = new TelegramIntentProvider(this);

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatIntentProvider<? extends ChatPlatform> getChatIntentProvider() {
        return this.getTelegramIntentProvider();
    }

    /**
     * Initializes and returns a new {@link TelegramIntentProvider}.
     *
     * @return the {@link TelegramIntentProvider}
     */
    public TelegramIntentProvider getTelegramIntentProvider() {
        return telegramIntentProvider;
    }


    /**
     * {@inheritDoc}
     * <p>
     * This method initializes the underlying {@link org.telegram.telegrambots.bots.TelegramLongPollingBot} client.
     * And registers it in the TelegramBotsAPI that will manage the interactions with the Telegram API.
     *
     * @see TelegramUtils#TELEGRAM_TOKEN_KEY
     * @see TelegramUtils#TELEGRAM_BOT_NAME
     * @see TelegramUtils#TELEGRAM_BOT_USERNAME
     */
    @Override
    public void start(XatkitBot xatkitBot, Configuration configuration) {
        super.start(xatkitBot, configuration);
        String telegramToken = configuration.getString(TelegramUtils.TELEGRAM_TOKEN_KEY);
        String botName = configuration.getString(TelegramUtils.TELEGRAM_BOT_NAME);
        String botUsername = (TelegramUtils.TELEGRAM_BOT_USERNAME);
        if (nonNull(telegramToken) && nonNull(botUsername) && nonNull(botName)) {
           try {
               //We create the bot and link it with the telegramIntentProvider that will process updates from
               // Telegram received by the bot
               telegramBot = new TelegramBotWrapperLongPolling(telegramToken, botName, botUsername, telegramIntentProvider);
               TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
               telegramBotsAPI.registerBot(telegramBot);
           } catch (TelegramApiException e) {
                throw new XatkitException("Cannot initialize the Telegram connector with the given token and "
                        + "username", e);
           }
        } else {
            Log.info("The configuration must include the telegram token, bot name and bot username");
            throw new XatkitException("Cannot initialize the Telegram connector");
        }
    }

}
