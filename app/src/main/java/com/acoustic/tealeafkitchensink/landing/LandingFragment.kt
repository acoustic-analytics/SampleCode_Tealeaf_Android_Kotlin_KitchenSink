/*
 * LandingFragment.kt
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

package com.acoustic.tealeafkitchensink.landing

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentLandingBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation
 */
class LandingFragment : Fragment(), MenuProvider {

    private var _binding: FragmentLandingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentLandingBinding.inflate(inflater, container, false)

        val viewModelFactory = LandingViewModelFactory()
        val landingViewModel = ViewModelProvider(this, viewModelFactory).get(LandingViewModel::class.java)

        binding.landingViewModel = landingViewModel

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val adapter = LandingAdapter(LandingClickListener { itemId ->
            landingViewModel.onLandingItemClicked(itemId)
        })
        binding.recyclerView.adapter = adapter

        //add data to the recyclerview adapter
        landingViewModel.componentItems.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.lifecycleOwner = this

        // Add an Observer on the state variable for Navigating when an item is clicked.
        landingViewModel.navigateToLandingDetail.observe(viewLifecycleOwner, Observer { item ->
            item?.let {
                this.findNavController().navigate(
                    LandingFragmentDirections.actionLandingFragmentToLandingDetailFragment(item))
                landingViewModel.onLandingDetailNavigated()
            }
        })

        //setting up recyclerview divider lines
        val manager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = manager
        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerView.context,
            manager.orientation
        )
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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