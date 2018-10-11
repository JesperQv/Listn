package com.jesperqvarfordt.listn.explore

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.jesperqvarfordt.listn.App
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.extensions.hideKeyboard
import com.jesperqvarfordt.listn.common.extensions.setVisible
import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.explore.di.TracksModule
import com.jesperqvarfordt.listn.player.PlayerActivity
import com.jesperqvarfordt.listn.player.di.DaggerTracksComponent
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_explore.view.*
import javax.inject.Inject


class ExploreFragment : Fragment(),
        ExploreContract.View {

    private val tracksAdapter = TracksAdapter(trackClicked = { tracks, index ->
        presenter.trackClicked(tracks, index)
    })

    private val chartsAdapter = ChartsAdapter(chartClicked = { chart ->
        presenter.chartClicked(chart)
    })

    @Inject
    lateinit var presenter: ExploreContract.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_explore, null)

        DaggerTracksComponent.builder()
                .appComponent(App.instance.appComponent)
                .tracksModule(TracksModule())
                .build()
                .inject(this)

        root.trackList.layoutManager = LinearLayoutManager(activity)
        root.trackList.adapter = tracksAdapter

        root.chartList.layoutManager = GridLayoutManager(activity, 2)
        root.chartList.adapter = chartsAdapter

        root.searchButton.setOnClickListener {
            performSearch(root.searchBar.text.toString())
        }

        root.searchBar.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(root.searchBar.text.toString())
                return@OnEditorActionListener true
            }
            return@OnEditorActionListener false
        })

        root.backButton.setOnClickListener({
            presenter.backClicked()
        })

        return root
    }


    private fun performSearch(query: String) {
        presenter.searchClicked(query)
        hideKeyboard()
    }

    override fun updateTracks(tracks: List<Track>) {
        tracksAdapter.updateTracks(tracks)
    }

    override fun updatePlayingTrack(id: Int) {
        tracksAdapter.updatePlayingTrackId(id)
    }

    override fun showEmpty() {
        errorView.setVisible(false)
        emptyView.setVisible(true)
    }

    override fun showError() {
        emptyView.setVisible(false)
        errorView.setVisible(true)
    }

    override fun openMusicPlayer() {
        startActivity(Intent(activity, PlayerActivity::class.java))
    }

    override fun showSearchBar() {
        searchContainer.setVisible(true)
        titleContainer.setVisible(false)
    }

    override fun showTitleBar(title: String) {
        searchContainer.setVisible(false)
        titleContainer.setVisible(true)
        chartName.text = title
    }

    override fun updateCharts(charts: List<Chart>) {
        chartsAdapter.updateCharts(charts)
    }

    override fun toggleTrackList() {
        errorView.setVisible(false)
        emptyView.setVisible(false)
        backButton.setVisible(true)
        trackList.setVisible(true)
        chartList.setVisible(false)
    }

    override fun toggleChartList() {
        errorView.setVisible(false)
        emptyView.setVisible(false)
        backButton.setVisible(false)
        chartList.setVisible(true)
        tracksAdapter.updateTracks(emptyList())
        trackList.setVisible(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.subscribe(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    fun backButtonPressed() {
        presenter.backClicked()
    }

    fun canBack(): Boolean {
        return trackList.visibility == View.VISIBLE
    }


}
