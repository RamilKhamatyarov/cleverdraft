package ru.rkhamatyarov.cleverdraft.view

import android.widget.ImageButton
import android.widget.TextView
import android.widget.RelativeLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import ru.rkhamatyarov.cleverdraft.R


/**
 * Created by Asus on 02.10.2017.
 */
class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var container: RelativeLayout
    lateinit var text: TextView
    lateinit var date: TextView
    lateinit var btnDelete: ImageButton

    init {

        setupViews(itemView)
    }

    private fun setupViews(view: View) {
        container = view.findViewById(R.id.holder_container)
        text = view.findViewById(R.id.note_text)
        date = view.findViewById(R.id.note_date)
        btnDelete = view.findViewById(R.id.btn_delete)
    }


}