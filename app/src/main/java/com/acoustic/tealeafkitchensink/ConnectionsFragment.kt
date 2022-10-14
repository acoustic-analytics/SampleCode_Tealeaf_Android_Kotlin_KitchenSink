/*
 * CustomEventsFragment.kt
 *
 * Created by kimberlysweeney on 09-20-2022.
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
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.databinding.FragmentConnectionsBinding
import com.tl.uic.Tealeaf
import com.tl.uic.model.Connection
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ConnectionsFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentConnectionsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentConnectionsBinding.inflate(inflater, container, false)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.contentConnections.connectionsConnectionWithoutErrorButton.setOnClickListener {
            val thread = Thread {
                try {

                    val imageUrl = "https://acoustic.com/"
                    val url = URL(imageUrl)
                    val connection = Connection()
                    val httpClient = url.openConnection() as HttpURLConnection

                    connection.url = imageUrl
                    connection.initTime = Date().time
                    connection.statusCode = httpClient.responseCode
                    connection.responseDataSize = httpClient.contentLength.toLong()

                    Tealeaf.logConnection(connection)

                } catch (e: Exception) {
                    Tealeaf.logException(e)
                }
            }
            thread.start()
        }

        binding.contentConnections.connectionsConnectionWithErrorButton.setOnClickListener {
            val thread = Thread {
                try {
                    val imageUrl = "http://www.google.com/"
                    val url = URL(imageUrl)
                    val connection = Connection()
                    val httpClient = url.openConnection() as HttpURLConnection

                    connection.url = imageUrl
                    connection.initTime = Date().time
                    connection.statusCode = httpClient.responseCode
                    connection.responseDataSize = httpClient.contentLength.toLong()

                    Tealeaf.logConnection(connection)

                } catch (e: java.lang.Exception) {
                    Tealeaf.logException(e)
                }
            }
            thread.start()
        }

        return binding.root

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.setGroupVisible(R.id.group_toolbars, false)
        menu.setGroupVisible(R.id.group_search, false)
        menu.setGroupVisible(R.id.group_main, false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }
}