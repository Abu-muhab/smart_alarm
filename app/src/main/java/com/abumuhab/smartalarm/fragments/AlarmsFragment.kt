package com.abumuhab.smartalarm.fragments

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.abumuhab.smartalarm.R
import com.abumuhab.smartalarm.adapters.AlarmAdapter
import com.abumuhab.smartalarm.database.AlarmDatabase
import com.abumuhab.smartalarm.databinding.FragmentAlarmsBinding
import com.abumuhab.smartalarm.viewmodels.AlarmsViewModel
import com.abumuhab.smartalarm.viewmodels.AlarmsViewModelFactory


class AlarmsFragment : Fragment() {
    private lateinit var viewModel: AlarmsViewModel

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

        val application: Application = requireNotNull(this.activity).application
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao
        val viewModelFactory = AlarmsViewModelFactory(application, alarmDao)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AlarmsViewModel::class.java)
        binding.viewModel = viewModel

        val adapter = AlarmAdapter(alarmDao)

        binding.alarmsRecyclerView.adapter = adapter

        viewModel.alarms.observe(viewLifecycleOwner) {
            it?.apply {
                adapter.submitList(this)
            }
        }

        binding.addAlarmButton.setOnClickListener {
            it.findNavController()
                .navigate(AlarmsFragmentDirections.actionAlarmsFragmentToAddAlarmFragment())
        }

        binding.lifecycleOwner = this
        return binding.root
    }
}