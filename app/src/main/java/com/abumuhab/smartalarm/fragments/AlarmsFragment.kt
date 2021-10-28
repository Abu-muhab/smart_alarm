package com.abumuhab.smartalarm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.abumuhab.smartalarm.R
import com.abumuhab.smartalarm.adapters.AlarmAdapter
import com.abumuhab.smartalarm.databinding.FragmentAlarmsBinding
import com.abumuhab.smartalarm.models.Alarm


class AlarmsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentAlarmsBinding>(
            layoutInflater,
            R.layout.fragment_alarms,
            container,
            false
        )
        (activity as AppCompatActivity).supportActionBar?.hide()

        val adapter = AlarmAdapter()

        binding.alarmsRecyclerView.adapter = adapter

        adapter.submitList(
            arrayListOf(
                Alarm("temp", "2"),
                Alarm("temp", "3"),
                Alarm("temp", "4"),
                Alarm("temp", "5"),
                Alarm("temp", "6"),
                Alarm("temp", "7"),
                Alarm("temp", "8"),
                Alarm("temp", "9"),
                Alarm("temp", "10"),
            )
        )

        return binding.root
    }
}