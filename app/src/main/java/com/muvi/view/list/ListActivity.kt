package com.muvi.view.list

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.muvi.R
import com.muvi.config.ContentType
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.view.detail.DetailActivity
import com.muvi.view.favourite.FavouriteActivity
import com.muvi.view.list.adapter.movie.OnListMovieClickListener
import com.muvi.view.list.adapter.tv.OnListTvClickListener
import com.muvi.view.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class ListActivity : AppCompatActivity(), OnListMovieClickListener, OnListTvClickListener {

    private val listActivityViewPagerAdapter by lazy {
        ListActivityViewPagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setComponent()
    }

    private fun setComponent() {
        viewPager.adapter = listActivityViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            val drawable: Drawable? = ContextCompat.getDrawable(
                this,
                ListActivityViewPagerAdapter.tabIcons[i]
            )
            drawable?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            tabLayout.getTabAt(i)?.icon = drawable
        }
    }

    private fun setListener() {
        favouriteButton.onClick { startActivity<FavouriteActivity>() }
        settingsButton.onClick { startActivity<SettingsActivity>() }
    }

    private fun unsetListener() {
        favouriteButton.setOnClickListener(null)
        settingsButton.setOnClickListener(null)
    }

    override fun onMovieItemClick(movie: DiscoverMovieListResult) {
        startActivity<DetailActivity>(
            "type" to ContentType.MOVIE,
            "movie" to movie,
            "openFrom" to this.javaClass.simpleName
        )
    }

    override fun onTvItemClick(tv: DiscoverTvListResult) {
        startActivity<DetailActivity>(
            "type" to ContentType.TV,
            "tv" to tv,
            "openFrom" to this.javaClass.simpleName
        )
    }

    override fun onBackPressed() {
        alert(resources.getString(R.string.dialog_confirm_exit_app)) {
            yesButton { super.onBackPressed() }
            noButton { }
        }.show()
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    override fun onStop() {
        super.onStop()
        unsetListener()
    }
}