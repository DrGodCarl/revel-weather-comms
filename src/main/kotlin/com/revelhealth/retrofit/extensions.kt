package com.revelhealth.retrofit

import com.natpryce.Failure
import com.natpryce.Result
import com.natpryce.Success
import retrofit2.Call
import retrofit2.Response


fun <A> Response<A>.toResult(): Result<A, RuntimeException> =
    when {
        isSuccessful -> Success(body()!!)
        // consider having a more specific failure for http failures. Maybe wrap retrofit's HttpException
        else -> Failure(RuntimeException("request failed with code: ${this.code()}"))
    }


/**
 * To be used like a specialized `resultFrom` wrapping network calls using retrofit:
 * network { api.getById(id) }
 */
inline fun <T> network(call: () -> Call<T>) = call().execute().toResult()
