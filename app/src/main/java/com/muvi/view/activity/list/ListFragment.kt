package com.muvi.view.activity.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.muvi.R
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre
import com.muvi.view.activity.detail.DetailActivity
import com.muvi.view.adapter.movie.MovieListAdapter
import com.muvi.view.adapter.movie.OnMovieClickListener
import com.muvi.view.adapter.tv.OnTvClickListener
import com.muvi.view.adapter.tv.TvListAdapter
import com.muvi.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_activity_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity

class ListFragment : Fragment(), AnkoLogger, OnMovieClickListener, OnTvClickListener {

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): ListFragment {
            return ListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private lateinit var listViewModel: ListViewModel
    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var tvListAdapter: TvListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listViewModel = ViewModelProviders.of(this@ListFragment).get(ListViewModel::class.java)
        listViewModel.apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_activity_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        listViewModel.apply {
            requestGenreList(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
            requestDiscover(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        if (!listViewModel.isLoaded()) showLoading()
        when (arguments?.getInt(ARG_SECTION_NUMBER) ?: 1) {
            1 -> listViewModel.movieGenreList.observe(viewLifecycleOwner, Observer { genres ->
                listViewModel.discoverMovieList.observe(viewLifecycleOwner, Observer { movies ->
                    listViewModel.setLoaded()
                    if (!movies.isNullOrEmpty()) onMovieListLoaded(movies, genres)
                    else showError("MOVIE LIST EMPTY")
                })
            })
            2 -> listViewModel.tvGenreList.observe(viewLifecycleOwner, Observer { genres ->
                listViewModel.discoverTvList.observe(viewLifecycleOwner, Observer { tvs ->
                    listViewModel.setLoaded()
                    if (!tvs.isNullOrEmpty()) onTvListLoaded(tvs, genres)
                    else showError("TV LIST EMPTY")
                })
            })
        }
    }

    override fun onPause() {
        super.onPause()
        if (loadingActivityList.isAnimating) loadingActivityList.pauseAnimation()
    }

    private fun setRecyclerView() {
        recyclerViewFragmentActivityList.layoutManager = LinearLayoutManager(context)
        recyclerViewFragmentActivityList.setHasFixedSize(true)
        when (arguments?.getInt(ARG_SECTION_NUMBER) ?: 1) {
            1 -> {
                movieListAdapter = MovieListAdapter()
                recyclerViewFragmentActivityList.adapter = movieListAdapter
                movieListAdapter.notifyDataSetChanged()
            }
            2 -> {
                tvListAdapter = TvListAdapter()
                recyclerViewFragmentActivityList.adapter = tvListAdapter
                tvListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun onMovieListLoaded(movies: List<DiscoverMovieListResult>, genres: List<Genre>) {
        movieListAdapter.setData(movies, genres, this@ListFragment)
        hideLoading()
    }

    private fun onTvListLoaded(tvs: List<DiscoverTvListResult>, genres: List<Genre>) {
        tvListAdapter.setData(tvs, genres, this@ListFragment)
        hideLoading()
    }

    override fun onMovieItemClick(movie: DiscoverMovieListResult) {
        context?.startActivity<DetailActivity>("type" to MovieListAdapter.type, "movie" to movie)
    }

    override fun onTvItemClick(tv: DiscoverTvListResult) {
        context?.startActivity<DetailActivity>("type" to TvListAdapter.type, "tv" to tv)
    }

    private fun showError(log: String) {
        error { log }
    }

    private fun showLoading() {
        if (!loadingActivityList.isAnimating) loadingActivityList.playAnimation()
        loadingActivityList.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingActivityList.visibility = View.GONE
        if (loadingActivityList.isAnimating) loadingActivityList.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerViewFragmentActivityList.adapter = null
        when (arguments?.getInt(ARG_SECTION_NUMBER)) {
            1 -> listViewModel.disposeRequestDiscoverMovies()
            2 -> listViewModel.disposeRequestDiscoverTvs()
        }
    }
}