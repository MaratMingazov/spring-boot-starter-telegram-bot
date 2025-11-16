package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.MessageType

data class RequestMappingInfo(
    private val token: String,
    private val pattern: String?,
    private val patternsCount: Int,
    private val messageTypes: Set<MessageType>,
)