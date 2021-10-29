package com.abumuhab.smartalarm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.abumuhab.smartalarm.databinding.FragmentAddAlarmBinding
import com.abumuhab.smartalarm.databinding.WeekdayCardBinding
import com.abumuhab.smartalarm.util.hideSoftKeyboard

class AddAlarmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val binding = FragmentAddAlarmBinding.inflate(inflater, container, false)

        binding.addAlarmContainer.setOnClickListener {
            hideSoftKeyboard(requireContext(), it)
        }

        binding.cancelButton.setOnClickListener {
            it.findNavController()
                .navigate(AddAlarmFragmentDirections.actionAddAlarmFragmentToAlarmsFragment())
        }


        val days = arrayListOf("Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun")
        days.forEach {
            val weekdayBinding =
                WeekdayCardBinding.inflate(layoutInflater, binding.weekdaysHolder, false)
            weekdayBinding.selected = false
            weekdayBinding.weekDay = it
            val layoutParams = LinearLayout.LayoutParams(0, 130, 1f)
            if (it != "Sun") {
                layoutParams.marginEnd = 10
            }

            weekdayBinding.button.setOnClickListener {
                weekdayBinding.selected = !weekdayBinding.selected!!
            }

            weekdayBinding.root.layoutParams = layoutParams
            binding.weekdaysHolder.addView(weekdayBinding.root)
        }

        binding.lifecycleOwner = this
        return binding.root
    }
}