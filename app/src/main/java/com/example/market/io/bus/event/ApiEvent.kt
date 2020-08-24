package com.example.market.io.bus.event

import androidx.annotation.Nullable

class ApiEvent<T> {
    object EventType {
        const val FRUIT_LIST_LOADED = 100
        const val FRUIT_ITEM_LOADED = 101
    }

    var eventType = 0
    var isSuccess = false

    @Nullable
    var eventData: T? = null
        private set

    constructor(eventType: Int, eventData: T) {
        this.eventType = eventType
        this.eventData = eventData
    }

    constructor(eventType: Int, success: Boolean, eventData: T) {
        this.eventType = eventType
        isSuccess = success
        this.eventData = eventData
    }

    fun setEventData(eventData: T) {
        this.eventData = eventData
    }
}