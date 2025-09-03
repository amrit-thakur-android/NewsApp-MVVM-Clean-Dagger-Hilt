package com.amritthakur.newsapp.presentation.common

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    /**
     * Main dispatcher - for UI updates
     */
    val main: CoroutineDispatcher

    /**
     * IO dispatcher - for network calls, file operations
     */
    val io: CoroutineDispatcher

    /**
     * Default dispatcher - for CPU-intensive work
     */
    val default: CoroutineDispatcher

    /**
     * Unconfined dispatcher - for testing or special cases
     */
    val unconfined: CoroutineDispatcher
}