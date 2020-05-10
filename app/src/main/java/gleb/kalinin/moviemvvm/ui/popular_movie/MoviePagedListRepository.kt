package gleb.kalinin.moviemvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import gleb.kalinin.moviemvvm.data.api.POST_PER_PAGE
import gleb.kalinin.moviemvvm.data.api.TheMovieDBInterface
import gleb.kalinin.moviemvvm.data.repository.MovieDataSource
import gleb.kalinin.moviemvvm.data.repository.MovieDataSourceFactory
import gleb.kalinin.moviemvvm.data.repository.NetworkState
import gleb.kalinin.moviemvvm.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoviePagedListRepository (private val apiService: TheMovieDBInterface) {

    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>> {
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config : PagedList.Config =  PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE) // 20
            .build()

        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()

        return moviePagedList
    }

    // NetworkState находится внутри MovieDataSource  (переменная в начале)
    fun getNetworkState(): LiveData<NetworkState> {
        // Инициализируем movieDataSource (находится в MovieDataSource)
        return Transformations.switchMap<MovieDataSource, NetworkState> (
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState)
    }
}