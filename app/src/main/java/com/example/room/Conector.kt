package com.example.room

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.room.ui.sincronizar


class Conector(context:Context) {

    private val context = context

    private  val sharedPref = context?.getSharedPreferences(
            "data", Context.MODE_PRIVATE)
    private val ip_bed = "http://"+sharedPref!!.getString("IP_BED","192.168.0.1")+"/"
    private val ip_desk = "http://"+sharedPref!!.getString("IP_DESK","192.168.0.1")+"/"

    fun send(data: String, device:String){
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(context)
        val url  = if(device == "desk")
            "$ip_desk$device?message=$data"
        else if(device == "bed")
            "$ip_bed$device?message=$data"
        else ""

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.POST, url.toString(),
                { response ->
                    // Display the first 500 characters of the response string.
                    //Log.d("success", response)
                },
                {
                })


        //Log.d("send", url.toString())
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    fun sinc(device:String, s:sincronizar){
        val params = IntArray(7)
        val queue = Volley.newRequestQueue(context)
        val url  = if(device == "desk")
            "$ip_desk$device?message=GET"
        else if(device == "bed")
            "$ip_bed$device?message=GET"
        else ""
        val stringRequest = StringRequest(
                Request.Method.POST, url.toString(),
                { response ->
                    // Display the first 500 characters of the response string.
                    //Log.d("success", response)
                    val parts = response.split(".")
                    for (i in parts.indices)
                        params[i]=parts[i].toInt()
                    s.actualizar(params)
                },
                {
                })


        //Log.d("send", url.toString())
        // Add the request to the RequestQueue.
        queue.add(stringRequest)

    }
}