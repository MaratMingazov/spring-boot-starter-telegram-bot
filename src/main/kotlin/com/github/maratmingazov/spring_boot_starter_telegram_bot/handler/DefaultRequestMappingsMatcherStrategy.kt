package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotEvent

class DefaultRequestMappingsMatcherStrategy(): RequestMappingsMatcherStrategy, Comparator<RequestMappingInfo> {
    override fun postProcess(mappings: List<RequestMapping>) {
    }

    override fun isMatched(
        telegramBotEvent: TelegramBotEvent,
        mappingInfo: RequestMappingInfo
    ): Boolean {
        return false
    }

    override fun extractPatternVariables(
        text: String,
        mappingInfo: RequestMappingInfo
    ): Map<String, String> {
        return mapOf()
    }

    override fun compare(
        o1: RequestMappingInfo?,
        o2: RequestMappingInfo?
    ): Int {
        return 0
    }
}