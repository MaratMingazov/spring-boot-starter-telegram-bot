package com.github.maratmingazov.spring_boot_starter_telegram_bot.handler.processor


import com.pengrad.telegrambot.Callback
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import java.io.IOException

/**
 * Result of the Telegram request processing. Includes callback methods to handle Telegram response.
 */
//class TelegramBotCallback<T: BaseRequest<T, R>, R: BaseResponse>(
//    val request: BaseRequest<T, R>,
//    private val nestedCallback: Callback<T, R>?
//) : Callback<T,R> {
//    override fun onResponse(request: T?, response: R?) {
//        nestedCallback?.onResponse(request,response)
//    }
//
//    override fun onFailure(request: T?, e: IOException?) {
//        nestedCallback?.onFailure(request,e)
//    }
//}

class TelegramBotCallback(
    val request: SendMessage? = null,
    private val nestedCallback: Callback<SendMessage, SendResponse>?
) : Callback<SendMessage, SendResponse> {
    override fun onResponse(request: SendMessage?, response: SendResponse?) {
        nestedCallback?.onResponse(request,response)
    }

    override fun onFailure(request: SendMessage?, e: IOException?) {
        nestedCallback?.onFailure(request,e)
    }
}