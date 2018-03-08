package com.jesperqvarfordt.listn.data.repository

import com.jesperqvarfordt.listn.data.api.SCApi
import com.jesperqvarfordt.listn.data.mapper.TrackMapper
import com.jesperqvarfordt.listn.data.model.SCChartResponse
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository
import io.reactivex.Observable


class SCTrackRepository(api: SCApi, mapper: TrackMapper) :
        TrackRepository, SCRepository(api, mapper) {

    override fun search(query: String?): Observable<List<Track>> = api.searchTracks(query)
            .flatMapIterable { trackList -> trackList }
            .map { track -> mapper.map(track) }
            .toList()
            .toObservable()

    override fun getTracksOnChart(chartUrl: String): Observable<List<Track>> =
            api.getTracksOnChart(chartUrl)
                    .map { t: SCChartResponse -> t.collection }
                    .flatMapIterable { trackList -> trackList }
                    .map { chartTrack -> chartTrack.track }
                    .map { track -> mapper.map(track) }
                    .toList()
                    .toObservable()


}