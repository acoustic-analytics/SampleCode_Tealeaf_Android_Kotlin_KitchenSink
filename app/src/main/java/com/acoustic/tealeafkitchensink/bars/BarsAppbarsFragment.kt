/*
 * BarsAppBarLayoutFragment.kt
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
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentBarsAppbarsBinding


/**
 * A simple [Fragment] subclass.
 * Use the [BarsAppbarsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarsAppbarsFragment : Fragment() {

    private lateinit var binding: FragmentBarsAppbarsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBarsAppbarsBinding.inflate(inflater, container, false)

        val bottomNav = requireActivity().findViewById<View>(R.id.bars_bottom_navigation_view)
        bottomNav.visibility = View.GONE

        binding.barsAppbarsFloatingActionButton.setOnClickListener {
            if(bottomNav.isVisible) {
                val animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_slide_out_bottom)
                bottomNav.startAnimation(animation)
                bottomNav.visibility = View.GONE
            } else {
                val animation = AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_slide_in_bottom)
                bottomNav.startAnimation(animation)
                bottomNav.visibility = View.VISIBLE
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = BarsAppbarsFragment()
    }
}