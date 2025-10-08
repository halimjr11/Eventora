package com.halimjr11.eventora.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.halimjr11.eventora.databinding.ItemCarouselEventBinding
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.ui.helper.loadImage

class CarouselAdapter :
    ListAdapter<EventDomain, CarouselAdapter.CarouselViewHolder>(CarouselDiffCallback) {
    private var onCardClick: ((EventDomain) -> Unit)? = null

    fun setOnClickCallback(action: ((EventDomain) -> Unit)?) {
        onCardClick = action
    }

    inner class CarouselViewHolder(
        private val binding: ItemCarouselEventBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: EventDomain) = binding.run {
            ivCarouselBanner.loadImage(data.mediaCover)
            binding.root.setOnClickListener {
                onCardClick?.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CarouselViewHolder = CarouselViewHolder(
        binding = ItemCarouselEventBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object CarouselDiffCallback : DiffUtil.ItemCallback<EventDomain>() {
        override fun areItemsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem.hashCode() == newItem.hashCode()

        override fun areContentsTheSame(oldItem: EventDomain, newItem: EventDomain): Boolean =
            oldItem == newItem
    }
}