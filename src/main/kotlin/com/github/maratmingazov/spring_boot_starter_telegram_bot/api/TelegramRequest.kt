package com.github.maratmingazov.spring_boot_starter_telegram_bot.api

import com.pengrad.telegrambot.Callback
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.model.User

data class TelegramRequest(
    val telegramBot: TelegramBot,
    val update: Update, // это update который прислал telegramBot
    val messageType: MessageType,
    val basePattern: String,
    val templateVariables: Map<String, String>,
    val message: Message,
    val text: String,
    val chat: Chat,
    val user: User,
    val callback: Callback<*,*>? = null,
    ) {
}