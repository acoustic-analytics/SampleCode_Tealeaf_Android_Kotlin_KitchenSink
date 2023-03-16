/*
 * TextViewsFragment.kt
 *
 * Created by kimberlysweeney on 08-04-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink.textviews

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TextSwitcher
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentTextViewsBinding
import com.acoustic.tealeafkitchensink.landingdetail.LandingDetailSocialMediaAdapter
import com.ibm.eo.EOCore
import com.tl.uic.Tealeaf
import com.tl.uic.TealeafEOLifecycleObject


class TextViewsFragment: Fragment(), MenuProvider {

    private lateinit var binding: FragmentTextViewsBinding

    private lateinit var mPlanets: Array<String>
    private lateinit var mSocialMediaList: Array<String>

    private lateinit var mPlanetsArrayAdapter: ArrayAdapter<String>
    private lateinit var mSocialMediaAdapter: LandingDetailSocialMediaAdapter

    private var mIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTextViewsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = host as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        mPlanets = resources.getStringArray(R.array.planets_array)
        mPlanetsArrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item_planet, mPlanets)
        binding.contentTextAutocomplete.textAutocompleteView.setAdapter(mPlanetsArrayAdapter)

        mSocialMediaList = resources.getStringArray(R.array.social_media_array)
        mSocialMediaAdapter = LandingDetailSocialMediaAdapter(context, mSocialMediaList)
        binding.contentTextCheckedViews.textCheckedViewListItems.adapter = mSocialMediaAdapter

        val textSwitcher = binding.contentTextSwitchers.textSwitcher
        textSwitcher.setFactory {
            val textView = TextView(context)
            textView.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            textView.textSize = 28f
            textView.setTextColor(Color.BLUE)
            textView
        }
        textSwitcher.setText(mPlanets[mIndex])

        val textIn = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
        textSwitcher.inAnimation = textIn

        val textOut = AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)
        textSwitcher.outAnimation = textOut

        val previousButton = binding.contentTextSwitchers.textSwitcherPrevious
        previousButton.setOnClickListener {
            textSwitcherAnimate(textSwitcher, 0)
            Tealeaf.logEvent(previousButton)
        }

        val nextButton = binding.contentTextSwitchers.textSwitcherNext
        nextButton.setOnClickListener {
            textSwitcherAnimate(textSwitcher, 1)
            Tealeaf.logEvent(nextButton)
        }

        val singleLineEditText = binding.contentTextEdits.textEditSingleLine
        singleLineEditText.setText(EOCore.getConfigItemString(Tealeaf.TLF_POST_MESSAGE_URL, TealeafEOLifecycleObject.getInstance()))
        binding.contentTextEdits.textEditSingleLineSubmit.setOnClickListener {

            var configModified = false

            if (singleLineEditText.text.toString() != EOCore.getConfigItemString(Tealeaf.TLF_POST_MESSAGE_URL, TealeafEOLifecycleObject.getInstance())) {
                EOCore.updateConfig(Tealeaf.TLF_POST_MESSAGE_URL, singleLineEditText.text.toString(), TealeafEOLifecycleObject.getInstance())
                configModified = true
            }

            if (configModified) {
                Tealeaf.disable()
                Tealeaf.enable()
            }

            /** start: manual instrumentation **/
            Tealeaf.logFormCompletion(true)
            /** end: manual instrumentation **/

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    private fun textSwitcherAnimate(textSwitcher: TextSwitcher, direction: Int) {
        when (direction) {
            0 -> { mIndex = if (mIndex + 1 < mPlanets.size) mIndex + 1 else 0 }
            1 -> { mIndex = if (mIndex - 1 >= 0) mIndex - 1 else 7 }
        }
        textSwitcher.setText(mPlanets[mIndex])
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
        fun newInstance() = TextViewsFragment()
    }

}