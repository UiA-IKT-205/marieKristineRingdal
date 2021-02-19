package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minutesToMilliSeconds

class MainActivity : AppCompatActivity() {

    lateinit var workTimer:CountDownTimer
    lateinit var pauseTimer:CountDownTimer
    lateinit var startButton:Button
    lateinit var countdownStatusText:TextView
    lateinit var countdownDisplay:TextView
    lateinit var pauseTimeDisplay:TextView
    lateinit var workTimeDisplay:TextView
    lateinit var selectCountdownTimeForWork:SeekBar
    lateinit var selectCountDownTimeForPause:SeekBar
    lateinit var repetitionsInput:EditText

    private var workTimerIsActive:Boolean = false
    private var pauseIsActive:Boolean = false

    private var workTimeToCountDownInMs = minutesToMilliSeconds(15)
    private var pauseTimeToCountdownInMs = minutesToMilliSeconds(15)
    private val timeTicks = 1000L

    private var amountOfRepetitions:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            startCountDown(it)
        }

        countdownDisplay = findViewById<TextView>(R.id.countDownView)
        pauseTimeDisplay = findViewById<TextView>(R.id.pauseTimeView)
        workTimeDisplay = findViewById<TextView>(R.id.workTimeView)
        countdownStatusText = findViewById(R.id.countdownStatusText)

        selectCountdownTimeForWork = findViewById<SeekBar>(R.id.countdownForWorkTimerSeekBar)
        selectCountdownTimeForWork.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val newCountDownTime = minutesToMilliSeconds(progress.toLong())
                workTimeDisplay.text = millisecondsToDescriptiveTime(newCountDownTime)
                workTimeToCountDownInMs = newCountDownTime

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not implemented
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not implemented
            }
        })

        selectCountDownTimeForPause = findViewById<SeekBar>(R.id.countdownForPauseTimerSeekBar)
        selectCountDownTimeForPause.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val newPauseTime =  minutesToMilliSeconds(progress.toLong())
                pauseTimeDisplay.text = millisecondsToDescriptiveTime(newPauseTime)
                pauseTimeToCountdownInMs = newPauseTime
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Not implemented
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Not implemented
            }
        })

        repetitionsInput = findViewById(R.id.amountOfRepetitionsEditText)

    }


    // Function that starts the countdown timer
    private fun startCountDown(v: View){
        if (workTimerIsActive){
            Toast.makeText(
                    this@MainActivity,
                    "Nedtelling pågår fortsatt",
                    Toast.LENGTH_SHORT).show()
            return
        }

        workTimer = object : CountDownTimer(workTimeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(
                        this@MainActivity,
                        "Pause",
                        Toast.LENGTH_SHORT).show()

                workTimerIsActive = false

                amountOfRepetitions = repetitionsInput.text.toString().toInt()
                if (amountOfRepetitions > 0){
                    startCountdownForPause(v)
                    amountOfRepetitions--
                    repetitionsInput.setText(amountOfRepetitions.toString())
                    countdownStatusText.text = "PAUSED FOR:"
                }

            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }

        }

        workTimer.start()
        workTimerIsActive = true
    }

    private fun startCountdownForPause(v: View){
        if(pauseIsActive){
            Toast.makeText(
                    this@MainActivity,
                    "Pausen pågår fortsatt",
                    Toast.LENGTH_SHORT).show()
            return
        }

        pauseTimer = object : CountDownTimer(pauseTimeToCountdownInMs, timeTicks){
            override fun onFinish() {
                Toast.makeText(
                        this@MainActivity,
                        "På tide å jobbe igjen!",
                        Toast.LENGTH_SHORT).show()
                pauseIsActive = false

                amountOfRepetitions = repetitionsInput.text.toString().toInt()
                if (amountOfRepetitions > 0){
                    workTimer.start()
                    countdownStatusText.text = resources.getText(R.string.countdownStatusText)
                }
                else{
                    workTimer.cancel()
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }

        pauseTimer.start()
        pauseIsActive = true
    }

    // Function that updates the countdown display
    fun updateCountDownDisplay(timeInMs:Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}