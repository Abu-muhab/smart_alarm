package com.abumuhab.smartalarm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abumuhab.smartalarm.database.AlarmDao
import com.abumuhab.smartalarm.databinding.AlarmListItemBinding
import com.abumuhab.smartalarm.models.Alarm
import kotlinx.coroutines.runBlocking

class AlarmAdapter(private val alarmDao: AlarmDao) :
    ListAdapter<Alarm, AlarmAdapter.ViewHolder>(AlarmDiffCallback()) {
    class ViewHolder(private val binding: AlarmListItemBinding, private val alarmDao: AlarmDao) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.alarm = alarm
            binding.deleteIcon.setOnClickListener {
                runBlocking { alarmDao.delete(alarm) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlarmListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding, alarmDao)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm)
    }
}

class AlarmDiffCallback : DiffUtil.ItemCallback<Alarm>() {
    override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem.dbId == newItem.dbId
    }

    override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }
}