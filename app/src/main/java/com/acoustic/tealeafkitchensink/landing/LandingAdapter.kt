/*
 * LandingAdapter.kt
 *
 * Created by kimberlysweeney on 03-21-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink.landing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.acoustic.tealeafkitchensink.data.Component
import com.acoustic.tealeafkitchensink.databinding.ListItemLandingBinding

class LandingAdapter(private val clickListener: LandingClickListener) : ListAdapter<Component, LandingAdapter.ViewHolder>(
    LandingDiffCallback()
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder(private val binding: ListItemLandingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(component: Component, clickListener: LandingClickListener) {
            binding.component = component
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemBinding = ListItemLandingBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(itemBinding)
            }
        }
    }

}

class LandingDiffCallback : DiffUtil.ItemCallback<Component>() {

    override fun areItemsTheSame(oldItem: Component, newItem: Component): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
        return oldItem == newItem
    }
}

class LandingClickListener(val clickListener: (title: String) -> Unit) {
    fun onClick(component: Component) = clickListener(component.title)
}