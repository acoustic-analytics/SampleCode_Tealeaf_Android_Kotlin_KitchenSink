/*
 * ContainerViewsFragment.kt
 *
 * Created by kimberlysweeney on 08-08-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink.containerviews

import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentContainerViewsBinding
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Use the [ContainerViewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContainerViewsFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentContainerViewsBinding

    private lateinit var mPlanets: Array<String>
    private lateinit var mPlanetsArrayAdapter: ArrayAdapter<String>

    private lateinit var mCountdown: Array<String>

    private var mImageUrl = "https://picsum.photos/600"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContainerViewsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = host as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mPlanets = resources.getStringArray(R.array.planets_array)
        mPlanetsArrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mPlanets)
        binding.contentConstraintLayout.constraintLayoutRightColumnContent.adapter = mPlanetsArrayAdapter

        mCountdown = resources.getStringArray(R.array.countdown)

        val horizontalScrollViewContent = binding.contentScrollViews.scrollViewsHorizontalContent
        val nestedScrollViewContent = binding.contentScrollViews.scrollViewsNestedContent

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        val marginLarge = resources.getDimensionPixelSize(R.dimen.thumbnail_margin_large)
        val marginSmall = resources.getDimensionPixelSize(R.dimen.thumbnail_margin_small)

        for((index, item) in mCountdown.withIndex()) {

            val countdownHorizontalButton = Button(context)
            params.rightMargin = marginLarge
            params.bottomMargin = marginSmall
            populateButtons(item, params, horizontalScrollViewContent, index, countdownHorizontalButton)

            val countdownNestedButton = Button(context)
            params.setMargins(marginLarge, marginLarge, marginLarge, marginLarge)
            populateButtons(item, params, nestedScrollViewContent, index, countdownNestedButton)
        }

        val frameImage = binding.contentFrameLayout.frameLayoutImage
        Picasso.get().load(mImageUrl).into(frameImage)

        return binding.root
    }

    private fun populateButtons(
        item: String,
        params: LinearLayout.LayoutParams,
        view: LinearLayout,
        index: Int,
        button: Button
    ) {
        button.text = item
        button.setBackgroundResource(R.drawable.rounded_button_2)
        button.layoutParams = params
        view.addView(button)
        if (index == 10) button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
    }

    override fun onResume() {
        super.onResume()
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
        fun newInstance() = ContainerViewsFragment()
    }

}