package com.abumuhab.smartalarm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abumuhab.smartalarm.databinding.AlarmListItemBinding
import com.abumuhab.smartalarm.models.Alarm

class AlarmAdapter: ListAdapter<Alarm, AlarmAdapter.ViewHolder>(AlarmDiffCallback()) {
    class ViewHolder(private val binding: AlarmListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(alarm: Alarm){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = AlarmListItemBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm)
    }
}

class AlarmDiffCallback: DiffUtil.ItemCallback<Alarm>(){
    override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
        return oldItem == newItem
    }
}