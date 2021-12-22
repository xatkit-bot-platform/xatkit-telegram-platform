package com.xatkit.plugins.telegram.platform.io;

import com.xatkit.plugins.chat.platform.io.ChatIntentProvider;
import com.xatkit.plugins.telegram.platform.TelegramPlatform;

public class TelegramIntentProvider extends ChatIntentProvider<TelegramPlatform> {

    /**
     * Constructs a {@link TelegramIntentProvider} and binds it to the provided {@code slackPlatform}.
     *
     * @param telegramPlatform the {@link TelegramPlatform} managing this provider
     */
    public TelegramIntentProvider(TelegramPlatform telegramPlatform) {
        super(telegramPlatform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        /*
         * Do nothing, the socket server is started asynchronously.
         */
    }

}
