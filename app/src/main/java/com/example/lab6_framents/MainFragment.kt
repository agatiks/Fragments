package com.example.lab6_framents

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class MainFragment : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.main_fragment, container, false)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                    .add(R.id.main_container, NavigationFragment())
                    .commit()
        }
        return root
    }
}