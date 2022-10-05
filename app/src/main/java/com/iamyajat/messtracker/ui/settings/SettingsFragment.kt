package com.iamyajat.messtracker.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.iamyajat.messtracker.R
import com.iamyajat.messtracker.adapter.DayAdapter
import com.iamyajat.messtracker.databinding.FragmentSettingsBinding
import com.iamyajat.messtracker.model.Day
import com.iamyajat.messtracker.util.DateFunctions.getCurrentMonth
import com.iamyajat.messtracker.util.DateFunctions.getDateId
import com.iamyajat.messtracker.util.DayListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    lateinit var dayAdapter: DayAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        dayAdapter = DayAdapter(object : DayListener {
            override fun onEnable(date: Int) {
                settingsViewModel.addValue(Day(getDateId(date), true))
            }

            override fun onDisable(date: Int) {
                settingsViewModel.updateValue(Day(getDateId(date), false))
            }

            override fun getDayData(date: Int): Day {
                settingsViewModel.getDayInfo(getDateId(date))
                Log.d("DAY_DATA", settingsViewModel.days.value.toString())
                if (settingsViewModel.days.value == null || settingsViewModel.days.value?.isEmpty() == true) {
                    return Day(getDateId(date))
                }
                return settingsViewModel.days.value?.get(0) ?: Day(getDateId(date))
            }

        })
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close_meal -> {
                    requireActivity().onBackPressed()
                    true
                }
                else -> false
            }
        }

        binding.monthTextView.text = getCurrentMonth()

        binding.calenderList.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = dayAdapter
        }


        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}