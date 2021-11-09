package com.example.lab6_framents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.navigation_fragment.*

class NavigationFragment : Fragment() {
    private var frag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        frag = arguments?.getInt("frag") ?: 0
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater,
                          container: ViewGroup?,
                          savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.navigation_fragment, container, false)
        with (root) {
            n_text.text = (1..frag).fold("") { acc, i -> "$acc->$i" }
            n_button.setOnClickListener {
                childFragmentManager.apply {
                    beginTransaction()
                            .replace(R.id.main_fragment, NavigationFragment().apply {
                                arguments = Bundle().apply {
                                    putInt("frag", this@NavigationFragment.frag + 1)
                                }
                            })
                            .addToBackStack("")
                            .commit()
                }
            }
        }
        return root
    }
}