package com.ahasanidea.weatherforecast.data.network

import com.ahasanidea.weatherforecast.data.network.response.Post
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import okio.Buffer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.IOException


//http://jsonplaceholder.typicode.com/posts/1
interface PostWebApiService {
    @GET("posts/{id}")
    fun getPost(
        @Path("id") id: Int
    ): Deferred<Post>

    @POST("posts")
    @FormUrlEncoded
    fun savePost(
        @Field("title") title: String
    ): Deferred<Post>

    companion object {
        operator fun invoke(): PostWebApiService {
            val requestInterceptor = Interceptor { chain ->
                var request = chain.request()

                val requestBuilder = request.newBuilder()
                val formBody = FormBody.Builder()
                    .add("body", "Body")
                    .add("userId", "12")
                    .build()
                var postBodyString = bodyToString(request.body())
                val concat = if (postBodyString.isNotEmpty()) "&" else ""
                postBodyString = postBodyString + concat + bodyToString(formBody)
                request = requestBuilder.post(
                    RequestBody.create(
                        MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
                        postBodyString
                    )
                )
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://jsonplaceholder.typicode.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PostWebApiService::class.java)
        }

        fun bodyToString(request: RequestBody?): String {
            try {
                var buffer = Buffer()
                request?.writeTo(buffer)
                return buffer.readUtf8()
            } catch (e: IOException) {
                return "error"
            }
        }
    }
}
