package com.iamyajat.messtracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.model.Meal
import com.iamyajat.messtracker.util.DateFunctions.formatDate
import com.iamyajat.messtracker.util.MealListener

class MealAdapter(
    private var meals: List<Meal>,
    var dateAndTime: Boolean = true,
    var mealListener: MealListener
) : RecyclerView.Adapter<MealAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealName: TextView = view.findViewById(R.id.meal_name)
        val mealTime: TextView = view.findViewById(R.id.meal_time)
        val mealCredits: TextView = view.findViewById(R.id.meal_credit)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_meal, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentMeal = meals[position]
        viewHolder.apply {
            mealName.text = currentMeal.mealName
            mealTime.text = formatDate(currentMeal.addedOn, dateAndTime)
            mealCredits.text = currentMeal.amount.toString()
            itemView.setOnLongClickListener {
                mealListener.onDelete(currentMeal.id!!)
                true
            }
        }
    }

    fun dataSetChange(newMeals: List<Meal>) {
        meals = newMeals
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = meals.size
}