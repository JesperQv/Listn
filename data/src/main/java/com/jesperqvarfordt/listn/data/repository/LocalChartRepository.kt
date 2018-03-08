package com.jesperqvarfordt.listn.data.repository

import com.jesperqvarfordt.listn.domain.model.Chart
import com.jesperqvarfordt.listn.domain.repository.ChartRepository
import io.reactivex.Observable

class LocalChartRepository : ChartRepository {

    override fun getCharts(): Observable<List<Chart>> {
        val edm = Chart("Dance & EDM",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Adanceedm&offset=0&limit=20",
                "https://i.imgur.com/KLutStR.jpg")
        val hipHop = Chart("Hip-Hop & Rap",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Ahiphoprap&offset=0&limit=20",
                "https://i.imgur.com/LhXcxoz.jpg")
        val deepHouse = Chart("Deep House",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Adeephouse&offset=0&limit=20",
                "https://i.imgur.com/dWiHM5r.jpg")
        val indie = Chart("Indie",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Aindie&offset=0&limit=20",
                "https://i.imgur.com/mQix1LD.jpg")
        val rock = Chart("Rock",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Arock&offset=0&limit=20",
                "https://i.imgur.com/7SNecJS.jpg")
        val drumBass = Chart("Drum & Bass",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Adrumbass&offset=0&limit=20",
                "https://i.imgur.com/JCI3Y3D.jpg")
        val pop = Chart("Pop",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Apop&offset=0&limit=20",
                "https://i.imgur.com/JNumZSR.jpg")
        val rbSoul = Chart("R&B & Soul",
                "https://api-v2.soundcloud.com/charts?kind=top&genre=soundcloud%3Agenres%3Arbsoul&offset=0&limit=20",
                "https://i.imgur.com/4F5qghC.jpg")
        return Observable.fromCallable({
            listOf(edm, hipHop, indie, pop, rock, deepHouse, rbSoul, drumBass)
        })
    }
}