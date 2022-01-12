package com.bhaveshsp.to_do.app_default

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bhaveshsp.to_do.R
/**
 *  @author Bhavesh Purohit
 *  Created on 28/06/2020
 */
abstract class AppDefaultActivity : AppCompatActivity() {
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createLayout())
        setUpInitialFragment(savedInstanceState)
    }
    @LayoutRes
    protected abstract fun createLayout():Int

    private fun setUpInitialFragment(@Nullable savedInstanceState: Bundle?){
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container,createFragment())
                .commit()
        }
    }
    @NonNull
    protected abstract fun createFragment():Fragment
}