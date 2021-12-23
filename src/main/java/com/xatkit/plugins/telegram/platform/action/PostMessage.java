package com.xatkit.plugins.telegram.platform.action;

import com.xatkit.core.XatkitException;
import com.xatkit.core.platform.action.RuntimeMessageAction;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.telegram.platform.QuickButtonDescriptor;
import com.xatkit.plugins.telegram.platform.TelegramBotWrapperLongPolling;
import com.xatkit.plugins.telegram.platform.TelegramPlatform;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;

/**
 * A {@link RuntimeMessageAction} that posts a {@code message} to a given xatkit-react {@code channel}.
 */
public class PostMessage extends RuntimeMessageAction<TelegramPlatform> {

    /**
     * The Telegram chat to post the message to.
     */
    protected String channel;

    /**
     * The descriptors of the <i>quick buttons</i> to print to the user.
     */
    private List<QuickButtonDescriptor> quickButtonDescriptors;

    /**
     * Constructs a new {@link PostMessage} with the provided {@code platform}, {@code session}, {@code
     * message}, and {@code channel}.
     * <p>
     * This constructor is similar to {@code new PostMessage(platform, session, message, Collections.emptyList
     * (), channel)}.
     *
     * @param platform the {@link TelegramPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param message  the message to post
     * @param channel  the xatkit-react channel to post the message to
     * @throws IllegalArgumentException if the provided {@code message} or {@code channel} is {@code null}
     */
    public PostMessage(@NonNull TelegramPlatform platform, @NonNull StateContext context, @NonNull String message,
                       @NonNull String channel) {
        this(platform, context, message, Collections.emptyList(), channel);
    }


    /**
     * Constructs a new {@link PostMessage} with the provided {@code platform}, {@code session}, {@code
     * message}, {@code buttons}, and {@code channel}.
     *
     * @param platform the {@link TelegramPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param message  the message to post
     * @param buttons  the quick message buttons to display with the message
     * @param channel  the xatkit-react channel to post the message to
     * @throws IllegalArgumentException if the provided {@code channel} is {@code empty}
     */
    public PostMessage(@NonNull TelegramPlatform platform, @NonNull StateContext context, @NonNull String message,
                       @NonNull List<String> buttons, @NonNull String channel) {
        super(platform, context, message);
        checkArgument(!(channel.isEmpty()), "Cannot construct a %s action with the provided "
                + "channel %s, expected a non-null and not empty String", this.getClass().getSimpleName(), channel);
        this.channel = channel;
        this.quickButtonDescriptors = new ArrayList<>();

        //For now we assign the same value to the button label and value descriptors as Telegram doesn't offer the
        // option to use two different values
        buttons.forEach(label -> this.quickButtonDescriptors.add(new QuickButtonDescriptor(label, label)));
    }

    /**
     * Posts the provided {@code message} to the given {@code channel}.
     * <p>
     * Posted messages are pushed to the telegra client through the
     * {@link com.xatkit.plugins.telegram.platform.TelegramBotWrapperLongPolling} attached to the
     * {@link TelegramPlatform}
     *
     * @return {@code null}
     */
    @Override
    protected Object compute() {
        TelegramBotWrapperLongPolling bot = this.runtimePlatform.getTelegramBot();

        SendMessage sendMessage = new SendMessage(); // Create a message object object
        sendMessage.setChatId(this.channel);
        sendMessage.setText(this.message);
        sendMessage.enableMarkdown(true);
        //If we have to provide a set of default options for the bot together with the textual response
        if (!quickButtonDescriptors.isEmpty()) {

            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(true);

            // Create a list of keyboard rows
            List<KeyboardRow> keyboard = new ArrayList<>();
            for (QuickButtonDescriptor qb : quickButtonDescriptors) {
                KeyboardRow kr = new KeyboardRow();
                kr.add(qb.getValue());
                keyboard.add(kr);
            }
            replyKeyboardMarkup.setKeyboard(keyboard);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        } else {
            // We completely remove the customkeyboard that could have been created by a previous button-based message
            // As described here: {@see https://stackoverflow.com/questions/48120494/how-do-you-remove-reply-keyboard-without-sending-a-message-in-telegram} even if
            // we mark keyboards as one-time the keyboard still remains as an option
            ReplyKeyboardRemove keyboardRemove= new ReplyKeyboardRemove(true);
            sendMessage.setReplyMarkup(keyboardRemove);
        }

        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new XatkitException(MessageFormat.format("Cannot send the message {0} to the Telegram API",
                    this.message), e);
        }
        return null;
    }

    @Override
    protected StateContext getClientStateContext() {
        return this.runtimePlatform.createSessionFromChannel(this.channel);
    }

}
