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
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.acoustic.tealeafkitchensink.databinding.FragmentConnectionsBinding
import com.tl.uic.Tealeaf
import com.tl.uic.model.Connection
import com.tl.uic.util.TLFConnectionUtil
import com.tl.uic.util.TLFConnectionUtil.openConnection
import okio.ByteString
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
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

        val menuHost: MenuHost = host as MenuHost
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.contentConnections.connectionsConnectionWithoutErrorButton.setOnClickListener {
            val thread = Thread {
                try {

                    val imageUrl = "https://acoustic.com/"
                    val url = URL(imageUrl)
                    val connection = Connection()

                    connection.url = imageUrl
                    connection.initTime = Date().time

                    val httpClient = url.openConnection() as HttpURLConnection

                    connection.statusCode = httpClient.responseCode
                    connection.responseDataSize = httpClient.contentLength.toLong()
                    connection.headers = httpClient.headerFields
                    connection.cookies = Tealeaf.getTLCookie()

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

                    connection.url = imageUrl
                    connection.initTime = Date().time

                    val httpClient = url.openConnection() as HttpURLConnection

                    connection.statusCode = httpClient.responseCode
                    connection.responseDataSize = httpClient.contentLength.toLong()

                    Tealeaf.logConnection(connection)

                } catch (e: java.lang.Exception) {
                    Tealeaf.logException(e)
                }
            }
            thread.start()
        }

        binding.contentConnections.connectionsConnectionHttpUrlButton.setOnClickListener {
            val thread = Thread {
                try {
                    val siteUrl = "https://acoustic.com"

                    //example of automatically opening an http connection and logging the Connection properties
                    val clientObject: Array<Any> = openConnection(
                        context,
                        siteUrl,
                        TLFConnectionUtil.ConnectionType.URL,
                        TLFConnectionUtil.ResponseType.DEFAULT,
                        null
                    )

                    //example of updating the connection properties using the currently open http connection
                    //this is only needed if the app needs to modify/update specific data values
                    //otherwise, the above openConnection method is the only piece of code needed
                    //to re-iterate, the following lines of code are optional
                    val connection = TLFConnectionUtil.getConnection()
                    //example of updating an existing property
                    connection.loadTime = 50
                    //calculate approximate response time
                    connection.responseTime = Date().time - connection.initTime
                    //will need to manually make another call  after updating properties
                    TLFConnectionUtil.setConnection(connection)

                    Log.i("TESTING", connection.headers.toString())
                    Log.i("TESTING", connection.payload)
                    Log.i("TESTING", connection.cookies)

                    //example of how the response data can be extracted
                    val httpClient = clientObject[0] as HttpURLConnection
                    val `in` = BufferedReader(InputStreamReader(httpClient.inputStream))
                    var inputLine: String?
                    while (`in`.readLine().also { inputLine = it } != null) {
                        Log.i("TESTING", inputLine!!)
                    }
                    `in`.close()

                } catch (e: Exception) {
                    Tealeaf.logException(e)
                }
            }
            thread.start()
        }

        binding.contentConnections.connectionsConnectionOkhttpButton.setOnClickListener {
            val thread = Thread {
                try {
                    val urlOKHttp = "https://jsonplaceholder.typicode.com/todos/1"
                    try {

                        //example of automatically opening an OKHttp connection, logging the Connection properties, and returning the Response as a string
                        val test1: Array<Any> = openConnection(
                            context,
                            urlOKHttp,
                            TLFConnectionUtil.ConnectionType.OKHTTP,
                            TLFConnectionUtil.ResponseType.STRING,
                            null
                        )
                        val stringResponse = test1[0] as String
                        Log.i("TESTING", "OKHttp String Response = $stringResponse")

                        //example of automatically opening an OKHttp connection, logging the Connection properties, and returning the Response in Bytes
                        val test2: Array<Any> = openConnection(
                            context,
                            urlOKHttp,
                            TLFConnectionUtil.ConnectionType.OKHTTP,
                            TLFConnectionUtil.ResponseType.BYTES,
                            null
                        )
                        val byteResponse = test2[0] as ByteString
                        Log.i("TESTING", "OKHttp Bytes Response = $byteResponse")

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } catch (e: Exception) {
                    Tealeaf.logException(e)
                }
            }
            thread.start()
        }

        binding.contentConnections.connectionsConnectionVolleyButton.setOnClickListener {
            val urlVolley = "https://jsonplaceholder.typicode.com/posts/"
            try {
                openConnection(
                    context,
                    urlVolley,
                    TLFConnectionUtil.ConnectionType.VOLLEY,
                    TLFConnectionUtil.ResponseType.DEFAULT
                ) { response ->
                    Log.i(
                        "TESTING",
                        "Volley Response = $response"
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return binding.root

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
}