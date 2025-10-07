package com.halimjr11.eventora.utils

sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    object Error : DomainResult<Nothing>()
}