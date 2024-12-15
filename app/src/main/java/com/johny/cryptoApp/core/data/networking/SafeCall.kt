package com.johny.cryptoApp.core.data.networking

import com.johny.cryptoApp.core.domain.utils.NetworkError
import com.johny.cryptoApp.core.domain.utils.Result
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T, NetworkError> {
   val response = try {
        execute()
    }catch (ex:UnresolvedAddressException){
        return Result.Error(NetworkError.NO_INTERNET_ERROR)
    }catch (ex:SerializationException){
        return Result.Error(NetworkError.SERIALIZATION_ERROR)
    }catch (ex:Exception){
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN_ERROR)
    }

    return responseToResult(response)
}