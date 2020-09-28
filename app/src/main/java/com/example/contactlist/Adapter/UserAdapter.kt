package com.example.contactlist.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlist.Data.User
import com.example.contactlist.R
import com.example.contactlist.ViewHolder.UserViewHolder

class UserAdapter(
    private val users: List<User>,
    private val onClick: (User) -> Unit
): RecyclerView.Adapter<UserViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_HORIZONTAL = 0
        private const val TYPE_VERTICAL = 1
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) = holder.bind(users[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val holder: UserViewHolder?
        when (viewType) {
            TYPE_HORIZONTAL -> {
                holder = UserViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(
                            R.layout.list_h_item,
                            parent,
                            false
                        )
                )
            }
            TYPE_VERTICAL -> {
                holder = UserViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(
                            R.layout.list_c_item,
                            parent,
                            false
                        )
                )
            }
            else -> {
                holder = null
            }
        }
        holder!!.root.setOnClickListener {
            onClick(users[holder.adapterPosition])
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return when (position % 2) {
            1 -> TYPE_HORIZONTAL
            0 -> TYPE_VERTICAL // To Change
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }
}