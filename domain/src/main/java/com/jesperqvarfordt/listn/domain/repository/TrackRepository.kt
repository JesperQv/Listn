package com.jesperqvarfordt.listn.domain.repository

import com.jesperqvarfordt.listn.domain.model.Track
import io.reactivex.Observable

interface TrackRepository {

    fun search(query: String?): Observable<List<Track>>

    fun getTracksOnChart(chartUrl: String): Observable<List<Track>>

}