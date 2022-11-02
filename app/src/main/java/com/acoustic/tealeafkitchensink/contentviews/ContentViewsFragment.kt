/*
 * ContentViewsFragment.kt
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

package com.acoustic.tealeafkitchensink.contentviews

import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentContentViewsBinding
import com.acoustic.tealeafkitchensink.landingdetail.LandingDetailClickHandler
import com.acoustic.tealeafkitchensink.landingdetail.LandingDetailFragmentDirections
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tl.uic.Tealeaf
import com.tl.uic.util.DialogUtil

/**
 * A simple [Fragment] subclass.
 * Use the [ContentViewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContentViewsFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentContentViewsBinding
    private var clickHandler: LandingDetailClickHandler? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContentViewsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        clickHandler = context?.let { LandingDetailClickHandler(it) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup glide image view
        val glideImageView = binding.contentImageViews.imageViewGlideLayout
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.navdrawer_header_background)
            .fitCenter()
            .into(glideImageView)

        val radius = resources.getDimension(R.dimen.corner_radius)
        val shapeableImageView =
            binding.contentImageViews.imageViewShapeableLayout
        val shapeAppearanceModel = shapeableImageView.shapeAppearanceModel.toBuilder()
            .setAllCornerSizes(radius)
            .build()
        shapeableImageView.shapeAppearanceModel = shapeAppearanceModel

        // Setup switcher image view
        val imageList = intArrayOf(
            R.drawable.logo_linkedin,
            R.drawable.logo_pinterest,
            R.drawable.logo_telegram
        )
        val imageSwitcher = binding.contentImageViews.imageViewSwitcherLayout
        imageSwitcher.setFactory {
            val imageView = ImageView(requireContext())
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView.setPadding(8, 8, 8, 8)
            imageView
        }
        var index = 0
        imageSwitcher.setImageResource(imageList[index])
        imageSwitcher.setOnClickListener {
            index = if (index + 1 < imageList.size) index + 1 else 0
            imageSwitcher.setImageResource(imageList[index])
        }

        // Setup Progress views
        val progressCircle = binding.contentProgressViews.progressCircularDeterminateLayout
        val progressCircleHandler = Handler(Looper.getMainLooper())
        Thread {
            // this loop will run until the value of i becomes 100
            var i = 0
            while (i <= 100) {
                // Update the progress bar and display the current value
                progressCircleHandler.post {
                    progressCircle.progress = i
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                i += 10
            }
        }.start()

        val progressBar = binding.contentProgressViews.progressLinearDeterminateLayout
        val progressBarHandler = Handler(Looper.getMainLooper())
        Thread {
            // this loop will run until the value of i becomes 100
            var i = 0
            while (i <= 100) {
                // Update the progress bar and display the current value
                progressBarHandler.post {
                    progressBar.progress = i
                }
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

                i += 10
            }
        }.start()

        // Setup web views
        binding.contentWebViews.webSingleLayout.setOnClickListener {
            findNavController().navigate(LandingDetailFragmentDirections.actionLandingDetailFragmentToSingleWebViewFragment())
        }
        binding.contentWebViews.webDoubleLayout.setOnClickListener {
            findNavController().navigate(LandingDetailFragmentDirections.actionLandingDetailFragmentToDoubleWebViewFragment())
        }

        // Setup dialogs
        binding.contentDialogs.buttonAlertLayout.setOnClickListener {
            //the following is an example on how to manually instrument an alert dialog
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle(resources.getString(R.string.dialog_alert_title))
                .setMessage(resources.getString(R.string.dialog_alert_supporting_text))
                .setNeutralButton(resources.getString(R.string.dialog_button_cancel)) { dialog, which ->
                    // Respond to neutral button press
                    Tealeaf.logDialogEvent(dialog, which)
                    clickHandler?.showToast(resources.getString(R.string.dialog_button_cancel) + " has been clicked")
                }
                .setNegativeButton(resources.getString(R.string.dialog_button_decline)) { dialog, which ->
                    // Respond to negative button press
                    Tealeaf.logDialogEvent(dialog, which)
                    clickHandler?.showToast(resources.getString(R.string.dialog_button_decline) + " has been clicked")
                }
                .setPositiveButton(resources.getString(R.string.dialog_button_accept)) { dialog, which ->
                    // Respond to positive button press
                    Tealeaf.logDialogEvent(dialog, which)
                    clickHandler?.showToast(resources.getString(R.string.dialog_button_accept) + " has been clicked")
                }
            val dialog = builder.create()
            Tealeaf.logScreenLayoutSetOnShowListener(requireActivity(), dialog)
            dialog.show()

        }

        binding.contentDialogs.buttonSimpleLayout.setOnClickListener {
            val items =
                arrayOf("This is action 1", "This is action 2", "No buttons")

            MaterialAlertDialogBuilder(this.requireContext())
                .setTitle(resources.getString(R.string.dialog_simple_title))
                .setItems(items) { dialog, which ->
                    // Respond to item chosen
                    clickHandler?.showToast(items[which] + " has been clicked")
                }
                .show()
        }

        binding.contentDialogs.buttonConfirmationLayout.setOnClickListener {
            val options = arrayOf("This is option 1", "This is option 2", "This is option 3")
            val checkedItem = 1

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.dialog_confirmation_title))
                .setNeutralButton(resources.getString(R.string.dialog_button_cancel)) { dialog, which ->
                    // Respond to neutral button press
                    clickHandler?.showToast(resources.getString(R.string.dialog_button_cancel) + " has been clicked")
                }
                .setPositiveButton(resources.getString(R.string.dialog_button_accept)) { dialog, which ->
                    // Respond to positive button press
                    clickHandler?.showToast(resources.getString(R.string.dialog_button_accept) + " has been clicked")
                }
                // Single-choice items (initialized with checked item)
                .setSingleChoiceItems(options, checkedItem) { dialog, which ->
                    // Respond to item chosen
                    clickHandler?.showToast(options[which] + " has been clicked")
                }
                .show()
        }

        binding.contentDialogs.buttonFullscreenLayout.setOnClickListener {
            findNavController().navigate(LandingDetailFragmentDirections.actionLandingDetailFragmentToDialogFullscreenFragment())
        }

        // This is an example of instantiating an Alert Dialog which will auto-generate an alert
        // which contains a Title, Message, and Negative Button utilizing Tealeaf's custom AlertDialog class.
        binding.contentDialogs.dialogsAutoGeneratedLayout.setOnClickListener {
            val title = getString(R.string.dialogs_auto_generated_title)
            val message = getString(R.string.dialogs_auto_generated_message)
            //To instrument when the dialog is shown, use a custom method to show the dialog.
            DialogUtil.showDialog(
                context,
                title,
                message,
                getString(R.string.dialogs_auto_generated_button_text)
            )
        }

        // This is an example of instantiating an Alert Dialog which contains user-specified parameters
        // within a custom builder.  This dialog will instantiate Tealeaf's custom Alert Dialog class
        binding.contentDialogs.dialogsUserGeneratedLayout.setOnClickListener { v14: View ->
            val builder = AlertDialog.Builder(v14.context)
            val title = getString(R.string.dialogs_user_generated_title)
            val message = getString(R.string.dialogs_user_generated_message)
            builder.setMessage(message)
                .setTitle(title)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.dialogs_user_generated_positive_button)) { dialog: DialogInterface, _: Int -> dialog.cancel() }
                .setNegativeButton(getString(R.string.dialogs_user_generated_negative_button)) { dialog: DialogInterface, _: Int -> dialog.cancel() }
            val dialog = builder.create()
            //To instrument when the dialog is shown, use a custom method to show the dialog.
            DialogUtil.showDialog(context, dialog)
        }

        // This is an example of instantiating a Dialog Fragment which inherits from Android's
        // DialogFragment class and will require manual instrumentation within the custom dialog to
        // instrument user interaction.
        binding.contentDialogs.dialogsCustomFragmentLayout.setOnClickListener {
            val dialogFragment = CustomDialogFragment()
            dialogFragment.show(
                requireActivity().supportFragmentManager,
                getString(R.string.dialogs_custom_fragment_tag)
            )
        }

        // Sheet views setup
        val standardBottomSheetBehavior =
            BottomSheetBehavior.from(binding.contentSheetBottomStandard.root)

        binding.contentSheetBottomStandard.sheetBottomStandardActionButton.setOnClickListener {
            clickHandler?.showToast("Bottom sheet action 1 clicked")
        }

        binding.contentSheetBottomStandard.sheetBottomStandardTitle.setOnClickListener {
            standardBottomSheetBehavior.state =
                if (standardBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    BottomSheetBehavior.STATE_EXPANDED
                }
        }

        binding.contentSheetViews.sheetBottomStandardButton.setOnClickListener {
            standardBottomSheetBehavior.state =
                if (standardBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    BottomSheetBehavior.STATE_COLLAPSED
                } else {
                    BottomSheetBehavior.STATE_EXPANDED
                }
        }

        binding.contentSheetViews.sheetBottomModalButton.setOnClickListener {
            val modalBottomSheet = ModalBottomSheetFragment()
            modalBottomSheet.show(requireActivity().supportFragmentManager, ModalBottomSheetFragment.TAG)
        }
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
        fun newInstance() = ContentViewsFragment()

        private const val imageUrl: String =
            "https://upload.wikimedia.org/wikipedia/commons/4/45/Law_Courts_during_blue_hour%2C_Christchurch%2C_New_Zealand.jpg"
    }
}
