package com.github.maratmingazov.spring_boot_starter_telegram_bot.api

/**
 * Когда мы создадим @BotController класс и @BotRequest метод,
 * то у такого метода можно указать какие типы сообщений мы хотим обрабатывать
 */
enum class MessageType {
    ANY,
    MESSAGE,
    EDITED_MESSAGE,
    CALLBACK_QUERY,
    UNSUPPORTED,
}