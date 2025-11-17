package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.MessageType

data class RequestMappingInfo(
    val token: String,
    /**
     * /hello -> @BotRequest(value=arrayOf("/hello"), type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE))
     * это означает что метод хочет принимать команды /hello от пользователя
     */
    val pattern: String,
    val patternsCount: Int,
    val messageTypes: Set<MessageType>,
)