package com.example.moviesgalore

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesgalore.R.id

class NowPlayingMovieAdapter(private val items: List<NowPlayingMovie>)
    : RecyclerView.Adapter<NowPlayingMovieAdapter.ViewHolder>()
    {
    private lateinit var context: Context

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitleTV: TextView = itemView.findViewById(id.movie_title) as TextView
        //val movieRatingTV = itemView.findViewById(id.movie_rating) as TextView
        val movieDescTV: TextView = itemView.findViewById(id.movie_description) as TextView
        val moviePosterIV: ImageView = itemView.findViewById(id.movie_poster) as ImageView
        val moviePosterLandIV: ImageView = itemView.findViewById(id.movie_poster_land) as ImageView
        val ratingStars: RatingBar = itemView.findViewById(R.id.ratingStars) as RatingBar

        override fun toString(): String {
            return movieTitleTV.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val contactView = inflater.inflate(R.layout.now_playing_movie, parent, false)
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]
        holder.movieTitleTV.text = movie.title
        //holder.movieRatingTV.text = (movie.vote_average?.times(10)).toString() + "%"
        holder.movieDescTV.text = movie.overview
        val moviePosterUrl = "https://image.tmdb.org/t/p/w500" + movie.poster_path
        val moviePosterLandUrl = "https://image.tmdb.org/t/p/w500" + movie.backdrop_path

        Glide.with(context)
            .load(moviePosterUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.not_found)
            .centerInside()
            .into(holder.moviePosterIV)

        Glide.with(context)
            .load(moviePosterLandUrl)
            .placeholder(R.drawable.loading)
            .error(R.drawable.not_found)
            .centerInside()
            .into(holder.moviePosterLandIV)

        holder.ratingStars.rating = movie.vote_average?.div(2)!!

        holder.itemView.isLongClickable = true
        holder.itemView.setOnLongClickListener {
            try {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.imdb.com/title/" + movie.imdb_id))
                context.startActivity(browserIntent)
                Toast.makeText(context, "IMDB page for " + holder.movieTitleTV.text.toString() + " is opening!", Toast.LENGTH_SHORT).show()
            }
            catch (e: ActivityNotFoundException) {
                Toast.makeText(it.context, "IMDB page for " + holder.movieTitleTV.text + " could not open.", Toast.LENGTH_LONG).show()
            }
            true
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}