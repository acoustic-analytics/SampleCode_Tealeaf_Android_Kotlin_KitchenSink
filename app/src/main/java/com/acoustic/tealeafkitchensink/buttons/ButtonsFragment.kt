/*
 * ButtonsFragment.kt
 *
 * Created by kimberlysweeney on 08-10-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink.buttons

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentButtonsBinding
import com.acoustic.tealeafkitchensink.landingdetail.LandingDetailClickHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tl.uic.Tealeaf

/**
 * A simple [Fragment] subclass.
 * Use the [ButtonsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ButtonsFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentButtonsBinding
    private var clickHandler: LandingDetailClickHandler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentButtonsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        clickHandler = context?.let { LandingDetailClickHandler(it) }

        val touchables = binding.root.touchables
        for (view in touchables) {
            when (view) {
                is Button -> {
                    view.setOnClickListener {
                        clickHandler?.showToast("${view.text} has been clicked")
                        /** start: manually instrument event capture when a view is clicked **/
                        Tealeaf.logEvent(view)
                        /** end: manually instrument event capture **/
                    }
                }
                is FloatingActionButton -> {
                    view.setOnClickListener {
                        clickHandler?.showToast("${view.contentDescription} has been clicked")
                    }

                }
                is AppCompatImageButton -> {
                    view.setOnClickListener {
                        clickHandler?.showToast("${view.contentDescription} has been clicked")
                    }

                }
            }
        }
        return binding.root
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.setGroupVisible(R.id.group_toolbars, false)
        menu.setGroupVisible(R.id.group_search, false)
        menu.setGroupVisible(R.id.group_main, false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    companion object {
        @JvmStatic
        fun newInstance() = ButtonsFragment()
    }
}
