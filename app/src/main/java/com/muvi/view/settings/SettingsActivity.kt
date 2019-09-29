package com.muvi.view.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.muvi.R
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setComponent()
    }

    private fun setComponent() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun showCurrentAppLanguage() {
        rowAppLanguageDescriptionActivitySettings.text = Locale.getDefault().displayLanguage
    }

    private fun setListener() {
        rowAppLanguageActivitySettings.onClick {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun unsetListener() {
        rowAppLanguageActivitySettings.setOnClickListener(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        setListener()
        showCurrentAppLanguage()
    }

    override fun onStop() {
        super.onStop()
        unsetListener()
    }
}