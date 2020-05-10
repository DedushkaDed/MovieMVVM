package gleb.kalinin.moviemvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import gleb.kalinin.moviemvvm.data.repository.NetworkState
import gleb.kalinin.moviemvvm.data.vo.Movie
import gleb.kalinin.moviemvvm.ui.single_movie_details.MovieDetailsRepository
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel (private val movieDetailsRepository: MoviePagedListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        movieDetailsRepository.fetchLiveMoviePagedList(compositeDisposable)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieDetailsRepository.getNetworkState()
    }

    fun listIsEmpty ():Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}