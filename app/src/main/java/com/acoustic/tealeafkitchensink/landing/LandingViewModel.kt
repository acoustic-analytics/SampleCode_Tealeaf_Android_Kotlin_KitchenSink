/*
 * LandingViewModel.kt
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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acoustic.tealeafkitchensink.data.Component

class LandingViewModel : ViewModel() {

    val componentItems = MutableLiveData<List<Component>?>()
    private val mComponentArrayList = ArrayList<Component>()

    // navigation for the LandingDetail fragment
    private val _navigateToLandingDetail = MutableLiveData<String?>()
    val navigateToLandingDetail
        get() = _navigateToLandingDetail

    fun onLandingItemClicked(title: String) {
        _navigateToLandingDetail.value = title
    }

    fun onLandingDetailNavigated() {
        _navigateToLandingDetail.value = null
    }

    init {
        populateComponentList()
        componentItems.value = mComponentArrayList
    }

    private fun populateComponentList() {
        mComponentArrayList.apply {
            add(
                Component(
                    1,
                    "Buttons",
                    "Buttons allow users to take actions, and make choices, with a single tap."
                )
            )
            add(
                Component(
                    2,
                    "Text Views",
                    "Text Views let users enter and edit text."
                )
            )
            add(
                Component(
                    3,
                    "Controls",
                    "Controls are the interactive components in your app's user interface."
                )
            )
            add(
                Component(
                    4,
                    "Bars",
                    "Bars display information and actions relating to the current screen and provide access to destinations in your app."
                )
            )
            add(
                Component(
                    5,
                    "Content Views",
                    "Content Views present specialized content."
                )
            )
            add(
                Component(
                    6,
                    "Container Views",
                    "Container Views layout, organize, and present large data sets."
                )
            )
            add(
                Component(
                    7,
                    "Fragment Not Logged",
                    "Logging for this fragment has been disabled."
                )
            )
        }
    }
}
