package gleb.kalinin.moviemvvm.data.api

import gleb.kalinin.moviemvvm.data.vo.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBInterface {

    // https://api.themoviedb.org/3/movie/popular?api_key=b764966fe6510611a5336b30098acde1&language=en-US&page=1 - Популярные фильмы
    // https://api.themoviedb.org/3/movie/338762?api_key=b764966fe6510611a5336b30098acde1&language=en-US - Информация о одном фильме
    // https://api.themoviedb.org/3/ - BASE_URL

    @GET("movie/{movie_id}")
    fun getMovieDetails (@Path("movie_id") id: Int) : Single<MovieDetails>  // Single - rxJava
}

