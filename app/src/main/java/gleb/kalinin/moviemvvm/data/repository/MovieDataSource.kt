package gleb.kalinin.moviemvvm.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import gleb.kalinin.moviemvvm.data.api.FIRST_PAGE
import gleb.kalinin.moviemvvm.data.api.TheMovieDBInterface
import gleb.kalinin.moviemvvm.data.vo.Movie
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

// We're using this PageKeyedDataSource because we need to load data, based on page number. https://api.themoviedb.org/3/movie/popular?api_key=b764966fe6510611a5336b30098acde1&language=en-US&page=1 -> page number, находится в самом конце запроса популярных фильмов.

class MovieDataSource (private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        // If response successful
                        callback.onResult(it.movieList, null, page+1)
                        networkState.postValue(NetworkState.LOADED)

                    },
                    {
                        // If something went wrong
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        // If response successful
                        if(it.totalPages >= params.key){
                            callback.onResult(it.movieList, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        // We don't have more page's
                        else {
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }

                    },
                    {
                        // If something went wrong
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message)
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

    }
}