package com.bhaveshsp.to_do.main

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bhaveshsp.to_do.R
import com.bhaveshsp.to_do.app_default.AppDefaultActivity
import com.bhaveshsp.to_do.utility.*

/**
 *  @author Bhavesh Purohit
 *  Created on 28/06/2020
 */
class MainActivity : AppDefaultActivity() {
    private lateinit var toolbar: Toolbar
    private var themeStyle:Int= R.style.CustomThemeLight
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        addTheme(getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString(THEME_PREF,
            LIGHT_THEME))
    }
    override fun createLayout(): Int {
        return R.layout.activity_main
    }
    override fun createFragment(): Fragment {
        return MainFragment().newInstance()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.setting_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.change_mode){
            val theme = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString(
                THEME_PREF, LIGHT_THEME)
            if (theme == LIGHT_THEME){
                item.title = getString(R.string.night_mode)
                Log.d(TAG, "onOptionsItemSelected: Mode Night")
            }
            else{
                Log.d(TAG, "onOptionsItemSelected: Mode Normal")
                item.title = getString(R.string.normal_mode)
            }
            changeThemeMode(theme)
        }
        return true
    }
    private fun changeThemeMode(theme:String?){
        val sharedPreferences = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        if(theme == LIGHT_THEME) {
            editor.putString(THEME_PREF, DARK_THEME)

        }
        else{
            editor.putString(THEME_PREF, LIGHT_THEME)
        }
        editor.apply()
        themeStyle = addTheme(theme)
        getTheme()
        Log.d(TAG, "changeThemeMode: ThemeChanged")
        Log.d(TAG, "changeThemeMode: Refreshing Activity")
        this.recreate()
    }

    override fun getTheme(): Resources.Theme {
        val theme = super.getTheme()
        theme.applyStyle(themeStyle,true)
        return theme
    }
    private fun addTheme(theme: String?):Int{
        Log.d("TESTING", "addTheme: $theme")
        val themeChanged:Int
        val toolbarColor:Int
        when(theme){
            DARK_THEME ->{
                themeChanged = R.style.CustomThemeDark
                toolbarColor = ContextCompat.getColor(this,R.color.colorPrimaryDark)
            }
            else -> {
                themeChanged = R.style.CustomThemeLight
                toolbarColor = ContextCompat.getColor(this,R.color.colorPrimary)
            }
        }
        toolbar.setBackgroundColor(toolbarColor)
        setTheme(themeChanged)
        return themeChanged
    }

}