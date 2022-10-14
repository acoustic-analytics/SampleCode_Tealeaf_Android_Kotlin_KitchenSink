/*
 * BarsNavigationViewFragment.kt
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
import com.acoustic.tealeafkitchensink.databinding.FragmentBarsExtrasBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [BarsExtrasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarsExtrasFragment : Fragment() {

    private lateinit var binding: FragmentBarsExtrasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBarsExtrasBinding.inflate(inflater, container, false)

        binding.contentSnackbar.snackBarButton.setOnClickListener {
            Snackbar.make(it, getString(R.string.snack_bar_text), Snackbar.LENGTH_LONG).show()
        }

        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance() = BarsExtrasFragment()
    }
}