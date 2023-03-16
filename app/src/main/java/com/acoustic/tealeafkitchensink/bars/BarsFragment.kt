/*
 * BarsSearchFragment.kt
 *
 * Created by kimberlysweeney on 08-03-2022.
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
import com.acoustic.tealeafkitchensink.databinding.FragmentBarsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [BarsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarsFragment : Fragment() {

    private lateinit var binding: FragmentBarsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBarsBinding.inflate(inflater, container, false)

        val appbarsFragment = BarsAppbarsFragment()
        val tabsFragment = BarsTabsFragment()
        val toolbarsFragment = BarsToolbarsFragment()
        val widgetsFragment = BarsExtrasFragment()
        val searchFragment = BarsSearchFragment()

        setCurrentFragment(toolbarsFragment)

        val bottomNav = binding.barsBottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.action_toolbars -> setCurrentFragment(toolbarsFragment)
                R.id.action_tabs -> setCurrentFragment(tabsFragment)
                R.id.action_appbars -> setCurrentFragment(appbarsFragment)
                R.id.action_search -> setCurrentFragment(searchFragment)
                R.id.action_extras -> setCurrentFragment(widgetsFragment)
            }
            true
        }

        val badge = bottomNav.getOrCreateBadge(R.id.action_search)
        badge.isVisible = true
        badge.number = 4

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun setCurrentFragment(fragment:Fragment) =
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.bars_frame_layout,fragment)
            commit()
        }

    companion object {
        @JvmStatic
        fun newInstance() = BarsFragment()
    }
}