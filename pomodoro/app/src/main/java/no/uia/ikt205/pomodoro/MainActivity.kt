package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var coutdownDisplay:TextView
    lateinit var thirty:Button
    lateinit var sixty:Button
    lateinit var ninety:Button
    lateinit var hundredtwenty:Button

    private var timerRunning: Boolean = false

    private var timeToCountDownInMs = 5000L
    private val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       startButton = findViewById<Button>(R.id.startCountdownButton)
       startButton.setOnClickListener(){
           startCountDown(it)
       }

        // Funksjon for 30 sekunders nedtelling
        thirty = findViewById<Button>(R.id.thirtySeconds)
        thirty.setOnClickListener(){
            // Setter nedregningen til 1800000 ms
            timeToCountDownInMs = 1800000L
            // Dersom nedtellingen pågår skal ikke nedtellingen endres
            if (timerRunning){
                Toast.makeText(this@MainActivity,"Nedtelling pågår fortsatt", Toast.LENGTH_SHORT).show()
            }
            // Oppdater tiden i nedtellingsdisplayet
            else
                updateCountDownDisplay(timeToCountDownInMs)
        }

        // Funksjon for 60 sekunders nedtelling
        sixty = findViewById<Button>(R.id.sixtySeconds)
        sixty.setOnClickListener(){
            timeToCountDownInMs = 3600000L
            if (timerRunning){
                Toast.makeText(this@MainActivity,"Nedtelling pågår fortsatt", Toast.LENGTH_SHORT).show()
            } else
            updateCountDownDisplay(timeToCountDownInMs)
        }

        // Funksjon for 90 sekunders nedtelling
        ninety = findViewById<Button>(R.id.ninetySeconds)
        ninety.setOnClickListener(){
            timeToCountDownInMs = 5400000L
            if (timerRunning){
                Toast.makeText(this@MainActivity,"Nedtelling pågår fortsatt", Toast.LENGTH_SHORT).show()
            } else
            updateCountDownDisplay(timeToCountDownInMs)
        }

        // Funksjon for 120 sekunders nedtelling
        hundredtwenty = findViewById<Button>(R.id.hundredtwentySeconds)
        hundredtwenty.setOnClickListener(){
            timeToCountDownInMs = 7200000
            if (timerRunning){
                Toast.makeText(this@MainActivity,"Nedtelling pågår fortsatt", Toast.LENGTH_SHORT).show()
            } else
            updateCountDownDisplay(timeToCountDownInMs)
        }

       coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    private fun startCountDown(v: View){
        // Dersom en nedtelling pågår skal nedtellingen ikke forandres
        if (timerRunning){
            Toast.makeText(this@MainActivity,"Nedtelling pågår fortsatt", Toast.LENGTH_SHORT).show()
            return
        }

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                timerRunning = false
            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }

        }

        timer.start()
        timerRunning = true
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}