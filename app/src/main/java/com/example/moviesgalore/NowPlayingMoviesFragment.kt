package com.example.moviesgalore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers

val objectURL = "https://api.themoviedb.org/3/movie/now_playing?language=en-US&page=1"

private const val ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1YmZmYzI2NzcwY2U3M2FmNGMxYjE0ZjUyMmIzM2Y5YSIsInN1YiI6IjY1MTczNTMxMDcyMTY2MDEzOWM0ZWIyNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.6ql4Eycyo-MOg-bBw3SJr9EAarufKKDGu9CA0RBi-40"

class NowPlayingMoviesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_now_playing_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.movies) as RecyclerView
        val context = view.context

        recyclerView.layoutManager = LinearLayoutManager(context)
        updateAdapter(progressBar, recyclerView, view.findViewById(R.id.now_playing))

        return view
    }

    private fun updateAdapter(progressBar: ContentLoadingProgressBar, recyclerView: RecyclerView, nowPlayingTV: TextView) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        val headers = RequestHeaders()

        headers["accept"] = "application/json"
        headers["Authorization"] = "Bearer $ACCESS_TOKEN"

        client[
            objectURL,
            headers,
            params,
            object : JsonHttpResponseHandler()

            {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    progressBar.hide()
                    nowPlayingTV.visibility = View.VISIBLE
                    val moviesRawJSON : String = json.jsonObject.get("results").toString()

                    val gson = Gson()
                    val movieType = object : TypeToken<List<NowPlayingMovie>>() {}.type
                    val models : List<NowPlayingMovie> = gson.fromJson(moviesRawJSON, movieType)

                    for (m in models) {
                        getIMDBid(m.id!!, m)
                    }

                    recyclerView.adapter = NowPlayingMovieAdapter(models)
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    progressBar.hide()
                    nowPlayingTV.visibility = View.VISIBLE

                    t?.message?.let {
                        Log.e("NowPlayingMoviesFragment", errorResponse)
                    }
                }
            }]
    }

    private fun getIMDBid(id: Int, m: NowPlayingMovie) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        val headers = RequestHeaders()
        val imdbIDURL = "https://api.themoviedb.org/3/movie/$id/external_ids"

        headers["accept"] = "application/json"
        headers["Authorization"] = "Bearer $ACCESS_TOKEN"

        client[
            imdbIDURL,
            headers,
            params,
            object : JsonHttpResponseHandler()
            {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Headers,
                    json: JsonHttpResponseHandler.JSON
                ) {
                    m.imdb_id = json.jsonObject.get("imdb_id").toString()
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Headers?,
                    errorResponse: String,
                    t: Throwable?
                ) {
                    t?.message?.let {
                        Log.e("NowPlayingMoviesFragment", errorResponse)
                    }
                    m.imdb_id = "ERROR"
                }
            }]
    }
}