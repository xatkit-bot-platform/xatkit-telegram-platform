package com.xatkit.plugins.telegram;

import com.xatkit.plugins.chat.ChatUtils;
import com.xatkit.plugins.telegram.platform.TelegramPlatform;
import com.xatkit.plugins.telegram.platform.io.TelegramIntentProvider;
import org.apache.commons.configuration2.Configuration;

/**
 * An utility interface that holds Telegram-related helpers.
 */
public interface TelegramUtils extends ChatUtils {

    /**
     * The {@link Configuration} key to store the Telegram bot token needed to interact with the API.
     *
     * @see TelegramIntentProvider#TelegramIntentProvider(TelegramPlatform)
     */
    String TELEGRAM_TOKEN_KEY = "xatkit.telegram.token";

    /**
     * The {@link Configuration} key to store the Telegram bot name displayed in contact details and elsewhere.
     *
     * @see TelegramIntentProvider#TelegramIntentProvider(TelegramPlatform)
     */
    String TELEGRAM_BOT_NAME = "xatkit.telegram.botname";

    /**
     * The {@link Configuration} key to store the Telegram bot username identifying the bot.
     *
     * @see TelegramIntentProvider#TelegramIntentProvider(TelegramPlatform)
     */
    String TELEGRAM_BOT_USERNAME = "xatkit.telegram.botusername";

    /**
     * The {@link Configuration} key to store whether to ignore fallback intents on group channels.
     * <p>
     * This value is set to {@code false} by default.
     *
     * @see #DEFAULT_IGNORE_FALLBACK_ON_GROUP_CHANNELS
     */
    String IGNORE_FALLBACK_ON_GROUP_CHANNELS_KEY = "xatkit.telegram.ignore_fallback_on_group_channels";

    /**
     * The default value of the {@link #IGNORE_FALLBACK_ON_GROUP_CHANNELS_KEY} {@link Configuration} key.
     */
    boolean DEFAULT_IGNORE_FALLBACK_ON_GROUP_CHANNELS = false;

}
