package com.abumuhab.smartalarm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.abumuhab.smartalarm.R
import com.abumuhab.smartalarm.databinding.FragmentAddAlarmBinding

class AddAlarmFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val binding = FragmentAddAlarmBinding.inflate(inflater,container,false)
        return binding.root
    }
}