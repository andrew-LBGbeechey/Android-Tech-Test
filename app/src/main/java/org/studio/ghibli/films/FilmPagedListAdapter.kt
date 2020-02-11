package org.studio.ghibli.films

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_films.view.*
import org.studio.ghibli.R
import org.studio.ghibli.model.FilmUI

class FilmPagedListAdapter(private val listener: FilmsListener) :
    PagedListAdapter<FilmUI, FilmPagedListAdapter.FilmViewHolder>(DiffUtilCallBack()) {

    interface FilmsListener {
        fun onFilmClicked(film: FilmUI)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_films, parent, false)
        return FilmViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilmViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(listener, it)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id.hashCode().toLong()
    }

    class FilmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container: View = itemView.item_films_film
        private val filmTitle: TextView = itemView.film_title
        private val filmDirector: TextView = itemView.film_director
        private val filmDescription: TextView = itemView.film_description

        fun bind(listener: FilmsListener,  film: FilmUI) {
            with(film) {
                filmTitle.text = title
                filmDirector.text = director
                filmDescription.text = description

                container.setOnClickListener {
                    listener.onFilmClicked(film)
                }
            }
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<FilmUI>() {
    override fun areItemsTheSame(oldItem: FilmUI, newItem: FilmUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FilmUI, newItem: FilmUI): Boolean {
        return oldItem.id == newItem.id
                && oldItem.title == newItem.title
                && oldItem.description == newItem.description
    }

}