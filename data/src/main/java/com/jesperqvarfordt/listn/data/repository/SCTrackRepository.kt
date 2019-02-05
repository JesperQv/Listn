package com.jesperqvarfordt.listn.data.repository

import com.jesperqvarfordt.listn.data.api.SCApi
import com.jesperqvarfordt.listn.data.mapper.TrackMapper
import com.jesperqvarfordt.listn.domain.model.Track
import com.jesperqvarfordt.listn.domain.repository.TrackRepository


class SCTrackRepository(api: SCApi, mapper: TrackMapper) :
        TrackRepository, SCRepository(api, mapper) {

    override suspend fun search(query: String?): List<Track> = api.searchTracks(query)
            .await()
            .map { track -> mapper.map(track) }

    override suspend fun getTracksOnChart(chartUrl: String): List<Track> =
            api.getTracksOnChart(chartUrl)
                    .await()
                    .collection
                    .map { chartTrack -> mapper.map(chartTrack.track) }
                    .toList()

}