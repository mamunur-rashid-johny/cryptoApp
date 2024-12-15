package com.johny.cryptoApp.core.domain.utils

enum class NetworkError:Error {
    REQUEST_TIMEOUT,
    TOO_MANY_REQUEST,
    NO_INTERNET_ERROR,
    SERVER_ERROR,
    SERIALIZATION_ERROR,
    UNKNOWN_ERROR,
}