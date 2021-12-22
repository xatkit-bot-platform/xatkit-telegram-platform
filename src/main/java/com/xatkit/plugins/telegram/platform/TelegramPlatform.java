package com.xatkit.plugins.telegram.platform;

import com.xatkit.core.platform.RuntimePlatform;
import com.xatkit.plugins.chat.platform.ChatPlatform;
import com.xatkit.plugins.chat.platform.io.ChatIntentProvider;
import com.xatkit.plugins.telegram.platform.io.TelegramIntentProvider;

/**
 * A {@link ChatPlatform} class that connects and interacts with the Telegram API.
 */
public class TelegramPlatform extends ChatPlatform {

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
        return new TelegramIntentProvider(this);
    }



}
