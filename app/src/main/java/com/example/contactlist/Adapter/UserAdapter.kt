package com.example.contactlist.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlist.Data.User
import com.example.contactlist.R
import kotlinx.android.synthetic.main.list_c_item.view.*
import kotlinx.android.synthetic.main.list_h_item.view.*
import kotlinx.android.synthetic.main.list_c_item.view.first_name as first_name1
import kotlinx.android.synthetic.main.list_c_item.view.last_name as last_name1
import kotlinx.android.synthetic.main.list_c_item.view.number as number1

class UserAdapter(
    private val users: List<User>,
    private val onClick: (User) -> Unit,
    private val onButtonClick: (User) -> Unit
): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    init {
        notifyDataSetChanged()
    }

    companion object {
        const val TYPE_HORIZONTAL = 0
        const val TYPE_VERTICAL = 1
    }

    inner class UserViewHolder(var root: View) : RecyclerView.ViewHolder(root) {
        fun bind(user: User) {
            with(root) {
                first_name.text = user.firstName
                last_name.setImageURI(Uri.parse(user.lastName))
                number.text = user.number
                image.setOnClickListener { onButtonClick(user) }
            }
        }
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
                            R.layout.list_c_item,
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
            0 -> TYPE_VERTICAL
            else -> TYPE_VERTICAL
//            2 -> TYPE_VERTICAL // To Change
//            3 -> TYPE_VERTICAL // To Change
//            4 -> TYPE_VERTICAL // To Change
//            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }
}