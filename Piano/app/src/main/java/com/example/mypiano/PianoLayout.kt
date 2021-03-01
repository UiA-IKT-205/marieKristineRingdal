package com.example.mypiano

import android.media.Image
import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mypiano.data.PianoSheet
import com.example.mypiano.databinding.FragmentPianoLayoutBinding
import kotlinx.android.synthetic.main.fragment_piano_layout.*
import kotlinx.android.synthetic.main.fragment_piano_layout.view.*
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path

class PianoLayout : Fragment() {

    private var _binding:FragmentPianoLayoutBinding? = null
    private val binding get() = _binding!!

    private val naturalTones = listOf("C", "D", "E", "F", "G", "A", "B", "C2", "D2", "E2", "F2", "G2", "A2", "B2")
    private val semiTones = listOf("C#", "D#", "F#", "G#", "A#", "C#2", "D#2", "F#2", "G#2", "A#2")

    private var pianoSheet:MutableList<PianoSheet> = mutableListOf<PianoSheet>()

    private var recordingIsActive:Boolean  = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoLayoutBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()


        naturalTones.forEach{ originalNoteValue ->
            val naturalTonePianoKey = WhitePianoKeyFragment.newInstance(originalNoteValue)
            var startTimeOfNote:Long = 0

            naturalTonePianoKey.onKeyPressed = { note ->
                startTimeOfNote = SystemClock.elapsedRealtime() - recordingTimeChronometer.base
                println("White piano key pressed: $note")
            }
            naturalTonePianoKey.onKeyReleased = {
                var noteTimeDuration = SystemClock.elapsedRealtime() - recordingTimeChronometer.base - startTimeOfNote
                val note = PianoSheet(it, startTimeOfNote, noteTimeDuration)
                pianoSheet.add(note)

                println("White piano key released: $note")
            }

            fragmentTransaction.add(view.whitePianoKeysLayout.id, naturalTonePianoKey, "note_$originalNoteValue")
        }


        semiTones.forEach { originalNoteValue ->
            val semiTonePianoKey = BlackPianoKeyFragment.newInstance(originalNoteValue)
            var startTimeOfNote:Long = 0

            semiTonePianoKey.onKeyPressed = { note ->
                startTimeOfNote = SystemClock.elapsedRealtime() - recordingTimeChronometer.base
                println("Black piano key pressed: $note")
            }
            semiTonePianoKey.onKeyReleased = {
                var noteTimeDuration = SystemClock.elapsedRealtime() - recordingTimeChronometer.base - startTimeOfNote
                val note = PianoSheet(it, startTimeOfNote, noteTimeDuration)
                pianoSheet.add(note)

                println("Black piano key released: $note")
            }
            fragmentTransaction.add(view.blackPianoKeysLayout.id, semiTonePianoKey, "note_$originalNoteValue")
        }
        fragmentTransaction.commit()


        view.startStopRecordingButton.setOnClickListener {
            if(!recordingIsActive){
                pianoSheet.clear()
                startRecordingTimer()
                startStopRecordingButton.text = "Stop REC"
            } else {
                stopRecordingTimer()
                startStopRecordingButton.text = "Reset REC"
            }
        }


        view.saveMusicSheetButton.setOnClickListener{
            var fileName = view.fileNameInput.text.toString()
            val filePath = this.activity?.getExternalFilesDir(null)
            val newMusicFile = (File(filePath, fileName))

            when {
                pianoSheet.count() == 0 -> Toast.makeText(activity, "Please enter some notes", Toast.LENGTH_SHORT).show()
                fileName.isEmpty() -> Toast.makeText(activity, "Please enter a file name", Toast.LENGTH_SHORT).show()
                filePath == null -> Toast.makeText(activity, "Path does not exist", Toast.LENGTH_SHORT).show()
                newMusicFile.exists() -> Toast.makeText(activity, "File already exists", Toast.LENGTH_SHORT).show()

                else -> {
                    fileName = "$fileName.music"
                    FileOutputStream(newMusicFile, true).bufferedWriter().use { writer ->
                        pianoSheet.forEach {
                            writer.write("${it.toString()}\n")
                        }
                    }
                    Toast.makeText(activity, "Your file has been saved!", Toast.LENGTH_SHORT).show()
                    pianoSheet.clear()
                    fileNameInput.text.clear()
                    FileOutputStream(newMusicFile).close()

                    println("Saved as: $fileName")
                    println("Saved at: $filePath/$fileName")
                }
            }
        }
        return view
    }


    private fun startRecordingTimer(){
        if (!recordingIsActive){
            recordingTimeChronometer.base = SystemClock.elapsedRealtime()
            recordingTimeChronometer.start()
            recordingIsActive = true
        }
    }

    private fun stopRecordingTimer(){
        if (recordingIsActive){
            recordingTimeChronometer.stop()
            recordingIsActive = false
        }
    }
}