package br.ufs.uolchallenge.presentation.feed

import android.arch.lifecycle.ViewModel
import br.ufs.uolchallenge.domain.FetchNewsFeed
import br.ufs.uolchallenge.domain.News
import br.ufs.uolchallenge.presentation.BehaviorsCoordinator
import br.ufs.uolchallenge.presentation.RowModelMapper
import br.ufs.uolchallenge.presentation.models.NewsFeedEntry
import io.reactivex.Observable
import io.reactivex.internal.operators.observable.ObservableNever
import java.util.concurrent.TimeUnit

/**
 * Created by bira on 11/3/17.
 */

open class NewsFeedViewModel internal constructor(
        private val usecase: FetchNewsFeed,
        private val coordinator: BehaviorsCoordinator<News>) : ViewModel() {

    private val mapper = RowModelMapper()
    private var replayer: Observable<NewsFeedEntry> = Observable.never()

    fun fetchLastestNews(shouldRefresh: Boolean = false): Observable<NewsFeedEntry> {
        if (shouldRefresh) reset()
        if (replayer == CLEAR_STATE) assignReplayer()
        return replayer
    }

    private fun assignReplayer() {
        replayer = usecase.latestNews()
                .compose(coordinator)
                .map { mapper.toRowModel(it) }
                .replay(BUFFER_COUNT)
                .autoConnect(MAX_SUBSCRIBERS)
    }

    private fun reset() {
        replayer = Observable.never()
    }

    companion object {
        val CLEAR_STATE: Observable<*> = ObservableNever.INSTANCE
        val MAX_SUBSCRIBERS = 1
        val BUFFER_COUNT = 100
    }

}