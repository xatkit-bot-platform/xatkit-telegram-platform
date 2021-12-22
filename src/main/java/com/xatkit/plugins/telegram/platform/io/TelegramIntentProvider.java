package com.xatkit.plugins.telegram.platform.io;

import com.xatkit.core.platform.io.IntentRecognitionHelper;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.execution.StateContext;
import com.xatkit.intent.RecognizedIntent;
import com.xatkit.plugins.chat.ChatUtils;
import com.xatkit.plugins.chat.platform.io.ChatIntentProvider;
import com.xatkit.plugins.telegram.TelegramUtils;
import com.xatkit.plugins.telegram.platform.TelegramPlatform;
import org.apache.commons.configuration2.Configuration;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static fr.inria.atlanmod.commons.Preconditions.checkNotNull;

public class TelegramIntentProvider extends ChatIntentProvider<TelegramPlatform> {


    /**
     * Specifies whether {@code DEFAULT_FALLBACK_INTENT}s should be ignored in group channel (default to {@code false}).
     */
    private boolean ignoreFallbackOnGroupChannels;

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

    @Override
    public void start(Configuration configuration) {
        super.start(configuration);
        checkNotNull(configuration, "Cannot construct a TelegramIntentProvier from a null configuration");
        this.ignoreFallbackOnGroupChannels =
                configuration.getBoolean(TelegramUtils.IGNORE_FALLBACK_ON_GROUP_CHANNELS_KEY,
                        TelegramUtils.DEFAULT_IGNORE_FALLBACK_ON_GROUP_CHANNELS);

    }

    /**
     * Receives the update from the {@link com.xatkit.plugins.telegram.platform.TelegramBotWrapperLongPolling} and
     * process it to generate the corresponding {@link RecognizedIntent}.
     *
     * @param update the {@link Update} data object sent by Telegram
     */
    public void handle(Update update) {

        Message message = update.getMessage();

        String rawMessage = cleanCommandSymbol(message.getText());
        String channel = message.getChat().getId().toString();
        String user = update.getChatMember().toString();
        Boolean isGroupChannel = message.getChat().isGroupChat();


        StateContext context =
                runtimePlatform.getXatkitBot().getOrCreateContext(channel);

        RecognizedIntent recognizedIntent;
        try {
            recognizedIntent =
                    IntentRecognitionHelper.getRecognizedIntent(rawMessage, context,
                            TelegramIntentProvider.this.xatkitBot);
        } catch (IntentRecognitionProviderException e) {
            throw new RuntimeException("An internal error occurred when computing" +
                    " the intent, see attached exception", e);
        }

        /*
         * Chat-related values (from ChatUtils). These are required for all the
         * platforms extending ChatPlatform.
         */
        recognizedIntent.getPlatformData().put(ChatUtils.CHAT_CHANNEL_CONTEXT_KEY
                , channel);
        recognizedIntent.getPlatformData().put(ChatUtils.CHAT_USERNAME_CONTEXT_KEY, user);
        recognizedIntent.getPlatformData().put(ChatUtils.CHAT_RAW_MESSAGE_CONTEXT_KEY, rawMessage);


        if (recognizedIntent.getDefinition().getName().equals(
                "Default_Fallback_Intent") && ignoreFallbackOnGroupChannels) {
            /*
             * First check the property, if fallback intents are not ignored no
             * need to check if this is a group channel or not
             */
            if (!isGroupChannel) {
                TelegramIntentProvider.this.sendEventInstance(recognizedIntent,
                        context);
            } else {
                /*
                 * Do nothing, fallback intents are ignored in group channels and
                 * this is a group channel.
                 */
            }
        } else {
            TelegramIntentProvider.this.sendEventInstance(recognizedIntent, context);
        }
    }

    private String cleanCommandSymbol(String message)
    {
        if (message.startsWith("/")) {
            return message.substring(1);
        }
        else {
            return message;
        }
    }

}
