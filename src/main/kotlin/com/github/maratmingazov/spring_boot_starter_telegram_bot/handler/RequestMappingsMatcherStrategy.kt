package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor.TelegramBotEvent

/**
 * У контроллера может быть несколько методов помеченных @BotRequest аннотацией
 * Стратегия определяет какой из этих методов должен быть выбран
 */
interface RequestMappingsMatcherStrategy {

    /**
     * Мы хотим отсортировать методы @BotRequest
     * Первыми хотим расположить методы, у которых есть path, например /start
     * Чтобы если пользователь передал команду, найти в вызвать их
     * В конце будут находиться методы, которые могут обрабатывать любые сообщения
     */
    fun postProcess(mappings: List<RequestMapping>): List<RequestMapping>

    /**
     * Проверяем может ли данный метод (mappingInfo содержит информацию о методе) обработать данный telegramEvent
     */
    fun isMatched(telegramBotEvent: TelegramBotEvent, mappingInfo: RequestMappingInfo): Boolean

    /**
     * В телеграмм сообщении (text) может быть написано например /start
     * Нужно выташить все такие path
     */
    fun extractPatternVariables(text: String, mappingInfo: RequestMappingInfo): Map<String, String>
}