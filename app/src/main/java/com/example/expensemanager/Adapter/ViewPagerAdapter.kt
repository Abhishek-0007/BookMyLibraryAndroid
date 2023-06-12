package com.example.expensemanager.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.expensemanager.ui.VIewPagerGenericFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return VIewPagerGenericFragment(1)
            1 -> return VIewPagerGenericFragment(2)
            2 -> return VIewPagerGenericFragment(3)
        }
        return VIewPagerGenericFragment(1)
    }

}