package org.studio.ghibli.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_people.view.*
import org.studio.ghibli.R
import org.studio.ghibli.model.PeopleUI

class PeoplePagedListAdapter(private val listener: PeopleListener) :
    PagedListAdapter<PeopleUI, PeoplePagedListAdapter.PeopleViewHolder>(DiffUtilCallBack()) {

    interface PeopleListener {
        fun onPeopleClicked(people: PeopleUI)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_people, parent, false)
        return PeopleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(listener, it)
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)!!.id.hashCode().toLong()
    }

    class PeopleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container: View = itemView.item_people_people
        private val peopleName: TextView = itemView.people_name
        private val peopleGender: TextView = itemView.people_gender
        private val peopleSpecies: TextView = itemView.people_species

        fun bind(listener: PeopleListener,  people: PeopleUI) {
            with(people) {
                peopleName.text = name
                peopleGender.text = gender
                peopleSpecies.text = species

                container.setOnClickListener {
                    listener.onPeopleClicked(people)
                }
            }
        }
    }
}

class DiffUtilCallBack : DiffUtil.ItemCallback<PeopleUI>() {
    override fun areItemsTheSame(oldItem: PeopleUI, newItem: PeopleUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PeopleUI, newItem: PeopleUI): Boolean {
        return oldItem.id == newItem.id
                && oldItem.name == newItem.name
                && oldItem.species == newItem.species
    }

}