package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.MessageType
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.User

data class TelegramBotEvent(
    val token: String,
    val telegramBot: TelegramBot,
    val update: Update,
    val message: Message? = null,
    val chat: Chat? = null,
    val user: User? = null,
    val text: String,
    val messageType: MessageType,
) {

    companion object {
        fun fromUpdate(token: String, telegramBot: TelegramBot, update: Update): TelegramBotEvent {
            val message = firstNonNull(update.message(), update.editedMessage(), update.channelPost(), update.editedChannelPost())
            var chat: Chat? = null
            var user: User? = null
            var text: String? = null
            var messageType: MessageType = MessageType.UNSUPPORTED

            message?.let {
                chat = firstNonNull(message.chat())
                user = firstNonNull(message.from(), message.leftChatMember())
                text = message.text()
                messageType = MessageType.MESSAGE
            }

            return TelegramBotEvent(
                token = token,
                telegramBot = telegramBot,
                update = update,
                message = message,
                chat = chat,
                user = user,
                text = text?:"",
                messageType = messageType
            )
        }

        private fun <T> firstNonNull(vararg values: T): T? =
            values.firstOrNull { it != null }
    }

}