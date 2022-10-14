/*
 * LandingAdapter.kt
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

package com.acoustic.tealeafkitchensink.landingdetail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import com.acoustic.tealeafkitchensink.R

class LandingDetailSocialMediaAdapter(context: Context?, items: Array<String>) : ArrayAdapter<String?>(context!!, -1, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return customView(position, convertView, parent)
    }

    private fun customView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = convertView

        if (view == null) {
            view =
                LayoutInflater.from(context).inflate(R.layout.list_item_soical_media, parent, false)
        }

        //get data
        val item = getItem(position)

        //get layout
        val platform = view!!.findViewById<CheckedTextView>(R.id.social_media_text_view)

        //set value
        platform.text = item

        platform.setOnClickListener {
            if (platform.isChecked) {
                platform.checkMarkDrawable = null
                platform.isChecked = false
            } else {
                platform.setCheckMarkDrawable(R.drawable.ic_baseline_check_circle_outline_24)
                platform.isChecked = true
            }
        }

        return view

    }

}



