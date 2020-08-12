package com.example.pomodoro2.features.infra.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory

/**
 * A public interface that exposes the [getPropertiesAsync] method
 */
interface Api {
/*
    @GET("person_object.json")
    fun getProperties():
            Call<Contact>
*/
    /**
     * Returns a Coroutine [Deferred] of [NetworkContact] which can be fetched with await() if
     * in a Coroutine scope.
     * The @GET annotation indicates that the "person_object.json" endpoint will be requested with the GET
     * HTTP method
     *
     * The Coroutine Call Adapter allows us to return a Deferred, a Job with a result Deferred<Contact>
     *
     */
    @GET("person_object.json")
    fun getPropertiesAsync():
            Deferred<NetworkContact>

    companion object {
        // Network Status
        enum class ApiStatus { LOADING, ERROR, DONE }

        /**
         * Use the Retrofit builder to build a retrofit object using a Moshi converter
         * with our Moshi object.
         */
        fun create(): Api {
            val retrofit = Retrofit.Builder()
                .baseUrl(ApiConstants.API)
                //.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            return retrofit.create(Api::class.java)
        }

        /**
         * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
         * full Kotlin compatibility.
         */
        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }
}