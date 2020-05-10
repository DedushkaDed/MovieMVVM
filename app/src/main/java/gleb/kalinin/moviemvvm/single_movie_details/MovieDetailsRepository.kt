package gleb.kalinin.moviemvvm.single_movie_details

import androidx.lifecycle.LiveData
import gleb.kalinin.moviemvvm.data.api.TheMovieDBInterface
import gleb.kalinin.moviemvvm.data.repository.MovieDetailsNetworkDataSource
import gleb.kalinin.moviemvvm.data.repository.NetworkState
import gleb.kalinin.moviemvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository (private val apiService: TheMovieDBInterface) {

    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int) : LiveData<MovieDetails> {

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieDetailsResponse

    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkDataSource.networkState
    }



}