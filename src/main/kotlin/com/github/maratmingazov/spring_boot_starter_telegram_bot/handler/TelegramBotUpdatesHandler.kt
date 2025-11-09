package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update

interface TelegramBotUpdatesHandler {

    /**
     * Здесь мы определяем, как будем обрабатывать полученные обновления
     */
    fun process(token: String, bot: TelegramBot, updates: List<Update>)
}