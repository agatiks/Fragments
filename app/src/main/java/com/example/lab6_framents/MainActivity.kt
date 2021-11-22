package com.example.lab6_framents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

const val KEY_CURR_FRAG = "fragment_tag"
const val KEY_CURR_FRAG_ID = "fragment_id"
const val KEY_QUEUE = "queue"

private const val TAB_STATE_KEY: String = "MainActivity_queue"
class MainActivity : AppCompatActivity() {
    private lateinit var queue: LinkedList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState == null) {
            queue = LinkedList()
            selectTab(R.id.search)
        } else {
            queue = LinkedList(savedInstanceState.getStringArrayList(TAB_STATE_KEY)!!)
            selectTab(queue.first.toInt())
        }

        main_navigation?.setNavigationItemSelectedListener(::selectItem)
        main_bottom_navigation?.setOnItemSelectedListener(::selectItem)
    }

    private fun selectItem(menuItem: MenuItem): Boolean {
        selectTab(menuItem.itemId)
        return true
    }

    private fun selectTab(tabInd: Int) {
        selectTabFragment("$tabInd")
        main_navigation?.menu?.forEach { it.isChecked = false }
        val menuItem = (main_navigation?.menu ?: main_bottom_navigation?.menu)!!.findItem(tabInd)
        menuItem.isChecked = true
    }

    private fun selectTabFragment(fragmentTag: String) {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag) ?: MainFragment()
        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.findFragmentByTag(queue.firstOrNull())?.let {
            if (it.childFragmentManager.backStackEntryCount == 0) {
                queue.removeFirst()
                transaction.remove(it)
            } else {
                transaction.hide(it)
            }
        }

        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.main_fragment_container, fragment, fragmentTag)
        }

        queue.apply {
            remove(fragmentTag)
            addFirst(fragmentTag)
        }
        transaction.commit()
    }

    override fun onBackPressed() {
        if (queue.isEmpty()) finish()
        val currentFragmentTag = queue.first
        val fragment = supportFragmentManager.findFragmentByTag(currentFragmentTag)
        if (fragment == null) {
            super.onBackPressed()
            return
        }
        val manager = fragment.childFragmentManager
        if (manager.backStackEntryCount == 0) {
            if (queue.size <= 1) finish()
            else {
                supportFragmentManager
                        .beginTransaction()
                        .remove(fragment)
                        .commit()
                queue.removeFirst()
                selectTab(queue.first.toInt())
            }
        } else {
            manager.popBackStack()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList(KEY_QUEUE, ArrayList(queue))
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}