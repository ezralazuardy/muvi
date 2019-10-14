/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.muvi.R
import com.muvi.view.favourite.adapter.recyclerview.movie.FavouriteMovieAdapter
import com.muvi.view.favourite.adapter.recyclerview.tv.FavouriteTvAdapter
import com.muvi.viewmodel.favourite.FavouriteViewModel
import kotlinx.android.synthetic.main.fragment_activity_favourite.*
import org.koin.android.viewmodel.ext.android.viewModel

class FavouriteFragment : Fragment() {

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): FavouriteFragment {
            return FavouriteFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private val favouriteMovieAdapter by lazy { FavouriteMovieAdapter() }
    private val favouriteTvAdapter by lazy { FavouriteTvAdapter() }
    private val favouriteViewModel: FavouriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_activity_favourite, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (favouriteViewModel.loaded) hideLoading() else showLoading()
        setRecyclerView()
        observeFavouriteMovies()
        observeFavouriteTvs()
    }

    private fun getFavouriteMovies() {
        favouriteViewModel.getFavouriteMovies()
    }

    private fun getFavouriteTvs() {
        favouriteViewModel.getFavouriteTvs()
    }

    private fun observeFavouriteMovies() {
        favouriteViewModel.favouriteMovies.observe(viewLifecycleOwner, Observer {
            if (!favouriteViewModel.loaded) favouriteViewModel.loaded = true
            favouriteMovieAdapter.setData(it, activity as FavouriteActivity)
            if (it.isNullOrEmpty()) {
                emptyListPlaceholderTitle.text =
                    getString(R.string.empty_favourite_movie_placeholder_title)
                emptyListPlaceholderDescription.text =
                    getString(R.string.empty_favourite_movie_placeholder_description)
                emptyListPlaceholder.visibility = View.VISIBLE
            }
            hideLoading()
        })
    }

    private fun observeFavouriteTvs() {
        favouriteViewModel.favouriteTvs.observe(viewLifecycleOwner, Observer {
            if (!favouriteViewModel.loaded) favouriteViewModel.loaded = true
            favouriteTvAdapter.setData(it, activity as FavouriteActivity)
            if (it.isNullOrEmpty()) {
                emptyListPlaceholderTitle.text =
                    getString(R.string.empty_favourite_tv_placeholder_title)
                emptyListPlaceholderDescription.text =
                    getString(R.string.empty_favourite_tv_placeholder_description)
                emptyListPlaceholder.visibility = View.VISIBLE
            }
            hideLoading()
        })
    }

    private fun setRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        when (arguments?.getInt(ARG_SECTION_NUMBER) ?: 1) {
            1 -> {
                recyclerView.adapter = favouriteMovieAdapter
                favouriteMovieAdapter.notifyDataSetChanged()
            }
            2 -> {
                recyclerView.adapter = favouriteTvAdapter
                favouriteTvAdapter.notifyDataSetChanged()
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