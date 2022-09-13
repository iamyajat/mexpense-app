package com.iamyajat.messtracker.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamyajat.messtracker.adapter.MealAdapter
import com.iamyajat.messtracker.databinding.FragmentHistoryBinding
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
            if(firstTime) {
                mealAdapter = MealAdapter(todayMeals, true, object : MealListener {
                    override fun onDelete(mealId: Long) {
                        historyViewModel.deleteMeal(mealId)
                        historyViewModel.initVM()
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