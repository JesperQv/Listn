package com.jesperqvarfordt.listn.explore

import com.jesperqvarfordt.listn.device.imagecache.ImageCache
import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.usecase.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class ExplorePresenter
@Inject
constructor(private val search: SearchTracksUseCase,
            private val startPlayingPlaylist: SetPlaylistAndPlayUseCase,
            private val getCharts: GetChartsUseCase,
            private val getTracksOnChart: GetTracksOnChartUseCase,
            private val listenToMediaInfo: SubscribeToMediaInfoUseCase,
            private val imageCache: ImageCache) : ExploreContract.Presenter, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var job: Job

    private var view: ExploreContract.View? = null
    private val disposables = CompositeDisposable()

    override fun subscribe(view: ExploreContract.View) {
        job = Job()
        this.view = view
        loadChartsAndShow()
        listenToMediaInfo()
    }

    private fun loadChartsAndShow() {
        view?.toggleChartList()
        view?.showSearchBar()
        disposables.add(getCharts.execute()
                .subscribe({ charts ->
                    view?.updateCharts(charts)
                }, {
                    view?.showError()
                }))
    }

    private fun listenToMediaInfo() {
        disposables.add(listenToMediaInfo.execute()
                .subscribe({
                    view?.updatePlayingTrack(it.id)
                }, {
                    // Nothing
                }))
    }

    override fun unsubscribe() {
        view = null
        job.cancel()
        disposables.clear()
    }

    override fun searchClicked(query: String) {
        view?.toggleTrackList()
        launch {
            val tracks = search.execute(query)
            withContext(Dispatchers.Main) {
                if (tracks.isEmpty()) {
                    view?.showEmpty()
                } else {
                    view?.updateTracks(tracks)
                    imageCache.preloadImages(tracks)
                }
            }
        }
    }

    override fun trackClicked(tracks: List<Track>, clickedId: Int) {
        disposables.add(startPlayingPlaylist.execute(tracks, clickedId)
                .subscribe())
        view?.openMusicPlayer()
    }

    override fun chartClicked(chart: Chart) {
        view?.toggleTrackList()
        view?.showTitleBar(chart.name)
        launch {
            try {
                val tracks = getTracksOnChart.execute(chart)
                withContext(Dispatchers.Main) {
                    view?.updateTracks(tracks)
                    imageCache.preloadImages(tracks)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    view?.showError()
                }
            }

        }
    }

    override fun backClicked() {
        loadChartsAndShow()
    }

}