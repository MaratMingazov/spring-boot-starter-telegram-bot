package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.MessageType

data class RequestMappingInfo(
    val token: String,
    val pattern: String?,
    val patternsCount: Int,
    val messageTypes: Set<MessageType>,
)