package com.focus617.mylib.retrofit
//
//import okhttp3.Dispatcher
//import okhttp3.OkHttpClient
//import retrofit2.Call
//import retrofit2.Retrofit
//import retrofit2.http.GET
//import java.util.*
//import java.util.concurrent.Executors
//
//data class Config(val bannerId: String, val bannerUrl: String)
//
//interface UpdateApi {
//
//    @GET("/")
//    suspend fun getConfigSuspend(): List<Config>
//
//    @GET("/")
//    fun getConfig(): Call<List<Config>>
//
//    @GET("/")
//    fun getConfigObservable(): Observable<List<Config>>
//}
//
//val updateApi by lazy {
//    Retrofit.Builder()
//        .client(
//            OkHttpClient.Builder()
//                .dispatcher(Dispatcher(Executors.newFixedThreadPool(3) { runnable ->
//                    Thread(runnable).also{ it.isDaemon = true}
//                }))
//                .build()
//        )
////        .addCallAdapterFactory()
//}