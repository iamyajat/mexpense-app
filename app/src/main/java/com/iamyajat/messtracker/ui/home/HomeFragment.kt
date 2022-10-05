package com.iamyajat.messtracker.ui.home

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.adapter.MealAdapter
import com.iamyajat.messtracker.databinding.FragmentHomeBinding
import com.iamyajat.messtracker.model.Meal
import com.iamyajat.messtracker.util.Constants.MAX_CREDITS
import com.iamyajat.messtracker.util.DateFunctions
import com.iamyajat.messtracker.util.DateFunctions.formatDate
import com.iamyajat.messtracker.util.DateFunctions.getGreeting
import com.iamyajat.messtracker.util.DateFunctions.getTodayStart
import com.iamyajat.messtracker.util.MealListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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

        binding.offDaysButton.setOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_navigation_home_to_navigation_settings)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.open_info -> {

                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(
                            "Info"
                        )
                        .setMessage(getString(R.string.info))
                        .setPositiveButton("Dismiss") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                    true
                }
                else -> false
            }
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
            if (todayMeals.isEmpty()) {
                binding.noMealsText.visibility = View.VISIBLE
            } else {
                binding.noMealsText.visibility = View.INVISIBLE
            }
            if (firstTime) {
                mealAdapter = MealAdapter(todayMeals, false, object : MealListener {
                    override fun onDelete(meal: Meal) {
                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle(
                                "Delete ${meal.mealName} added at ${
                                    DateFunctions.formatDateTime(
                                        meal.addedOn,
                                        false
                                    )
                                }?"
                            )
                            .setMessage("You cannot undo this operation!")
                            .setNegativeButton("Cancel") { dialog, which ->
                                dialog.dismiss()
                            }
                            .setPositiveButton("Delete") { dialog, which ->
                                // Respond to positive button press
                                meal.id?.let { homeViewModel.deleteMeal(it) }
                                homeViewModel.initVM()
                            }
                            .show()
                    }
                })
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

        binding.syncCreditsButton.setOnClickListener {
            val creditsEditText = EditText(context)
            creditsEditText.apply {
                setText((homeViewModel.monthExp.value!! - homeViewModel.todayExp.value!!).toString())
                textSize = 24f
                inputType = InputType.TYPE_CLASS_NUMBER
            }

            val frameLayout = FrameLayout(requireContext())
            frameLayout.apply {
                addView(creditsEditText)
                setPadding(56, 0, 56, 0)
            }

            val yesterday = Date(getTodayStart().time - 1000)
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(
                    "Sync Credits"
                )
                .setMessage("Enter credits spent till yesterday (${formatDate(yesterday)})")
                .setView(frameLayout)
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("Confirm") { dialog, which ->
                    // Respond to positive button press
                    if (creditsEditText.text.toString().trim() != "") {
                        val extra =
                            creditsEditText.text.toString()
                                .toLong() - homeViewModel.monthExp.value!! + homeViewModel.todayExp.value!!
                        if (extra != 0L) {
                            homeViewModel.addValue(
                                if (extra > 0L) "Untracked Credits" else "Extra Credits",
                                extra,
                                yesterday
                            )
                        }
                    }
                    homeViewModel.initVM()
                }
                .show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}