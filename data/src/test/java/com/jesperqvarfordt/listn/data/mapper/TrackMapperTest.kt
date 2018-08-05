package com.jesperqvarfordt.listn.data.mapper

import com.jesperqvarfordt.listn.data.model.SCTrack
import com.jesperqvarfordt.listn.data.model.SCUser
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class TrackMapperTest {

    private lateinit var mapper: TrackMapper

    private val clientId = "clientId"
    private val title = "title"
    private val streamUrl = "streamUrl"
    private val coverUrl = "largeCoverUrl"
    private val durationInMs = 1000L
    private val name = "name"
    private val user = SCUser(name)
    private val scTrack = SCTrack(1, title, streamUrl, coverUrl, durationInMs, user)

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mapper = TrackMapper(clientId)
    }

    @Test
    @Throws(Exception::class)
    fun `mapper works for title`() {
        val track = mapper.map(scTrack)

        assertEquals(track.title, title)
    }

    @Test
    @Throws(Exception::class)
    fun `mapper works for stream url`() {
        val track = mapper.map(scTrack)

        assertEquals(track.streamUrl, streamUrl + "/stream" + "?client_id=" + clientId)
    }

    @Test
    @Throws(Exception::class)
    fun `mapper works for thumbnail`() {
        val track = mapper.map(scTrack)

        assertEquals(track.thumbnailUrl, coverUrl)
    }

    @Test
    @Throws(Exception::class)
    fun `mapper works for large image url`() {
        val track = mapper.map(scTrack)

        assertEquals(track.largeImageUrl, coverUrl.replace("large", "t500x500"))
    }

    @Test
    @Throws(Exception::class)
    fun `mapper works for duration`() {
        val track = mapper.map(scTrack)

        assertEquals(track.durationInMs, durationInMs)
    }

    @Test
    @Throws(Exception::class)
    fun `mapper works for artist name`() {
        val track = mapper.map(scTrack)

        assertEquals(track.artist, user.name)
    }

}