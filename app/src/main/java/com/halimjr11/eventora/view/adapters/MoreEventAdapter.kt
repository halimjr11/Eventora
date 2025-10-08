package com.halimjr11.eventora.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.halimjr11.eventora.databinding.ItemVerticalEventBinding
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.ui.helper.loadImage

class MoreEventAdapter : ListAdapter<EventDomain, MoreEventAdapter.MoreEventViewHolder>(
    UpcomingDiffCallback
) {
    private var onCardClick: ((EventDomain) -> Unit)? = null

    fun setOnClickCallback(action: ((EventDomain) -> Unit)?) {
        onCardClick = action
    }

    inner class MoreEventViewHolder(private val binding: ItemVerticalEventBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: EventDomain) = binding.run {
            tvEventTitle.text = data.name
            tvEventDesc.text = data.summary
            imgThumb.loadImage(data.imageLogo)
            binding.root.setOnClickListener {
                onCardClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MoreEventViewHolder = MoreEventViewHolder(
        ItemVerticalEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: MoreEventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object UpcomingDiffCallback : DiffUtil.ItemCallback<EventDomain>() {
        override fun areItemsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem == newItem
    }
}