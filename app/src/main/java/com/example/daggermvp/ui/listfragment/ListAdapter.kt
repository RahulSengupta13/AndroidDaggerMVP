package com.example.daggermvp.ui.listfragment

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.daggermvp.models.Post
import com.example.daggermvp.R

class ListAdapter(private val context: Context, private val list: MutableList<Post>,
                  fragment: Fragment): RecyclerView.Adapter<ListAdapter.ListViewHolder>(){

    private val listener: ListAdapter.OnItemClickListener
    init {
        this.listener = fragment as ListAdapter.OnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ListAdapter.ListViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val post = list[position]
        holder.title.text = post.title
        holder.body.text = post.body
        holder.layout!!.setOnClickListener {
            listener.itemDetail(post.id.toString())
        }
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var layout = itemView.findViewById<ConstraintLayout>(R.id.item_layout)
        val title = itemView.findViewById<TextView>(R.id.item_title)
        val body = itemView.findViewById<TextView>(R.id.item_body)

        fun bind(item: Post) {
            // title = item.post
            // body etc.
        }
    }

    interface OnItemClickListener {
        fun itemRemoveClick(post: Post)
        fun itemDetail(postId : String)
    }
}