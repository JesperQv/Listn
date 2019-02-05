package com.jesperqvarfordt.listn.domain.repository

import com.jesperqvarfordt.listn.domain.model.Track

interface TrackRepository {

    suspend fun search(query: String?): List<Track>

    suspend fun getTracksOnChart(chartUrl: String): List<Track>

}