package com.xatkit.plugins.telegram;

import com.xatkit.core.XatkitBot;
import com.xatkit.plugins.telegram.platform.TelegramPlatform;
import com.xatkit.plugins.telegram.platform.io.TelegramIntentProvider;
import lombok.val;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;

import java.util.Arrays;

import static com.xatkit.dsl.DSL.fallbackState;
import static com.xatkit.dsl.DSL.intent;
import static com.xatkit.dsl.DSL.intentIs;
import static com.xatkit.dsl.DSL.model;
import static com.xatkit.dsl.DSL.state;

/**
 * This class is used to run existing bots, and should not contain test cases.
 */
public class BotTest {

    public static void main(String[] args) {

        /*
         * Define the intents our bot will react to.
         * <p>
         * In this example we want our bot to answer greetings inputs and "how are you" questions, so we create an
         * intent for each, and we give a few example training sentences to configure the underlying NLP engine.
         * <p>
         * Note that we recommend the usage of Lombok's val when using the Xatkit DSL: the fluent API defines many
         * interfaces that are not useful for bot designers. If you don't want to use val you can use our own
         * interface IntentVar instead.
         */
        val greetings = intent("Greetings")
                .trainingSentence("Hi")
                .trainingSentence("Hello")
                .trainingSentence("Good morning")
                .trainingSentence("Good afternoon");

        //Simulating the start command
        val start = intent("Start")
                .trainingSentence("start");

        val weather = intent("Weather")
                .trainingSentence("weather");

        val pollution = intent("Pollution")
                .trainingSentence("pollution");

        TelegramPlatform telegramPlatform = new TelegramPlatform();
        TelegramIntentProvider telegramIntentProvider = telegramPlatform.getTelegramIntentProvider();

        val awaitingInput = state("AwaitingInput");
        val handleWelcome = state("HandleWelcome");
        val handleStart = state("Start");
        val handleWeather= state("Weather");
        val handlePollution = state("Pollution");


        awaitingInput
                .next()
                /*
                 * We check that the received event matches the ClientReady event defined in the
                 * ReactEventProvider. The list of events defined in a provider is available in the provider's
                 * wiki page.
                 */
                .when(intentIs(greetings)).moveTo(handleWelcome)
                .when(intentIs(start)).moveTo(handleStart);

        handleWelcome
                .body(context -> telegramPlatform.reply(context, "Hi, nice to meet you!"))
                .next()
                .moveTo(awaitingInput);

        handleStart
                .body(context -> telegramPlatform.reply(context, "Great, let's go ahead, what do you want to know "
                                + "about",
                                Arrays.asList("weather","pollution")))
                .next()
                .when(intentIs(weather)).moveTo(handleWeather)
                .when(intentIs(pollution)).moveTo(handlePollution);

        handleWeather
                .body(context -> telegramPlatform.reply(context, "It's really hot today"))
                .next()
                .moveTo(awaitingInput);

        handlePollution
                .body(context -> telegramPlatform.reply(context, "I'm dying!!!"))
                .next()
                .moveTo(awaitingInput);

        val defaultFallback = fallbackState()
                .body(context -> telegramPlatform.reply(context, "Sorry, I didn't, get it"));


        val botModel = model()
                .usePlatform(telegramPlatform)
                .listenTo(telegramIntentProvider)
                .initState(awaitingInput)
                .defaultFallbackState(defaultFallback);

        Configuration botConfiguration = new BaseConfiguration();
        botConfiguration.addProperty("xatkit.telegram.botname", "Xatkit Test");
        botConfiguration.addProperty("xatkit.telegram.botusername", "Your username");
        botConfiguration.addProperty("xatkit.telegram.token", "Your token");
     //   botConfiguration.addProperty("xatkit.telegram.ignore_fallback_on_group_channels", true);



        XatkitBot xatkitBot = new XatkitBot(botModel, botConfiguration);
        xatkitBot.run();

    }
}
