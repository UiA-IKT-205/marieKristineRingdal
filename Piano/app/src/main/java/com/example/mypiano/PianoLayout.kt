package com.example.mypiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mypiano.databinding.FragmentPianoLayoutBinding
import kotlinx.android.synthetic.main.fragment_piano_layout.view.*

class PianoLayout : Fragment() {

    private var _binding:FragmentPianoLayoutBinding? = null
    private val binding get() = _binding!!

    private val naturalTones = listOf("C", "D", "E", "F", "G", "A", "B",
        "C2", "D2", "E2", "F2", "G2", "A2", "B2")
    private val semiTones = listOf("C#", "D#", "F#", "G#", "A#", "C#2", "D#2", "F#2", "G#2", "A#2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoLayoutBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        naturalTones.forEach{
            val naturalTonePianoKey = WhitePianoKeyFragment.newInstance(it)

            naturalTonePianoKey.onKeyPressed = {
                println("White piano key pressed: $it")
            }
            naturalTonePianoKey.onKeyReleased = {
                println("White piano key released: $it")
            }

            fragmentTransaction.add(view.whitePianoKeysLayout.id, naturalTonePianoKey, "note_$it")
        }

        semiTones.forEach {
            val semiTonePianoKey = BlackPianoKeyFragment.newInstance(it)

            semiTonePianoKey.onKeyPressed = {
                println("Black piano key pressed: $it")
            }
            semiTonePianoKey.onKeyReleased = {
                println("Black piano key released: $it")
            }

            fragmentTransaction.add(view.blackPianoKeysLayout.id, semiTonePianoKey, "note_$it")
        }

        fragmentTransaction.commit()
        return view
    }

}