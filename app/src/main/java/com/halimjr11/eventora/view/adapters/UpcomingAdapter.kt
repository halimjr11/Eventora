package com.halimjr11.eventora.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.halimjr11.eventora.databinding.ItemUpcomingEventBinding
import com.halimjr11.eventora.domain.model.EventDomain

class UpcomingAdapter : ListAdapter<EventDomain, UpcomingAdapter.UpcomingViewHolder>(
    UpcomingDiffCallback
) {
    private var onCardClick: ((EventDomain) -> Unit)? = null

    fun setOnClickCallback(action: ((EventDomain) -> Unit)?) {
        onCardClick = action
    }

    inner class UpcomingViewHolder(private val binding: ItemUpcomingEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: EventDomain) = binding.run {
            tvEventName.text = data.name
            imgEvent.load(data.imageLogo)
            binding.root.setOnClickListener {
                onCardClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingViewHolder = UpcomingViewHolder(
        ItemUpcomingEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object UpcomingDiffCallback : DiffUtil.ItemCallback<EventDomain>() {
        override fun areItemsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem == newItem
    }
}