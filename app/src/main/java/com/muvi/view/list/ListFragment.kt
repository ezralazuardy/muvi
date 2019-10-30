/*
 * Created by Ezra Lazuardy on 10/31/19, 12:23 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/31/19, 12:22 AM
 */

package com.muvi.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.muvi.R
import com.muvi.config.ContentType
import com.muvi.view.list.adapter.recyclerview.movie.ListMovieAdapter
import com.muvi.view.list.adapter.recyclerview.tv.ListTvAdapter
import com.muvi.viewmodel.list.ListViewModel
import kotlinx.android.synthetic.main.fragment_activity_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.koin.android.viewmodel.ext.android.viewModel

class ListFragment : Fragment(), AnkoLogger {

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): ListFragment =
            ListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
    }

    private val listMovieAdapter by lazy { ListMovieAdapter() }
    private val listTvAdapter by lazy { ListTvAdapter() }
    private val listViewModel: ListViewModel by viewModel()
    private lateinit var onFragmentChangeListener: OnFragmentChangeListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            onFragmentChangeListener = activity as OnFragmentChangeListener
        } catch (e: ClassCastException) {
            throw ClassCastException(e.message + " | " + activity.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_activity_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (listViewModel.loaded) hideLoading() else showLoading()
        setRecyclerView()
        if ((arguments?.getInt(ARG_SECTION_NUMBER) ?: 1) == 1) getMovies() else getTvs()
    }

    private fun getMovies() {
        listViewModel.movieGenres.observe(viewLifecycleOwner, Observer { genres ->
            listViewModel.discoverMovies.observe(viewLifecycleOwner, Observer {
                if (!listViewModel.loaded) listViewModel.loaded = true
                if (!it.isNullOrEmpty())
                    listMovieAdapter.setData(it, genres, activity as ListActivity)
                else
                    showError("MOVIE LIST EMPTY")
                hideLoading()
            })
        })
    }

    private fun getTvs() {
        listViewModel.tvGenres.observe(viewLifecycleOwner, Observer { genres ->
            listViewModel.discoverTvs.observe(viewLifecycleOwner, Observer {
                if (!listViewModel.loaded) listViewModel.loaded = true
                if (!it.isNullOrEmpty())
                    listTvAdapter.setData(it, genres, activity as ListActivity)
                else
                    showError("TV LIST EMPTY")
                hideLoading()
            })
        })
    }

    private fun setRecyclerView() {
        recyclerViewFragmentActivityList.layoutManager = LinearLayoutManager(context)
        recyclerViewFragmentActivityList.setHasFixedSize(true)
        when (arguments?.getInt(ARG_SECTION_NUMBER) ?: 1) {
            1 -> {
                recyclerViewFragmentActivityList.adapter = listMovieAdapter
                listMovieAdapter.notifyDataSetChanged()
            }
            2 -> {
                recyclerViewFragmentActivityList.adapter = listTvAdapter
                listTvAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showError(log: String) {
        error { log }
    }

    private fun showLoading() {
        loadingActivityList.playAnimation()
        loadingActivityList.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingActivityList.visibility = View.GONE
        loadingActivityList.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        if (arguments?.getInt(ARG_SECTION_NUMBER) ?: 1 == 1) onFragmentChangeListener.onFragmentChange(ContentType.MOVIE) else onFragmentChangeListener.onFragmentChange(
            ContentType.TV
        )
    }

    override fun onPause() {
        super.onPause()
        loadingActivityList.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerViewFragmentActivityList.adapter = null
    }

    interface OnFragmentChangeListener {

        fun onFragmentChange(contentType: ContentType)
    }
}