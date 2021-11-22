package com.example.lab6_framents

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.navigation_fragment.*
import kotlinx.android.synthetic.main.navigation_fragment.view.*
import java.util.*

class NavigationFragment : Fragment() {
    private var depth: Int = 0
    private val KEY_DEPTH = "depth"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt(KEY_DEPTH)?.let { depth = it }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_fragment, container, false).apply {
            d_text.text = (1..depth).fold("0") { acc, i -> "$acc -> $i" }
            val new_frag = NavigationFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_DEPTH, this@NavigationFragment.depth + 1)
                }
            }
            d_button.setOnClickListener {
                requireFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_container, new_frag)
                        .addToBackStack(null)
                        .commit()
            }
        }
    }
}