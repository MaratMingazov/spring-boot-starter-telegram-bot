package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.MessageType
import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotEvent
import org.springframework.util.AntPathMatcher

class DefaultRequestMappingsMatcherStrategy(): RequestMappingsMatcherStrategy, Comparator<RequestMappingInfo> {

    private val pathMatcher = AntPathMatcher()

    override fun postProcess(mappings: List<RequestMapping>): MutableList<RequestMapping> {
        val result = mappings.toMutableList()
        result.sortWith { a, b -> compare(a.mappingInfo, b.mappingInfo) }
        return result
    }

    override fun isMatched(
        telegramBotEvent: TelegramBotEvent,
        mappingInfo: RequestMappingInfo
    ): Boolean {
        if (!mappingInfo.messageTypes.contains(telegramBotEvent.messageType) && !mappingInfo.messageTypes.contains(MessageType.ANY)) {
            return false
        }

        /**
         * в метода @BotRequest можно объявить паттерны
         * @BotRequest(value=arrayOf("/hello"), type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE))
         * @BotRequest(value=arrayOf("/start"), type=arrayOf(MessageType.CALLBACK_QUERY, MessageType.MESSAGE))
         * Поэтому если пользователь прислал текст содержащий какую-то команду и если есть такой метод,
         * то мы этот метод и вызовев
         */
        return pathMatcher.match(mappingInfo.pattern, telegramBotEvent.text);
    }

    override fun extractPatternVariables(
        text: String,
        mappingInfo: RequestMappingInfo
    ): Map<String, String> {
        return pathMatcher.extractUriTemplateVariables(mappingInfo.pattern, text);
    }

    override fun compare(o1: RequestMappingInfo, o2: RequestMappingInfo): Int {
        val compared = pathMatcher
            .getPatternComparator("")
            .compare(o1.pattern, o2.pattern)

        if (compared != 0) return compared

        // Compare patterns count
        if (o1.patternsCount != o2.patternsCount) {
            return o1.patternsCount.compareTo(o2.patternsCount)
        }

        // Compare message types
        val t1 = o1.messageTypes
        val t2 = o2.messageTypes

        val t1HasAny = MessageType.ANY in t1
        val t2HasAny = MessageType.ANY in t2

        return when {
            t1HasAny && t2HasAny -> 0
            t1HasAny -> 1
            t2HasAny -> -1
            t1.size != t2.size -> t1.size.compareTo(t2.size)
            else -> 0
        }
    }
}