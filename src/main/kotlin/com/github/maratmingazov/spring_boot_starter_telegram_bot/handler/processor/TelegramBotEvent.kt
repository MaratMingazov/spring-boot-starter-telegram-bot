package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

class TelegramBotEvent(
    private val token: String,
    private val bot: TelegramBot,
    private val update: Update
) {
}