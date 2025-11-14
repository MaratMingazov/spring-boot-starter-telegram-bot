package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

class DefaultTelegramBotUpdatesHandler: TelegramBotUpdatesHandler{

    override fun process(token: String, bot: TelegramBot, updates: List<Update>) {
        println("DefaultTelegramBotUpdatesHandler. ${updates.size}")
    }
}