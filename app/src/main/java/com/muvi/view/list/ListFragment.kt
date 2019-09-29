package com.muvi.view.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.muvi.R
import com.muvi.view.list.adapter.movie.ListMovieAdapter
import com.muvi.view.list.adapter.tv.ListTvAdapter
import com.muvi.viewmodel.list.ListViewModel
import kotlinx.android.synthetic.main.fragment_activity_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class ListFragment : Fragment(), AnkoLogger {

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

    private val listViewModel: ListViewModel by viewModels()
    private val listMovieAdapter by lazy { ListMovieAdapter() }
    private val listTvAdapter by lazy { ListTvAdapter() }

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

    override fun onPause() {
        super.onPause()
        loadingActivityList.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerViewFragmentActivityList.adapter = null
    }
}