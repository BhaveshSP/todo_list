package com.bhaveshsp.to_do.app_default

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
/**
 *  @author Bhavesh Purohit
 *  Created on 28/06/2020
 */
abstract class AppDefaultFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes(),container,false)
    }
    @LayoutRes
    protected abstract fun layoutRes():Int

}