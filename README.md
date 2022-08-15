Xatkit Telegram Platform
=====

[![License Badge](https://img.shields.io/badge/license-EPL%202.0-brightgreen.svg)](https://opensource.org/licenses/EPL-2.0)
[![Wiki Badge](https://img.shields.io/badge/doc-wiki-blue)](https://github.com/xatkit-bot-platform/xatkit/wiki/Xatkit-Slack-Platform)


Receive and send messages from [Telegram](https://telegram.org/).

The Telegram platform is a concrete implementation of the [*ChatPlatform*](https://github.com/xatkit-bot-platform/xatkit-chat-platform).

We rely on [this](https://github.com/rubenlagus/TelegramBots) Java-based wrapper of the [Telegram API for Bots](https://core.telegram.org/api#bot-api).
## Providers

The Telegram platform defines the following providers:

| Provider                   | Type  | Context Parameters | Description                                                  |
| -------------------------- | ----- | ------------------ | ------------------------------------------------------------ |
| ChatProvider | Intent | - `chat.channel`: the identifier of the channel that sent the message<br/> - `chat.username`: the name of the user that sent the message<br/> - `chat.rawMessage`: the raw message sent by the user (before NLP processing) | Receive messages from a communication channel and translate them into Xatkit-compatible intents (*inherited from [ChatPlatform](https://github.com/xatkit-bot-platform/xatkit-chat-platform)*) |
| TelegramIntentProvider | Intent | - `chat.channel`: the identifier of the channel (chatID in Telegram terminology) that sent the message<br/> - `chat.username`: the name of the bot that sent the message<br/> - `chat.rawMessage`: the raw message sent by the user (before NLP processing) | Receive messages from a Telegram chat and translate them into Xatkit-compatible intents |


## Actions

| Action | Parameters                                                   | Return                         | Return Type | Description                                                 |
| ------ | ------------------------------------------------------------ | ------------------------------ | ----------- | ----------------------------------------------------------- |
| Reply | - `message` (**String**): the message to post as a reply <br/> - `buttons` (**List[String]**, *Optional*): a list of values to render as quick message buttons as part of a custom [ReplyKeyboard](https://core.telegram.org/type/ReplyMarkup) | The posted message | String | Posts the provided `message` as a reply to a received message (*inherited from [ChatPlatform](https://github.com/xatkit-bot-platform/xatkit-chat-platform)*). If the `buttons` parameter is specified the chat window will also print quick message buttons to drive the conversation. |


## Options

The Telegram platform supports the following configuration options

| Key                  | Values | Description                                                  | Constraint    |
| -------------------- | ------ | ------------------------------------------------------------ | ------------- |
| `xatkit.telegram.token` | String | The Telegram token used by Xatkit to interact with the [Telegram API](https://core.telegram.org/bots). You can get a token via the [BotFather service](https://core.telegram.org/bots#6-botfather) | Mandatory
| `xatkit.telegram.botusername` | String | The username of your Telegram bot | Mandatory
| `xatkit.telegram.botname` | String | The name of your Telegram bot | Optional
| `xatkit.telegram.ignore_fallback_on_group_channels` | Boolean | Whether the bot should simply ignore messages it doesn't understand when in a group | Optional

## Using the Telegram platform

Check the BotTest sample bot included in the repo for an example of a bot using this platform. Make sure also to include this dependency to your pom

```xml
        <dependency>
            <groupId>com.xatkit</groupId>
            <artifactId>telegram-platform</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
```
## Tips

### Bots that start the conversation
To be able to write to a telegram group or user, you need to know the chat ID of that group or user.

If the user starts a conversation, the channel ID will be stored in the context by the `TelegramIntentProvider` in the `handle` method (see `String channel = message.getChatId().toString();`);

But if you want the bot to start the conversation, you'll need to create a context with the [proper chat id](https://www.alphr.com/find-chat-id-telegram/) yourself.

```java
StateContext contextTelegram = telegramPlatform.createSessionFromChannel(telegramChatId);
telegramPlatform.reply(contextTelegram, telegramMessage);
```

### Format of the strings for the bot

You can use markdown to format the strings that you send to the bot. The string can also contain emojis. Add them using their emoji unicode value.


