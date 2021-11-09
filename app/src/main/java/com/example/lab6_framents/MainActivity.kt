package com.example.lab6_framents

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

const val KEY_CURR_FRAG = "fragment_tag"
const val KEY_QUEUE = "queue"
class MainActivity : AppCompatActivity() {
    private var currFragmentTag: String? = null
    private var queue: Deque<Int> = ArrayDeque()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) toFragment(R.id.search)
        else {
            currFragmentTag = savedInstanceState.getString(KEY_CURR_FRAG)
            queue = ArrayDeque(savedInstanceState.getIntegerArrayList(KEY_QUEUE)?:ArrayList())
        }
        navigation.setOnItemSelectedListener {
            toFragment(it.itemId)
            true
        }
    }

    private fun toFragment(fragmentId: Int) {
        val fragment = supportFragmentManager.findFragmentById(fragmentId) ?: Fragment()
        val transaction = supportFragmentManager.beginTransaction()
        val currFragment = supportFragmentManager.findFragmentByTag(currFragmentTag)
        if (currFragment != null) transaction.hide(currFragment)
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.main, fragment, "$fragmentId")
            queue.addLast(fragmentId)
        }
        currFragmentTag = fragment.tag
        transaction.commit()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(KEY_CURR_FRAG, currFragmentTag)
        outState.putIntegerArrayList(KEY_QUEUE, ArrayList(queue))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.swipes or R.id.my_page or R.id.search -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(currFragmentTag) ?: error("Error")
        if (fragment.childFragmentManager.backStackEntryCount != 0) {
            fragment.childFragmentManager.popBackStack()
        } else {
            if (queue.isEmpty()) {
                finish()
            } else {
                val last = queue.pop()
                navigation.selectedItemId = last
                toFragment(last)
            }
        }
    }
}