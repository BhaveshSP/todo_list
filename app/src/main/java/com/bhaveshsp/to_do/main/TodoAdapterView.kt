package com.bhaveshsp.to_do.main

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bhaveshsp.to_do.DoItem
import com.bhaveshsp.to_do.R
import com.bhaveshsp.to_do.utility.*

/**
 * @author Bhavesh Purohit
 * Created on 06/07/2020
 */
class TodoAdapterView(private var doItemList:ArrayList<DoItem>,
                      private val context: Context) : RecyclerView.Adapter<TodoAdapterView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemView = layoutInflater.inflate(R.layout.todo_list_item_layout,parent,false)
        return ViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return doItemList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doItem = doItemList[position]
        val itemTitleColor:Int
        val itemBgColor :Int
        val theme = context.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE).getString(
            THEME_PREF, LIGHT_THEME)
        when(theme){
            LIGHT_THEME -> {
                itemBgColor = ContextCompat.getColor(context,R.color.colorBackground)
                itemTitleColor = ContextCompat.getColor(context,R.color.colorBackgroundDark)
            }
            else -> {
                itemBgColor = ContextCompat.getColor(context,R.color.colorBackgroundDark)
                itemTitleColor = ContextCompat.getColor(context,R.color.colorBackground)
            }

        }

        holder.linearLayout.setBackgroundColor(itemBgColor)
        val title = doItem.getTitle()
        holder.toDoTitleText.text = title
        holder.toDoTitleText.setTextColor(itemTitleColor)
        // Color Generator
        val colorGenerator = ColorGenerator.MATERIAL
        val color = colorGenerator.randomColor
        // Create TextDrawable
        val textDrawable = TextDrawable.builder()
                .beginConfig().textColor(Color.WHITE)
                .toUpperCase()
                .useFont(Typeface.DEFAULT)
                .endConfig()
                .buildRound(title.substring(0,1),color)
        holder.toDoImageView.setImageDrawable(textDrawable)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var linearLayout:LinearLayout = itemView.findViewById(R.id.todoListItemLayout)
        var toDoTitleText: TextView = itemView.findViewById(R.id.todoListItemTitle)
        var toDoImageView: ImageView = itemView.findViewById(R.id.todoListItemImage)
    }

}