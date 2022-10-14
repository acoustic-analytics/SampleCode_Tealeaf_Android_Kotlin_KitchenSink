/*
 * SingleWebViewFragment.kt
 *
 * Created by Hugo Castro on 08-02-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink.contentviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.acoustic.tealeafkitchensink.databinding.FragmentSingleWebviewBinding


/**
 * A simple [Fragment] subclass that contains a single web view.
 */
class SingleWebViewFragment : Fragment() {

    private lateinit var binding: FragmentSingleWebviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleWebviewBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webviewSingleLayout.loadUrl("https://acoustic.com/")
    }

    companion object {
        @JvmStatic
        fun newInstance() = SingleWebViewFragment()
    }
}
