package com.iamyajat.messtracker.ui.meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.databinding.FragmentMealBinding
import com.iamyajat.messtracker.util.Constants.MAX_CREDITS
import com.iamyajat.messtracker.util.DateFunctions.getMealType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MealFragment : Fragment() {

    private var _binding: FragmentMealBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val mealViewModel =
            ViewModelProvider(this)[MealViewModel::class.java]

        _binding = FragmentMealBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close_meal -> {
                    requireActivity().onBackPressed()
                    true
                }
                else -> false
            }
        }

        mealViewModel.apply {
            initVM()
            todayBal.observe(viewLifecycleOwner) {
                binding.todayExpenseAmount.text = "$it"
            }
            monthExp.observe(viewLifecycleOwner) {
                binding.monthExpenseAmount.text = "${
                    MAX_CREDITS - it
                }"
            }
        }


        binding.amountText.apply {
            doOnTextChanged { text, start, before, count ->
                binding.amount.error = ""

                val amt = if (text.toString().trim() == "") 0 else text.toString().toLong()
                val mAmt =
                    if (mealViewModel.monthExp.value == null) 0 else mealViewModel.monthExp.value
                val tAmt =
                    if (mealViewModel.todayBal.value == null) 0 else mealViewModel.todayBal.value

                mealViewModel.changeAmount(amt)
                binding.monthExpenseAmount.text = "${
                    MAX_CREDITS - amt - mAmt!!
                }"

                binding.todayExpenseAmount.text = "${
                    tAmt!! - amt
                }"
            }
        }

        binding.addMealButton.setOnClickListener {
            var amt = if (binding.amountText.text.toString()
                    .trim() == ""
            ) 0 else binding.amountText.text.toString().toLong()

            if (amt > 0) {
                mealViewModel.addValue(binding.mealNameText.text.toString())
                requireActivity().onBackPressed()
            } else {
                binding.amount.error = "enter an amount"
            }
        }

        return root

    }

    override fun onStart() {
        super.onStart()
        binding.mealNameText.setText(getMealType())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}