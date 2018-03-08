package com.jesperqvarfordt.listn.data.model

import com.google.gson.annotations.SerializedName

data class SCTrack(@SerializedName("title") val title: String?,
                   @SerializedName("uri") val streamUrl: String?,
                   @SerializedName("artwork_url") val coverUrl: String?,
                   @SerializedName("duration") val durationInMs: Long,
                   @SerializedName("user") val artist: SCUser?)