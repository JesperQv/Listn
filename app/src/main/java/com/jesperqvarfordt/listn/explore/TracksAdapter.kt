package com.jesperqvarfordt.listn.explore

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jesperqvarfordt.listn.R
import com.jesperqvarfordt.listn.common.extensions.msToTimeStamp
import com.jesperqvarfordt.listn.common.extensions.setVisible
import com.jesperqvarfordt.listn.domain.model.Track
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import kotlinx.android.synthetic.main.item_track.view.*

class TracksAdapter
constructor(private val trackClicked: (tracks: List<Track>, index: Int) -> Unit) :
        RecyclerView.Adapter<TracksAdapter.TrackViewHolder>() {

    private var tracks: MutableList<Track> = mutableListOf()
    private var playingId = -1

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks.toMutableList()
        notifyDataSetChanged()
    }

    fun updatePlayingTrackId(id: Int) {
        playingId = id
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.onBind(tracks[position], position, playingId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_track, parent, false))
    }

    inner class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(track: Track, pos: Int, playingId: Int) {
            if (track.id == playingId) {
                itemView.trackTitlePlaying.text = track.title
                itemView.trackTitlePlaying.setVisible(true)
                itemView.trackTitle.setVisible(false)
            } else {
                itemView.trackTitle.text = track.title
                itemView.trackTitlePlaying.setVisible(false)
                itemView.trackTitle.setVisible(true)
            }

            itemView.trackDuration.text = track.durationInMs.msToTimeStamp()
            itemView.setOnClickListener {
                trackClicked.invoke(tracks, pos)
            }
            val transformation = RoundedCornersTransformation(4, 0)
            Picasso.get().load(track.thumbnailUrl).transform(transformation).into(itemView.coverImage)
        }

    }
}