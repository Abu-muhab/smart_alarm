package com.abumuhab.smartalarm.fragments

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.abumuhab.smartalarm.R
import com.abumuhab.smartalarm.databinding.FragmentAddAlarmBinding
import com.abumuhab.smartalarm.databinding.WeekdayCardBinding
import com.abumuhab.smartalarm.models.AlarmSound
import com.abumuhab.smartalarm.util.hideSoftKeyboard
import com.abumuhab.smartalarm.viewmodels.AddAlarmViewModel
import com.abumuhab.smartalarm.viewmodels.AddAlarmViewModelFactory

class AddAlarmFragment : Fragment() {
    private lateinit var viewModel: AddAlarmViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val binding: FragmentAddAlarmBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_alarm, container, false
        )

        val application: Application = requireNotNull(this.activity).application
        val viewModelFactory = AddAlarmViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddAlarmViewModel::class.java)
        binding.viewModel = viewModel

        binding.addAlarmContainer.setOnClickListener {
            hideSoftKeyboard(requireContext(), it)
        }

        binding.cancelButton.setOnClickListener {
            it.findNavController()
                .navigate(AddAlarmFragmentDirections.actionAddAlarmFragmentToAlarmsFragment())
        }

        binding.selectAlarmButton.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), it)
            popupMenu.menuInflater.inflate(R.menu.alarm_types_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                viewModel.setAlarmSound(it.title.toString())
                true
            }
            popupMenu.show()
        }

        binding.previewButton.setOnClickListener {
            if (viewModel.mediaPlaying.value == true) {
                viewModel.stopAlarmSound()
            } else {
                viewModel.playSelectedAlarmSound()
            }
        }

        binding.vibrationSwitch.setOnCheckedChangeListener { p0, p1 ->
            viewModel.addVibration.value = p1
            if (p1) {
                val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            }
        }

        binding.volumeSlider.value = 0.5F
        binding.volumeSlider.addOnChangeListener { _, value, _ ->
            viewModel.changeAlarmVolume(value)
        }

        binding.hourPicker.minValue = 1
        binding.hourPicker.maxValue = 12
        binding.hourPicker.value = viewModel.hour.toInt()
        val hourRage = 1..12
        binding.hourPicker.displayedValues = (hourRage.map {
            it.toString().padStart(2, '0')
        }).toTypedArray()

        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59
        binding.minutePicker.value = viewModel.minute.toInt()
        val minRange = 0..59
        binding.minutePicker.displayedValues = (minRange.map {
            it.toString().padStart(2, '0')
        }).toTypedArray()


        val days = arrayListOf("Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun")
        days.forEach {
            var day = it
            val weekdayBinding =
                WeekdayCardBinding.inflate(layoutInflater, binding.weekdaysHolder, false)
            weekdayBinding.selected = false
            weekdayBinding.weekDay = it
            val layoutParams = LinearLayout.LayoutParams(
                0,
                (80 * requireContext().resources.displayMetrics.density).toInt(),
                1f
            )
            if (it != "Sun") {
                layoutParams.marginEnd = 10
            }

            weekdayBinding.button.setOnClickListener {
                weekdayBinding.selected = !weekdayBinding.selected!!
                if (weekdayBinding.selected == true) {
                    viewModel.addToSelectedDays(day)
                } else {
                    viewModel.removeFromSelectedDays(day)
                }
            }

            weekdayBinding.root.layoutParams = layoutParams
            binding.weekdaysHolder.addView(weekdayBinding.root)
        }

        binding.lifecycleOwner = this
        return binding.root
    }
}