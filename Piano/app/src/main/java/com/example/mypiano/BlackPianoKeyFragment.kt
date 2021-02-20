package com.example.mypiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.mypiano.databinding.FragmentBlackPianoKeyBinding
import com.example.mypiano.databinding.FragmentWhitePianoKeyBinding
import kotlinx.android.synthetic.main.fragment_black_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_white_piano_key.view.*

class BlackPianoKeyFragment : Fragment() {

    private var _binding:FragmentBlackPianoKeyBinding? = null
    private val binding get() = _binding!!

    private lateinit var note:String

    var onKeyPressed:((note:String) -> Unit)? = null
    var onKeyReleased:((note:String) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            note = it.getString("NOTE") ?: "?"
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBlackPianoKeyBinding.inflate(inflater)
        val view = binding.root

        view.blackPianoKeyButton.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN -> this@BlackPianoKeyFragment.onKeyPressed?.invoke(note)
                    MotionEvent.ACTION_UP -> this@BlackPianoKeyFragment.onKeyReleased?.invoke(note)
                }
                return true
            }
        })

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(note: String) =
            BlackPianoKeyFragment().apply {
                arguments = Bundle().apply {
                    putString("NOTE", note)
                }
            }
    }
}