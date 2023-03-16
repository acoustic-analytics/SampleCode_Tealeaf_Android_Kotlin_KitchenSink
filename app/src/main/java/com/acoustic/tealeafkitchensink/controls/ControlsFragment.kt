/*
 * ControlsFragment.kt
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

package com.acoustic.tealeafkitchensink.controls

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.annotation.MenuRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.util.Pair
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.R
import com.acoustic.tealeafkitchensink.databinding.FragmentControlsBinding
import com.acoustic.tealeafkitchensink.landingdetail.LandingDetailClickHandler
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.tl.uic.Tealeaf
import com.tl.uic.util.DialogLogScreenTask
import com.tl.uic.util.TaskRunner
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*


class ControlsFragment: Fragment(), MenuProvider, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {

    private val meetingTag = "ScheduleMeeting"
    private val flightTag = "BookFlight"
    private val timeTag = "PickTime"

    private lateinit var binding: FragmentControlsBinding
    private lateinit var mPlanets: Array<String>

    private lateinit var mPlanetsArrayAdapter: ArrayAdapter<String>

    private lateinit var mMap: GoogleMap

    private var clickHandler: LandingDetailClickHandler? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentControlsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = host as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val meetingDateBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
        val meetingDatePicker: MaterialDatePicker<Long> = meetingDateBuilder.build()
        meetingDatePicker.addOnPositiveButtonClickListener {
            Tealeaf.logCustomEvent("MeetingDatePickerOK")
        }
        meetingDatePicker.addOnNegativeButtonClickListener {
            Tealeaf.logCustomEvent("MeetingDatePickerCancel")
        }

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar[Calendar.MONTH] = Calendar.JANUARY
        calendar[Calendar.YEAR] = 2023
        calendar[Calendar.DAY_OF_MONTH] = 1
        val startDateBegin = calendar.timeInMillis
        calendar[Calendar.DAY_OF_MONTH] = 10
        val startDateEnd = calendar.timeInMillis

        val flightDateBuilder = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select Dates")
                .setSelection(Pair(startDateBegin, startDateEnd))
        val flightDatePicker: MaterialDatePicker<Pair<Long, Long>> = flightDateBuilder.build()

        flightDatePicker.addOnPositiveButtonClickListener { range ->

            var startDate: String? = null
            var endDate: String? = null
            range.first?.let { start ->
                range.second?.let { end ->

                    val calendarStart = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = start }
                    val calendarEnd = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = end }

                    val startYear = calendarStart.get(Calendar.YEAR)
                    val startMonth = calendarStart.get(Calendar.MONTH) + 1
                    val startDay = calendarStart.get(Calendar.DAY_OF_MONTH)
                    startDate = "$startMonth/$startDay/$startYear"

                    val endYear = calendarEnd.get(Calendar.YEAR)
                    val endMonth = calendarEnd.get(Calendar.MONTH) + 1
                    val endDay = calendarEnd.get(Calendar.DAY_OF_MONTH)
                    endDate = "$endMonth/$endDay/$endYear"

                }
            }

            binding.contentDatePickers.datePickersRangeText.text = String.format(getString(R.string.date_pickers_range_text), startDate, endDate)
            binding.contentDatePickers.datePickersRangeText.visibility = View.VISIBLE

        }

        flightDatePicker.addOnCancelListener {
            binding.contentDatePickers.datePickersRangeText.visibility = View.GONE
        }

        val timePickerBuilder = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(15)
                .setTitleText("Select meeting time")
        val timePicker: MaterialTimePicker = timePickerBuilder.build()

        clickHandler = context?.let { LandingDetailClickHandler(it) }
        mPlanets = resources.getStringArray(R.array.planets_array)
        mPlanetsArrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item_planet, mPlanets)

        binding.contentMenus.menuExposedAutocompleteTextview.setAdapter(mPlanetsArrayAdapter)
        binding.contentMenus.menuExposedAutocompleteTextview.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Tealeaf.logEvent(view, "PlanetSelected")
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //nothing to do here
            }
        }

        binding.contentSpinners.spinner.adapter = mPlanetsArrayAdapter
        binding.contentSpinners.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //nothing to do here
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Tealeaf.logEvent(view, "PlanetSelected")
            }
        }

        val touchables = binding.root.touchables
        for (view in touchables) {
            when (view) {
                is Button -> {
                    view.setOnClickListener {
                        if(view.contentDescription != null) {
                            when {
                                view.contentDescription.equals("select an option") -> {
                                    showMenu(view, R.menu.menu_main)
                                }
                                view.contentDescription.equals("schedule a meeting") -> {
                                    meetingDatePicker.show(requireActivity().supportFragmentManager, meetingTag)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        logMaterialDialog(meetingDatePicker.dialog,"MeetingDatePicker")
                                    }, 700)
                                }
                                view.contentDescription.equals("book a flight") -> {
                                    flightDatePicker.show(requireActivity().supportFragmentManager, flightTag)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        logMaterialDialog(flightDatePicker.dialog,"FlightDatePicker")
                                    }, 700)
                                }
                                view.contentDescription.equals("pick meeting time") -> {
                                    timePicker.show(requireActivity().supportFragmentManager, timeTag)
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        logMaterialDialog(timePicker.dialog,"MeetingTimePicker")
                                    }, 700)
                                }
                                else -> clickHandler?.showToast("${view.contentDescription} has been clicked")
                            }
                        } else {
                            clickHandler?.showToast("${view.text} has been clicked")
                        }
                    }
                }
            }
        }

        val supportMapFragment = childFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment
        supportMapFragment.getMapAsync(this)
        val locationButton = binding.contentMaps.mapsLocation
        locationButton.setOnClickListener {
            if (checkLocationPermissions()) {
                goToLocation(mMap)
            } else {
                requestLocationPermissions()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun localDateFromTimestamp(timestamp: Long): LocalDate = Instant.ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    private fun goToLocation(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(37.4215452, -122.0837541))
                .title("Apple")
        )
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    37.4215452,
                    -122.0837541
                ), 10f
            )
        )
    }

    @SuppressLint("SetTextI18n")
    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(requireContext(), v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener {
            Tealeaf.logEvent(v)
            binding.contentMenus.menuDropdownButton.text = getString(R.string.menu_dropdown_button_text) + " = " + it
            false
        }
        popup.show()
    }

    private fun logMaterialDialog(alertDialog: Dialog?, name: String) {
        val dialogLogScreenTask = DialogLogScreenTask(
            activity,
            name,
            alertDialog,
            Tealeaf.getCurrentSessionId(),
            false,
            true
        )
        TaskRunner.execute(dialogLogScreenTask, *arrayOfNulls(0))
    }

    private fun checkLocationPermissions(): Boolean {
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestLocationPermissions() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                location_permission_request_code
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            location_permission_request_code -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    goToLocation(mMap)
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }
    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        if (checkLocationPermissions()) {
            goToLocation(mMap)
        } else {
            requestLocationPermissions()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.setGroupVisible(R.id.group_toolbars, false)
        menu.setGroupVisible(R.id.group_search, false)
        menu.setGroupVisible(R.id.group_main, true)
    }

    @SuppressLint("SetTextI18n")
    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        binding.controlsMenuItemSelected.text = "$menuItem has been selected from the menu."
        binding.controlsMenuItemSelected.visibility = View.VISIBLE
        Tealeaf.logCustomEvent("MenuItemSelected")
        return false
    }

    companion object {

        private const val location_permission_request_code = 42

        @JvmStatic
        fun newInstance() = ControlsFragment()

    }

}