package com.jesperqvarfordt.listn.explore

import com.jesperqvarfordt.listn.common.base.BasePresenter
import com.jesperqvarfordt.listn.common.base.BaseView
import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.model.Track

interface ExploreContract {

    interface View: BaseView {
        fun showSearchBar()
        fun showTitleBar(title: String)
        fun toggleTrackList()
        fun toggleChartList()
        fun updateTracks(tracks: List<Track>)
        fun updatePlayingTrack(id: Int)
        fun updateCharts(charts: List<Chart>)
        fun showEmpty()
        fun showError()
        fun openMusicPlayer()
    }

    interface Presenter: BasePresenter<View> {
        fun searchClicked(query: String)
        fun trackClicked(tracks: List<Track>)
        fun chartClicked(chart: Chart)
        fun backClicked()
    }
}