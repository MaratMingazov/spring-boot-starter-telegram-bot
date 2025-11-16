package com.github.maratmingazov.spring_boot_starter_telegram_bot

import com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.RequestMappingInfo
import java.lang.reflect.Method

class HandlerMethodContainer {

    fun registerController(bean: Any, method: Method, mappongInfo: List<RequestMappingInfo> ): HandlerMethod {

    }
}