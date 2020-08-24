package com.example.market.io.bus

import android.os.Handler
import android.os.Looper
import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.EventBus
import java.util.concurrent.Executor

object BusProvider {
    val instance: EventBus = AsyncEventBus(object : Executor {
        private var handler: Handler? = null
        override fun execute(command: Runnable) {
            if (handler == null) {
                handler = Handler(Looper.getMainLooper())
            }
            handler!!.post(command)
        }
    })

    fun register(obj: Any?) {
        instance.register(obj)
    }

    fun unregister(obj: Any?) {
        try {
            instance.unregister(obj)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }
}