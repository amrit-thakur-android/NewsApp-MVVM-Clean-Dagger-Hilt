package com.amritthakur.newsapp.presentation.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationChannel @Inject constructor() {

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)

    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    fun postEvent(navigationEvent: NavigationEvent) {
        _navigationEvent.value = navigationEvent
    }

    fun clearEvent() {
        _navigationEvent.value = null
    }
}