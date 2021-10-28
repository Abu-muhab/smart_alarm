package com.abumuhab.smartalarm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.abumuhab.smartalarm.R
import com.abumuhab.smartalarm.databinding.FragmentAlarmsBinding


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
        return binding.root
    }
}