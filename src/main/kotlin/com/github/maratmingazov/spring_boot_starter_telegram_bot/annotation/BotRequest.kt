package com.github.maratmingazov.spring_boot_starter_telegram_bot.annotation

import com.github.maratmingazov.spring_boot_starter_telegram_bot.api.MessageType
import org.springframework.core.annotation.AliasFor

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BotRequest (

    /**
     * AliasFor нужно чтобы сделать аннотации взаимозаменяемыми
     * Чтобы можно было писать вот так
     * @BotRequest("/foo")
     * @BotRequest(path = "/foo")
     * @BotRequest(value = "/foo")
     */
    @get:AliasFor("path")
    val value: Array<String> = [],

    @get:AliasFor("value")
    val path: Array<String> = [],

    val type: Array<MessageType> = [MessageType.ANY]
)