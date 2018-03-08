package com.jesperqvarfordt.listn.domain.repository

import com.jesperqvarfordt.listn.domain.model.Chart
import io.reactivex.Observable

interface ChartRepository {

    fun getCharts(): Observable<List<Chart>>

}