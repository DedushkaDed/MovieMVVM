package gleb.kalinin.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gleb.kalinin.moviemvvm.data.api.TheMovieDBInterface
import gleb.kalinin.moviemvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

            // You want to dispose rxJava thread? We can use compositeDisposable -> It will be more clear when we implemented.

class MovieDetailsNetworkDataSource (private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable) {

                                                                    // _ (нижнее подчеркивание) обозначает -> private.

    private val _networkState = MutableLiveData<NetworkState>()     // We using: MutableLiveData -> because LiveData is not Mutable by nature. So we can't change <NetworkState>, but! With MutableLiveData - we can change the value.
    val networkState: MutableLiveData<NetworkState>
        get() = _networkState                                       // with this get, no need to implement get function to get networkState


    private val _downloadedMovieDetailsResponse = MutableLiveData<MovieDetails>()
    val downloadedMovieDetailsResponse: LiveData<MovieDetails>
        get() = _downloadedMovieDetailsResponse

    fun fetchMovieDetails(movieId: Int) {

        _networkState.postValue(NetworkState.LOADING)

        try {
            compositeDisposable.add(
                apiService.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                        {
                                                                    // If network call is success  -> we get the movie details.
                            _downloadedMovieDetailsResponse.postValue(it)
                            _networkState.postValue(NetworkState.LOADED)

                        },
                        {
                                                                    // If there's error
                            _networkState.postValue(NetworkState.ERROR)
                            Log.e("MovieDetailsDataSource", it.message)
                        }

                    )

            )
        }

        catch (e: Exception){
            Log.e("MovieDetailsDataSource", e.message)
        }
    }


}