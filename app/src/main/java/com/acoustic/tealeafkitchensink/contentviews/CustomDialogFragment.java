/*
 * CustomDialogFragment.kt
 *
 * Created by kimberlysweeney on 09-27-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */
package com.acoustic.tealeafkitchensink.contentviews;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.acoustic.tealeafkitchensink.R;
import com.tl.uic.Tealeaf;
import com.tl.uic.util.TLFDialogFragment;

public class CustomDialogFragment extends TLFDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = this.getActivity();
        while (activity != null && activity.getParent() != null) {
            activity = activity.getParent();
        }

        Tealeaf.logScreenLayoutSetOnShowListener(requireActivity(), requireDialog(), "CustomDialogFragment", false);
    }


}