/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.muvi.favourite.R
import com.muvi.favourite.view.adapter.recyclerview.movie.FavouriteListMovieAdapter
import com.muvi.favourite.view.adapter.recyclerview.tv.FavouriteListTvAdapter
import com.muvi.favourite.viewmodel.FavouriteListViewModel
import kotlinx.android.synthetic.main.fragment_activity_favourite_list.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavouriteListFragment : Fragment() {

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): FavouriteListFragment {
            return FavouriteListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private val favouriteListMovieAdapter by lazy { FavouriteListMovieAdapter() }
    private val favouriteListTvAdapter by lazy { FavouriteListTvAdapter() }
    private val favouriteListViewModel: FavouriteListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_activity_favourite_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (favouriteListViewModel.loaded) hideLoading() else showLoading()
        setRecyclerView()
        observeFavouriteMovies()
        observeFavouriteTvs()
    }

    private fun getFavouriteMovies() {
        favouriteListViewModel.getFavouriteMovies()
    }

    private fun getFavouriteTvs() {
        favouriteListViewModel.getFavouriteTvs()
    }

    private fun observeFavouriteMovies() {
        favouriteListViewModel.favouriteMovies.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                emptyListPlaceholderTitle.text =
                    getString(R.string.empty_favourite_movie_placeholder_title)
                emptyListPlaceholderDescription.text =
                    getString(R.string.empty_favourite_movie_placeholder_description)
                emptyListPlaceholder.visibility = View.VISIBLE
            } else {
                if (!favouriteListViewModel.loaded) favouriteListViewModel.loaded = true
                favouriteListMovieAdapter.setData(it, activity as FavouriteListActivity)
            }
            hideLoading()
        })
    }

    private fun observeFavouriteTvs() {
        favouriteListViewModel.favouriteTvs.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                emptyListPlaceholderTitle.text =
                    getString(R.string.empty_favourite_tv_placeholder_title)
                emptyListPlaceholderDescription.text =
                    getString(R.string.empty_favourite_tv_placeholder_description)
                emptyListPlaceholder.visibility = View.VISIBLE
            } else {
                if (!favouriteListViewModel.loaded) favouriteListViewModel.loaded = true
                favouriteListTvAdapter.setData(it, activity as FavouriteListActivity)
            }
            hideLoading()
        })
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        when (arguments?.getInt(ARG_SECTION_NUMBER) ?: 1) {
            1 -> {
                recyclerView.adapter = favouriteListMovieAdapter
                favouriteListMovieAdapter.notifyDataSetChanged()
            }
            2 -> {
                recyclerView.adapter = favouriteListTvAdapter
                favouriteListTvAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun showLoading() {
        loading.playAnimation()
        loading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        loading.pauseAnimation()
    }

    override fun onResume() {
        super.onResume()
        if ((arguments?.getInt(ARG_SECTION_NUMBER)
                ?: 1) == 1
        ) getFavouriteMovies() else getFavouriteTvs()
    }

    override fun onPause() {
        super.onPause()
        loading.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
    }
}