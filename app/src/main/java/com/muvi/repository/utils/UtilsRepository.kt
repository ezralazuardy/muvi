/*
 * Created by Ezra Lazuardy on 10/31/19, 12:23 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/30/19, 10:27 PM
 */

package com.muvi.repository.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.muvi.config.AppConfig
import com.muvi.view.widget.StackViewWidget
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class UtilsRepository(private val context: Context) {

    fun formatDate(format: String, date: String): String {
        var formattedDate = ""
        try {
            SimpleDateFormat(AppConfig.TMDB_DATE_FORMAT, Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
                parse(date)?.let {
                    formattedDate = SimpleDateFormat(format, Locale.getDefault()).format(it)
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return if(formattedDate.isEmpty()) date else formattedDate
    }

    fun formatTime(format: String, time: String): String {
        var formattedTime = ""
        try {
            SimpleDateFormat(AppConfig.DEFAULT_TIME_FORMAT, Locale.getDefault()).apply {
                timeZone = TimeZone.getDefault()
                parse(time)?.let {
                    formattedTime = SimpleDateFormat(format, Locale.getDefault()).format(it)
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return if(formattedTime.isEmpty()) time else formattedTime
    }

    fun formatHtmlFromString(string: String): Spanned {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY)
        else
            @Suppress("DEPRECATION")
            Html.fromHtml(string)
    }

    fun refreshFavouriteWidget() {
        val intent = Intent(context, StackViewWidget::class.java)
        intent.action = StackViewWidget.UPDATE_WIDGET
        context.sendBroadcast(intent)
    }
}