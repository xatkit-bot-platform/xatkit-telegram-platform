Xatkit Telegram Platform
=====

[![License Badge](https://img.shields.io/badge/license-EPL%202.0-brightgreen.svg)](https://opensource.org/licenses/EPL-2.0)
[![Wiki Badge](https://img.shields.io/badge/doc-wiki-blue)](https://github.com/xatkit-bot-platform/xatkit/wiki/Xatkit-Slack-Platform)


Receive and send messages from [Telegram](https://telegram.org/).

The Telegram platform is a concrete implementation of the [*ChatPlatform*](https://github.com/xatkit-bot-platform/xatkit-chat-platform).

## Providers

The Telegram platform defines the following providers:

| Provider                   | Type  | Context Parameters | Description                                                  |
| -------------------------- | ----- | ------------------ | ------------------------------------------------------------ |
| ChatProvider | Intent | - `chat.channel`: the identifier of the channel that sent the message<br/> - `chat.username`: the name of the user that sent the message<br/> - `chat.rawMessage`: the raw message sent by the user (before NLP processing) | Receive messages from a communication channel and translate them into Xatkit-compatible intents (*inherited from [ChatPlatform](https://github.com/xatkit-bot-platform/xatkit-chat-platform)*) |

## Actions

| Action | Parameters                                                   | Return                         | Return Type | Description                                                 |
| ------ | ------------------------------------------------------------ | ------------------------------ | ----------- | ----------------------------------------------------------- |

## Options

The Telegram platform supports the following configuration options

| Key                  | Values | Description                                                  | Constraint    |
| -------------------- | ------ | ------------------------------------------------------------ | ------------- |
| `xatkit.telegram.token` | String | The Telegram token used by Xatkit to interact with the [Telegram API](https://core.telegram.org/bots) |


