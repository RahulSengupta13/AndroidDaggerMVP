package com.photo.doc.ui.activities.result

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.photo.doc.R
import com.google.api.services.vision.v1.model.EntityAnnotation
import kotlinx.android.synthetic.main.result_item_layout.view.*
import java.util.*

class ResultAdapter constructor(private val context: Context, private val dataSet: MutableList<EntityAnnotation>) : androidx.recyclerview.widget.RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ResultAdapter.ResultViewHolder {
        val layout = LayoutInflater.from(p0.context).inflate(R.layout.result_item_layout, p0, false)
        return ResultViewHolder(layout)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: ResultAdapter.ResultViewHolder, position: Int) {
        val entity = dataSet[position]
        holder.view.resultDataTextView.text = String.format(Locale.US, "%.3f: %s", entity.score, entity.description)
        holder.view.viewWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_WEB_SEARCH)
            intent.putExtra(SearchManager.QUERY, entity.description)
            context.startActivity(intent)
        }
    }

    class ResultViewHolder(val view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)
}