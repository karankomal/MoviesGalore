package com.example.moviesgalore

import com.google.gson.annotations.SerializedName

class NowPlayingMovie {
    @JvmField
    @SerializedName("title")
    var title: String? = null

    @JvmField
    @SerializedName("overview")
    var overview: String? = null

    @JvmField
    @SerializedName("poster_path")
    var poster_path: String? = null

    @JvmField
    @SerializedName("vote_average")
    var vote_average: Float? = null

    @JvmField
    @SerializedName("backdrop_path")
    var backdrop_path: String? = null

    @JvmField
    @SerializedName("id")
    var id: Int? = null

    var imdb_id: String = "";

}