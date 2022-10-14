/*
 * BarsToolbarFragment.kt
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
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentBarsToolbarsBinding
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Use the [BarsToolbarsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarsToolbarsFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentBarsToolbarsBinding
    private lateinit var mMainToolbar: Toolbar
    private lateinit var mToolbar: Toolbar

    private var mImageUrl = "https://picsum.photos/500/500"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBarsToolbarsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mToolbar = binding.barsToolbarsToolbar
        mToolbar.title = "Bars"
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mMainToolbar = requireActivity().findViewById(R.id.toolbar)
        mMainToolbar.visibility = View.GONE

        val imageBackground = binding.barsToolbarsImage
        Picasso.get().load(mImageUrl).into(imageBackground)

        return binding.root

    }

    companion object {
        @JvmStatic
        fun newInstance() = BarsToolbarsFragment()
    }

    override fun onStop() {
        super.onStop()
        resetToolbar()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbars, menu)
        menu.setGroupVisible(R.id.group_toolbars, true)
        menu.setGroupVisible(R.id.group_search, false)
        menu.setGroupVisible(R.id.group_main, false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when(menuItem.itemId) {
            android.R.id.home -> {
                resetToolbar()
            }
        }
        return false
    }

    private fun resetToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(mMainToolbar)
        mToolbar.visibility = View.GONE
        mMainToolbar.visibility = View.VISIBLE
    }

}