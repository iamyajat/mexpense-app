package com.iamyajat.messtracker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.model.Day
import com.iamyajat.messtracker.util.DateFunctions.getMonthSize
import com.iamyajat.messtracker.util.DayListener

class DayAdapter(
    var dayListener: DayListener
) : RecyclerView.Adapter<DayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val day: TextView = view.findViewById(R.id.date)
        val backgroundCard: MaterialCardView = view.findViewById(R.id.background_card)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_day, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentDay: Day = dayListener.getDayData(position + 1)
        var enableSwitch = currentDay.enabled
        switchButton(enableSwitch, viewHolder)
        viewHolder.apply {
            day.text = "${position + 1}"
            itemView.setOnClickListener {
                if (!enableSwitch) {
                    dayListener.onEnable(position + 1)
                } else {
                    dayListener.onDisable(position + 1)
                }
                enableSwitch = !enableSwitch
                switchButton(enableSwitch, viewHolder)
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = getMonthSize()


    private fun switchButton(enableSwitch: Boolean, viewHolder: ViewHolder) {
        if (!enableSwitch) {
            viewHolder.backgroundCard.setCardBackgroundColor(Color.TRANSPARENT)
        } else {
            viewHolder.backgroundCard.setCardBackgroundColor(
                ContextCompat.getColor(viewHolder.itemView.context, R.color.brand)
            )
        }
    }
}