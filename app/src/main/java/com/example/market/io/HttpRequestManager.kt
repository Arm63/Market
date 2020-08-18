package com.example.market.io

import com.example.market.util.Constant
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object HttpRequestManager {
    fun executeRequest(
        apiUrl: String?,
        requestMethod: String?,
        postData: String?
    ): HttpURLConnection? {
        var connection: HttpURLConnection? = null
        try {
            val ulr = URL(apiUrl)
            connection = ulr.openConnection() as HttpURLConnection
            connection.requestMethod = requestMethod
            connection!!.useCaches = false
            when (requestMethod) {
                Constant.RequestMethod.PUT, Constant.RequestMethod.POST -> {
                    connection.setRequestProperty("Content-Type", "application/json")
                    connection.doInput = true
                    connection.doOutput = true
                    connection.connect()
                    val outputStream = connection.outputStream
                    outputStream.write(
                        if (postData != null)
                            postData.toByteArray() else byteArrayOf(
                            0
                        )
                    )
                    outputStream.flush()
                    outputStream.close()
                }
                Constant.RequestMethod.GET -> connection.connect()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return connection
    }
}