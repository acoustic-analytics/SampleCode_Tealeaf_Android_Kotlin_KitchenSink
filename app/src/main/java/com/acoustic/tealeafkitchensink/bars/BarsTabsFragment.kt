/*
 * BarsTabLayoutFragment.kt
 *
 * Created by kimberlysweeney on 08-02-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink.bars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentBarsTabsBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Use the [BarsTabsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarsTabsFragment : Fragment() {

    private lateinit var binding: FragmentBarsTabsBinding
    private var mImageUrl = "https://picsum.photos/600"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBarsTabsBinding.inflate(inflater, container, false)

        binding.contentTabsFixed.tabFixed.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> { binding.contentTabsFixed.tabFixedImage.setImageResource(R.drawable.logo_kotlin) }
                    1 -> { binding.contentTabsFixed.tabFixedImage.setImageResource(R.drawable.logo_java) }
                    2 -> { binding.contentTabsFixed.tabFixedImage.setImageResource(R.drawable.logo_flutter) }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        val imageScrollable = binding.contentTabsScrollable.tabScrollableImage
        Picasso.get().load(mImageUrl).into(imageScrollable)

        binding.contentTabsScrollable.tabScrollable.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mImageUrl = "https://picsum.photos/600?random=${tab.position + 1}"
                Picasso.get().load(mImageUrl).into(imageScrollable)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance() = BarsTabsFragment()
    }
}