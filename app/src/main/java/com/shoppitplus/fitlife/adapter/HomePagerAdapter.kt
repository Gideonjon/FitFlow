package com.shoppitplus.fitlife.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shoppitplus.fitlife.CallHistory
import com.shoppitplus.fitlife.ContactsFragment
import com.shoppitplus.fitlife.routines.Excercise

class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ContactsFragment()
            1 -> CallHistory()
            else -> Excercise()
        }
    }
}