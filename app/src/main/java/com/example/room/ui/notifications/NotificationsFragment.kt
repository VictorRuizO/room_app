package com.example.room.ui.notifications

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.room.R

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_config, container, false)
        val dark: Switch = root.findViewById(R.id.dark)
        val aceptar: Button = root.findViewById(R.id.config_button_aceptar)
        val textIpBed: TextView = root.findViewById(R.id.conf_ip_bed)
        val textIpDesk: TextView = root.findViewById(R.id.config_ip_escritorio)
        val sharedPref = activity?.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        textIpBed.text = sharedPref!!.getString("IP_BED","192.168.0.1")
        textIpDesk.text = sharedPref.getString("IP_DESK","192.168.0.1")

        dark.isChecked = sharedPref.getString("DARK","0").equals("1")


        aceptar.setOnClickListener {
           

            sharedPref?.edit()?.putString("IP_DESK", textIpDesk.text.toString())?.apply()
            sharedPref?.edit()?.putString("IP_BED", textIpBed.text.toString())?.apply()
            var darkMode:String = if (dark.isChecked )
                "1"
            else
                "0"
            sharedPref?.edit()?.putString("DARK", darkMode)?.apply()

            if (dark.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }



        return root
    }
}