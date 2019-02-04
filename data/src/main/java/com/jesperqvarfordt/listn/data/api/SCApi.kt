package com.jesperqvarfordt.listn.data.api

import com.jesperqvarfordt.listn.data.model.SCChartResponse
import com.jesperqvarfordt.listn.data.model.SCTrack
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface SCApi {

    @GET("tracks")
    fun searchTracks(@Query("q") query: String?): Deferred<List<SCTrack>>

    @GET
    fun getTracksOnChart(@Url url: String): Observable<SCChartResponse>
}