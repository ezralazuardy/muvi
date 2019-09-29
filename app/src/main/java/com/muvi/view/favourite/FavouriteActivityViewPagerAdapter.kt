package com.muvi.view.favourite

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.muvi.R

class FavouriteActivityViewPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        val tabIcons = arrayOf(R.drawable.ic_movies_white, R.drawable.ic_tv_white)
    }

    override fun getItem(position: Int): Fragment = FavouriteFragment.newInstance(position + 1)

    override fun getPageTitle(position: Int): CharSequence? = null

    override fun getCount(): Int = 2
}