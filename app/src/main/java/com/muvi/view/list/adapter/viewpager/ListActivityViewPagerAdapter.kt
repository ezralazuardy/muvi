/*
 * Created by Ezra Lazuardy on 10/31/19, 12:23 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/20/19, 9:06 PM
 */

package com.muvi.view.list.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.muvi.R
import com.muvi.view.list.ListFragment

class ListActivityViewPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        val tabIcons = arrayOf(R.drawable.ic_movies_white, R.drawable.ic_tv_white)
    }

    override fun getItem(position: Int): Fragment =
        ListFragment.newInstance(position + 1)

    override fun getPageTitle(position: Int): CharSequence? = null

    override fun getCount(): Int = 2
}