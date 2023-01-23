package com.samm.practiceapp01.presentation

sealed class NewsDataState<T>(val data: T? = null, var message: String? = null) {
    class Success<T>(data: T): NewsDataState<T>(data)
    class Error<T>(message: String?, data: T? = null): NewsDataState<T>(data, message)
    class Loading<T>(data: T? = null): NewsDataState<T>(data)
}
