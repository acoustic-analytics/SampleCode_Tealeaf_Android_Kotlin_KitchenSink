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
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentBarsSearchBinding
import com.google.android.material.snackbar.Snackbar
import com.tl.uic.Tealeaf

/**
 * A simple [Fragment] subclass.
 * Use the [BarsSearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BarsSearchFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentBarsSearchBinding
    private lateinit var mCountriesAdapter: ArrayAdapter<String>
    private lateinit var mCountries: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBarsSearchBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = host as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val searchList = binding.barsSearchList
        val searchListEmpty = binding.barsSearchNoResults

        mCountries = resources.getStringArray(R.array.countries_array)
        mCountriesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mCountries)
        searchList.adapter = mCountriesAdapter
        searchList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Snackbar.make(view, "${parent?.getItemAtPosition(position)} has been selected", Snackbar.LENGTH_SHORT).show()
        }
        searchList.emptyView = searchListEmpty

        // Inflate the layout for this fragment
        return binding.root

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)

        menu.setGroupVisible(R.id.group_toolbars, false)
        menu.setGroupVisible(R.id.group_search, true)
        menu.setGroupVisible(R.id.group_main, false)

        val search = menu.findItem(R.id.action_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search for a Country"

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                /** start: manual instrumentation **/
                Tealeaf.logFormCompletion(true)
                /** end: manual instrumentation **/
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mCountriesAdapter.filter.filter(newText)
                return true
            }

        })

    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    companion object {
        @JvmStatic
        fun newInstance() = BarsSearchFragment()
    }

}