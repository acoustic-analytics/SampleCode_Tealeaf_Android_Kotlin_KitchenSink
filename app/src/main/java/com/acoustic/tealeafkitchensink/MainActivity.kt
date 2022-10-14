/*
 * MainActivity.kt
 *
 * Created by kimberlysweeney on 03-11-2022.
 *
 * Copyright (C) 2022 Acoustic, L.P. All rights reserved.
 *
 * NOTICE: This file contains material that is confidential and proprietary to
 * Acoustic, L.P. and/or other developers. No license is granted under any intellectual or
 * industrial property rights of Acoustic, L.P. except as may be provided in an agreement with
 * Acoustic, L.P. Any unauthorized copying or distribution of content from this file is
 * prohibited.
 */

package com.acoustic.tealeafkitchensink

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.acoustic.tealeafkitchensink.databinding.ActivityMainBinding
import com.tl.uic.Tealeaf
import com.tl.uic.util.ActionBarDrawerToggleTealeaf

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggleTealeaf

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var tealeaf = Tealeaf(application)
        Tealeaf.enable()

        //get drawer layout and add listener for capturing open/close events
        //the actionBarDrawerToggle will be used to automatically capture the events
        setSupportActionBar(binding.toolbar)
        drawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggleTealeaf(
            this,
            drawerLayout,
            R.string.nav_drawer_menu_open,
            R.string.nav_drawer_menu_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        //show NavDrawer button on the actionbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.addOnDestinationChangedListener{ nc: NavController, nd: NavDestination, _: Bundle? ->
            if(nd.id == nc.graph.startDestinationId) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                /** start: manually capture open event in replay **/
                Tealeaf.logEvent(drawerLayout, "OnDrawerOpened")
                /** end: manually capture open event **/
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                /** start: manually capture close event in replay **/
                Tealeaf.logEvent(drawerLayout, "OnDrawerClosed")
                /** end: manually capture close event **/
            }
        }

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun dispatchTouchEvent(e: MotionEvent?): Boolean {
        Tealeaf.dispatchTouchEvent(this, e)
        return super.dispatchTouchEvent(e)
    }

}