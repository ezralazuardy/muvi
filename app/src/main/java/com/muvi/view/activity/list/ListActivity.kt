package com.muvi.view.activity.list

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.muvi.R
import com.muvi.view.activity.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class ListActivity : AppCompatActivity() {

    private var listActivityViewPagerAdapter: ListActivityViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setComponent()
    }

    private fun setComponent() {
        if (listActivityViewPagerAdapter == null) listActivityViewPagerAdapter =
            ListActivityViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = listActivityViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            val drawable: Drawable? = ContextCompat.getDrawable(
                this@ListActivity,
                ListActivityViewPagerAdapter.tabIcons[i]
            )
            drawable?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            tabLayout.getTabAt(i)?.icon = drawable
        }
        settingsButton.setOnClickListener { startActivity<SettingsActivity>() }
    }

    override fun onBackPressed() {
        alert(resources.getString(R.string.dialog_confirm_exit_app)) {
            yesButton { super.onBackPressed() }
            noButton { }
        }.show()
    }
}