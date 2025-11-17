package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor

import java.lang.reflect.Method

class HandlerMethod(
    private val bean: Any,
    private val method: Method,
) {
}