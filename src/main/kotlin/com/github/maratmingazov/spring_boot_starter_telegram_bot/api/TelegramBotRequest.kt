package com.github.maratmingazov.spring_boot_starter_telegram_bot.api

import com.pengrad.telegrambot.Callback
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.User

data class TelegramBotRequest(
    val telegramBot: TelegramBot,
    val update: Update, // это update который прислал telegramBot
    val messageType: MessageType,
    val basePattern: String? = null,
    val templateVariables: Map<String, String>? = null,
    val message: Message? = null,
    val text: String,
    val chat: Chat? = null,
    val user: User? = null,
    val callback: Callback<*,*>? = null,
    ) {
}