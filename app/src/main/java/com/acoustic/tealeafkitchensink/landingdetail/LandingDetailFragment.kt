/*
 * LandingDetailFragment.kt
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

package com.acoustic.tealeafkitchensink.landingdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.bars.BarsFragment
import com.acoustic.tealeafkitchensink.buttons.ButtonsFragment
import com.acoustic.tealeafkitchensink.containerviews.ContainerViewsFragment
import com.acoustic.tealeafkitchensink.contentviews.ContentViewsFragment
import com.acoustic.tealeafkitchensink.controls.ControlsFragment
import com.acoustic.tealeafkitchensink.databinding.FragmentLandingDetailBinding
import com.acoustic.tealeafkitchensink.notlogged.NotLoggedFragment
import com.acoustic.tealeafkitchensink.textviews.TextViewsFragment
import com.ibm.eo.EOCore
import com.tl.uic.Tealeaf
import com.tl.uic.TealeafEOLifecycleObject

class LandingDetailFragment : Fragment() {

    private var _binding: FragmentLandingDetailBinding? = null
    private val binding get() = _binding!!

    private val barsFragment = BarsFragment()
    private val buttonsFragment = ButtonsFragment()
    private val controlsFragment = ControlsFragment()
    private val textViewsFragment = TextViewsFragment()
    private val containerViewsFragment = ContainerViewsFragment()
    private val notLoggedFragment = NotLoggedFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLandingDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: LandingDetailFragmentArgs by navArgs()
        when {
            args.itemTitle.lowercase() == "buttons" -> {
                setCurrentFragment(buttonsFragment, "Buttons")
            }
            args.itemTitle.lowercase() == "controls" -> {
                setCurrentFragment(controlsFragment, "Controls")
            }
            args.itemTitle.lowercase() == "text views" -> {
                setCurrentFragment(textViewsFragment, "TextViews")
            }
            args.itemTitle.lowercase() == "bars" -> {
                setCurrentFragment(barsFragment, "Bars")
            }
            args.itemTitle.lowercase() == "content views" -> {
                val contentViewsFragment = ContentViewsFragment()
                setCurrentFragment(contentViewsFragment, "ContentViews")
            }
            args.itemTitle.lowercase() == "container views" -> {
                setCurrentFragment(containerViewsFragment, "ContainerViews")
            }
            args.itemTitle.lowercase() == "fragment not logged" -> {
                // Disable automatic fragment logging
                EOCore.updateConfig(
                    Tealeaf.TLF_Enable_Fragment_Life_Cycle_Listener,
                    "false",
                    TealeafEOLifecycleObject.getInstance()
                )
                setCurrentFragment(notLoggedFragment, "This Fragment Is Not Logged")
            }
        }
    }

    private fun setCurrentFragment(fragment: Fragment, name: String) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.dynamic_layout, fragment, name)
            commit()
        }

    override fun onPause() {
        super.onPause()

        // Enable automatic fragment logging
        EOCore.updateConfig(
            Tealeaf.TLF_Enable_Fragment_Life_Cycle_Listener,
            "true",
            TealeafEOLifecycleObject.getInstance()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}