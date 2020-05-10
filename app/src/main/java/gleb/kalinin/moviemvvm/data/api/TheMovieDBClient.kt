package gleb.kalinin.moviemvvm.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "b764966fe6510611a5336b30098acde1"
const val BASE_URL = "https://api.themoviedb.org/3/"

const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342" // Начало URL -> облачное хранилище всех изображений. К нему добавляем: "poster_path": "Ссылка на картинку". Это и есть ссылка на изображение.

    // https://api.themoviedb.org/3/movie/popular?api_key=b764966fe6510611a5336b30098acde1&language=en-US&page=1 - Популярные фильмы
    // https://api.themoviedb.org/3/movie/338762?api_key=b764966fe6510611a5336b30098acde1&language=en-US - Информация о одном фильме
    // https://image.tmdb.org/t/p/w342/8WUVHemHFH2ZIP6NWkwlHWsyrEL.jpg -> Логотип




object TheMovieDBClient {

    fun getClient():TheMovieDBInterface {

        val requestInterceptor = Interceptor {chain ->

            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)
    }
}