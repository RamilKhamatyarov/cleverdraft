package ru.rkhamatyarov.cleverdraft.view.utilities

import android.support.v7.widget.RecyclerView
import android.view.View
import ru.rkhamatyarov.cleverdraft.R.id.btn_delete
import android.widget.ImageButton
import ru.rkhamatyarov.cleverdraft.R.id.note_date
import android.widget.TextView
import ru.rkhamatyarov.cleverdraft.R.id.note_text
import ru.rkhamatyarov.cleverdraft.R.id.holder_container
import android.widget.RelativeLayout
import ru.rkhamatyarov.cleverdraft.R


/**
 * Created by Asus on 03.09.2017.
 */
 class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
      lateinit var container: RelativeLayout
      lateinit var content: TextView
      lateinit var date:TextView
      lateinit var butonDelete: ImageButton

      init {
          super.itemView
          setupViews(itemView)
      }

      private fun setupViews(view: View) {
           container = view.findViewById<RelativeLayout>(R.id.holder_container) as RelativeLayout
           content = view.findViewById<TextView>(R.id.note_text) as TextView
           date = view.findViewById<TextView>(R.id.note_date) as TextView
           butonDelete = view.findViewById<ImageButton>(R.id.btn_delete) as ImageButton
      }
}
