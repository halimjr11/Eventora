package com.halimjr11.eventora.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.halimjr11.eventora.databinding.ItemVerticalEventBinding
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.ui.helper.loadImage

class VerticalEventAdapter : ListAdapter<EventDomain, VerticalEventAdapter.FinishedViewHolder>(
    UpcomingDiffCallback
) {
    private var onCardClick: ((EventDomain) -> Unit)? = null

    fun setOnClickCallback(action: ((EventDomain) -> Unit)?) {
        onCardClick = action
    }

    inner class FinishedViewHolder(private val binding: ItemVerticalEventBinding) :
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
    ): FinishedViewHolder = FinishedViewHolder(
        ItemVerticalEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: FinishedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object UpcomingDiffCallback : DiffUtil.ItemCallback<EventDomain>() {
        override fun areItemsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem == newItem
    }
}