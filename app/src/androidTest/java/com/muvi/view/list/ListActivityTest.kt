package com.muvi.view.list

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.muvi.R
import com.muvi.utils.AndroidTestUtils.withIndex
import org.junit.Rule
import org.junit.Test

class ListActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule<ListActivity>(ListActivity::class.java)

    private val viewPager = onView(withId(R.id.viewPager))

    @Test
    fun checkViewPager() {
        viewPager.check(matches(isDisplayed()))
    }

    @Test
    fun loadMovieList() {
        onView(withIndex(withId(R.id.recyclerViewFragmentActivityList), 0)).run {
            check(matches(isDisplayed()))
//                check(RecyclerViewItemCountAssertion(20))
//                activityTestRule.activity.find<RecyclerView>(R.id.recyclerViewFragmentActivityList).adapter?.let {
//                    perform(scrollToPosition<ListMovieAdapter.ViewHolder>(it.itemCount - 1))
//                    perform(scrollToPosition<ListMovieAdapter.ViewHolder>(0))
//                }
        }
    }

//    @Test
//    fun loadTvList() {
//        testSwipeToTvViewPager()
//        pause(5000)
//        onView(withIndex(withId(R.id.recyclerViewFragmentActivityList), 1)).run {
//            check(matches(isDisplayed()))
////            check(RecyclerViewItemCountAssertion(20))
//            activityTestRule.activity.find<RecyclerView>(R.id.recyclerViewFragmentActivityList).adapter?.let {
//                perform(scrollToPosition<ListMovieAdapter.ViewHolder>(it.itemCount - 1))
//                perform(scrollToPosition<ListMovieAdapter.ViewHolder>(0))
//            }
//        }
//    }

//    private fun testSwipeToTvViewPager() = viewPager.perform(scrollToPage(1, true))
}