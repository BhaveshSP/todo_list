package com.bhaveshsp.to_do.addtodo


import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bhaveshsp.to_do.R
import com.bhaveshsp.to_do.app_default.AppDefaultActivity
import com.bhaveshsp.to_do.utility.DARK_THEME
import com.bhaveshsp.to_do.utility.LIGHT_THEME
import com.bhaveshsp.to_do.utility.SHARED_PREF
import com.bhaveshsp.to_do.utility.THEME_PREF

/**
 * @author Bhavesh Purohit
 * Created on 06/07/2020
 */
class AddTodoActivity : AppDefaultActivity() {
    private lateinit var toolbar:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar = findViewById(R.id.toolbar)
        val theme = getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString(
            THEME_PREF, LIGHT_THEME)
        val toolbarColor = when(theme){
            DARK_THEME ->{
                ContextCompat.getColor(this,R.color.colorPrimaryDark)
            }
            else -> {
                ContextCompat.getColor(this,R.color.colorPrimary)
            }
        }
        toolbar.setBackgroundColor(toolbarColor)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val cross = getDrawable(R.drawable.ic_cross)
        supportActionBar!!.setHomeAsUpIndicator(cross)
    }

    override fun createLayout(): Int {
        return R.layout.activity_add_todo
    }

    override fun createFragment(): Fragment {
        return AddTodoFragment().newInstance()
    }
}