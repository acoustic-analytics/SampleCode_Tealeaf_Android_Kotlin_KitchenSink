/*
 * CustomEventsFragment.kt
 *
 * Created by kimberlysweeney on 09-20-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.databinding.FragmentCustomEventsBinding
import com.tl.uic.Tealeaf

class CustomEventsFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentCustomEventsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCustomEventsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val data = HashMap<String, String>()
        data["Foo"] = "Bar"

        val eventButton = binding.contentCustomEvents.customEventsEventButton
        eventButton.setOnClickListener {
            Tealeaf.logCustomEvent("MyEvent", data)
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
}