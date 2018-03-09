package com.jesperqvarfordt.listn.data.repository

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.repository.ChartRepository
import io.reactivex.Observable

class LocalChartRepository : ChartRepository {

    override fun getCharts(): Observable<List<Chart>> {
        val edm = Chart("Dance & EDM",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Adanceedm&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_edm")
        val hipHop = Chart("Hip-Hop & Rap",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Ahiphoprap&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_hiphop")
        val deepHouse = Chart("Deep House",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Adeephouse&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_deephouse")
        val indie = Chart("Indie",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Aindie&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_indie")
        val rock = Chart("Rock",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Arock&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_rock")
        val drumBass = Chart("Drum & Bass",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Adrumbass&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_drumbass")
        val pop = Chart("Pop",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Apop&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_pop")
        val rbSoul = Chart("R&B & Soul",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Arbsoul&offset=0&limit=20",
                "android.resource://com.jesperqvarfordt.listn/drawable/rsz_rbsoul")
        return Observable.fromCallable({
            listOf(edm, hipHop, indie, pop, rock, deepHouse, rbSoul, drumBass)
        })
    }
}