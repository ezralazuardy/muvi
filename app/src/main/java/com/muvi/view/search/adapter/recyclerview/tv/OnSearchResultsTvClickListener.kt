/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.search.adapter.recyclerview.tv

import com.muvi.model.discover.DiscoverTvListResult

interface OnSearchResultsTvClickListener {

    fun onTvItemClick(tv: DiscoverTvListResult)
}