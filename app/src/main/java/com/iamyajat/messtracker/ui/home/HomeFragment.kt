package com.iamyajat.messtracker.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.adapter.MealAdapter
import com.iamyajat.messtracker.databinding.FragmentHomeBinding
import com.iamyajat.messtracker.util.Constants.MAX_CREDITS
import com.iamyajat.messtracker.util.DateFunctions.getGreeting
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.addMealButton.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_navigation_home_to_navigation_meal)
        }

        binding.maxCreditsAmt.text = " / $MAX_CREDITS"

        binding.topAppBar.title = getGreeting()

        homeViewModel.todayExp.observe(viewLifecycleOwner) {
            binding.todayExpenseAmount.text = it.toString()
        }

        homeViewModel.todayBal.observe(viewLifecycleOwner) {
            binding.todayBalance.text = it.toString()
        }

        firstTime = true

        homeViewModel.initVM()

        homeViewModel.meals.observe(viewLifecycleOwner) { todayMeals ->
            if (firstTime) {
                mealAdapter = MealAdapter(todayMeals, false)
                Log.d("MEALS_FRAG_HOME", todayMeals.toString())

                binding.mealList.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = mealAdapter
                }
                firstTime = false
            } else {
                Log.d("MEALS_FRAG_C_HOME", todayMeals.toString())
                mealAdapter.dataSetChange(todayMeals)
                mealAdapter.notifyDataSetChanged()
            }
        }

        homeViewModel.monthExp.observe(viewLifecycleOwner) { monthExp ->
            val rem = MAX_CREDITS - monthExp
            binding.monthBalance.text = "$rem"
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}