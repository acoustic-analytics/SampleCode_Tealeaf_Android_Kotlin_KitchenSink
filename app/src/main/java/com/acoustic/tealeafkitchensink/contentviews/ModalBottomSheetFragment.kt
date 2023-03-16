/*
 * ModalBottomSheetFragment.kt
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
import com.acoustic.tealeafkitchensink.databinding.ContentSheetBottomModalBinding
import com.acoustic.tealeafkitchensink.landingdetail.LandingDetailClickHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


/**
 * A simple [BottomSheetDialogFragment] subclass that contains a couple of actions.
 */
class ModalBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: ContentSheetBottomModalBinding
    private var clickHandler: LandingDetailClickHandler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContentSheetBottomModalBinding.inflate(inflater, container, false)

        clickHandler = context?.let { LandingDetailClickHandler(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sheetBottomModalActionButton1.setOnClickListener {
            clickHandler?.showToast("Bottom sheet action 1 clicked")
        }

        binding.sheetBottomModalActionButton2.setOnClickListener {
            clickHandler?.showToast("Bottom sheet action 2 clicked")
        }
    }

    companion object {
        const val TAG = "ModalBottomSheet"
    }
}
