package com.iamyajat.messtracker.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iamyajat.messtracker.adapter.MealAdapter
import com.iamyajat.messtracker.databinding.FragmentHistoryBinding
import com.iamyajat.messtracker.model.Meal
import com.iamyajat.messtracker.util.DateFunctions
import com.iamyajat.messtracker.util.MealListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private var firstTime = true
    private lateinit var mealAdapter: MealAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val historyViewModel =
            ViewModelProvider(this)[HistoryViewModel::class.java]

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        historyViewModel.initVM()

        historyViewModel.monthExp.observe(viewLifecycleOwner) {
            binding.monthExpenseAmount.text = it.toString()
        }

        historyViewModel.meals.observe(viewLifecycleOwner) { todayMeals ->
            if (todayMeals.isEmpty()) {
                binding.noMealsText.visibility = View.VISIBLE
            } else {
                binding.noMealsText.visibility = View.INVISIBLE
            }
            if (firstTime) {
                mealAdapter = MealAdapter(todayMeals, true, object : MealListener {
                    override fun onDelete(meal: Meal) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(
                                "Delete ${meal.mealName} added on ${
                                    DateFunctions.formatDateTime(
                                        meal.addedOn,
                                        true
                                    )
                                }?"
                            )
                            .setMessage("You cannot undo this operation!")
                            .setNegativeButton("Cancel") { dialog, which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("Delete") { dialog, which ->
                                // Respond to positive button press
                                meal.id?.let { historyViewModel.deleteMeal(it) }
                                historyViewModel.initVM()
                            }
                            .show()
                    }
                })
                Log.d("MEALS_FRAG", todayMeals.toString())

                binding.mealList.apply {
                    layoutManager = LinearLayoutManager(context)
//                    setHasFixedSize(true)
                    adapter = mealAdapter
                }
                firstTime = false
            } else {
                Log.d("MEALS_FRAG_C", todayMeals.toString())
                mealAdapter.dataSetChange(todayMeals)
                mealAdapter.notifyDataSetChanged()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}