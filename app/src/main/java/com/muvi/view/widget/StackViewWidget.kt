/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.view.detail.DetailActivity
import org.jetbrains.anko.startActivity

class StackViewWidget : AppWidgetProvider() {

    companion object {

        private const val TOAST_ACTION = "com.muvi.TOAST_ACTION"
        const val EXTRA_ITEM = "com.muvi.EXTRA_ITEM"
        const val UPDATE_WIDGET = "com.muvi.UPDATE_WIDGET"

        internal fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            appWidgetManager.updateAppWidget(
                appWidgetId,
                RemoteViews(context.packageName, R.layout.stack_view_widget).apply {
                    this.setRemoteAdapter(
                        R.id.stackView,
                        Intent(context, StackWidgetService::class.java).apply {
                            data = Uri.parse(this.toUri(Intent.URI_INTENT_SCHEME))
                            this.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                        }
                    )
                    this.setEmptyView(R.id.stackView, R.id.emptyView)
                    this.setPendingIntentTemplate(
                        R.id.stackView,
                        PendingIntent.getBroadcast(
                            context,
                            0,
                            Intent(context, this::class.java).apply {
                                action = TOAST_ACTION
                                this.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                            },
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    )
                }
            )
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) updateAppWidget(context, appWidgetManager, appWidgetId)
    }

    override fun onEnabled(context: Context) { }

    override fun onDisabled(context: Context) { }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.e(this::class.java.simpleName, "onReceive(${intent.action})")
        when(intent.action) {
            UPDATE_WIDGET -> {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                appWidgetManager.notifyAppWidgetViewDataChanged(
                    appWidgetManager.getAppWidgetIds(ComponentName(context, this::class.java)),
                    R.id.stackView
                )
            }
            EXTRA_ITEM -> {
                context.startActivity<DetailActivity>(
                    AppConfig.INTENT_EXTRA_CONTENT_TYPE to intent.getSerializableExtra(AppConfig.INTENT_EXTRA_CONTENT_TYPE),
                    AppConfig.INTENT_EXTRA_DATA_MOVIE to intent.getParcelableArrayExtra(AppConfig.INTENT_EXTRA_DATA_MOVIE),
                    AppConfig.INTENT_EXTRA_OPEN_FROM to intent.getStringExtra(AppConfig.INTENT_EXTRA_OPEN_FROM)
                )
            }
        }
    }
}

