package org.pemmobc1.currencyconvert.Retrofit

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {
    @GET("v6/d20ab0bca1546094d4c5d65f/latest/{currency}")
    fun getExchangeCurrency(@Path("currency") currency: String): Call<JsonObject>
}

