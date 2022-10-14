/*
 * ClickHandler.java
 *
 * Created by kimberlysweeney on 07-25-2022.
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
import android.widget.Toast

class LandingDetailClickHandler(private val context: Context) {

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}