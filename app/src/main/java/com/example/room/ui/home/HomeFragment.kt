package com.example.room.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.room.Conector
import com.example.room.R
import com.example.room.ui.dashboard.DashboardViewModel
import com.example.room.ui.sincronizar
import com.larswerkman.holocolorpicker.ColorPicker

class HomeFragment : Fragment(), sincronizar {

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var picker : ColorPicker
    private lateinit var statusIcon : ImageView
    private lateinit var sliderBrillo : SeekBar
    private lateinit var sliderVel : SeekBar
    private var status : Boolean = false
    private var animac : Int = 0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_bed, container, false)

        picker  = root.findViewById(R.id.bed_picker)
        statusIcon  = root.findViewById(R.id.bed_switch)
        val blanco : ImageButton = root.findViewById(R.id.bed_blanco)
        val azul : ImageButton = root.findViewById(R.id.bed_azul)
        val rojo : ImageButton = root.findViewById(R.id.bed_rojo)
        val verde : ImageButton = root.findViewById(R.id.bed_verde)
        val morado : ImageButton = root.findViewById(R.id.bed_morado)
        sliderBrillo  = root.findViewById(R.id.bed_brillo)
        sliderVel  = root.findViewById(R.id.bed_velocidad)
        val solid : Button = root.findViewById(R.id.bed_solid)
        val fade : Button = root.findViewById(R.id.bed_fade)
        val rainbow : Button = root.findViewById(R.id.bed_rainbow)
        val changeColor : Button = root.findViewById(R.id.bed_change_color)
        val fadeColor : Button = root.findViewById(R.id.bed_fade_color)
        val conector = Conector(root.context)
        val device = "bed"
        var progresBrillo =0
        var progresVel =0
        status  =false
        conector.sinc(device,this)
        picker.showOldCenterColor = false

        blanco.setOnClickListener {
            conector.send("c.255.255.255",device)
            if (animac!=0) conector.send("a.0",device)
            animac = 0
        }
        azul.setOnClickListener {
            conector.send("c.0.0.255",device)
            if (animac!=0) conector.send("a.0",device)
            animac = 0
        }
        rojo.setOnClickListener {
            conector.send("c.255.0.0",device)
            if (animac!=0) conector.send("a.0",device)
            animac = 0
        }
        verde.setOnClickListener {
            conector.send("c.0.255.0",device)
            if (animac!=0) conector.send("a.0",device)
            animac = 0
        }
        morado.setOnClickListener {
            conector.send("c.120.40.140",device)
            if (animac!=0) conector.send("a.0",device)
            animac = 0
        }

        solid.setOnClickListener {
            conector.send("a.0",device)
            animac = 0
        }
        fade.setOnClickListener {
            conector.send("a.1",device)
            animac = 1
        }
        rainbow.setOnClickListener {
            conector.send("a.2",device)
            animac = 2
        }
        changeColor.setOnClickListener {
            conector.send("a.3",device)
            animac = 3
        }
        fadeColor.setOnClickListener {
            conector.send("a.4",device)
            animac = 4
        }

        sliderBrillo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { progresBrillo=progress}

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                conector.send("b."+(10+245*progresBrillo/100).toInt(),device)
            }

        } )

        sliderVel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) { progresVel=progress}

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                conector.send("r."+(100-90*progresVel/100).toInt(),device)
            }

        } )

        statusIcon.setOnClickListener {
            if (status){
                status=false
                statusIcon.setImageResource(R.drawable.ic_ligth_off)
                conector.send("s.0",device)
            }else{
                status=true
                statusIcon.setImageResource(R.drawable.ic_off)
                conector.send("s.1",device)
            }
        }

        picker.setOnColorSelectedListener {
            conector.send("c."+picker.color.red+"."+picker.color.green+"."+picker.color.blue,device)
        }
        return root
    }

    override fun actualizar(params: IntArray) {
        super.actualizar(params)

        sliderBrillo.progress = (params[1].toInt()-10)*100/245
        sliderVel.progress = ((100-params[2].toInt())*100/90)
        animac = params[0]

        if (params[3]==1) {
            status = true
            statusIcon.setImageResource(R.drawable.ic_off)
        }
        else {
            status = false
            statusIcon.setImageResource(R.drawable.ic_ligth_off)
        }

        picker.color = Color.rgb(params[4],params[5],params[6])
    }
}