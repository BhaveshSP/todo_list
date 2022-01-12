package com.bhaveshsp.to_do.addtodo

import android.app.*
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.bhaveshsp.to_do.DoItem
import com.bhaveshsp.to_do.R
import com.bhaveshsp.to_do.app_default.AppDefaultFragment
import com.bhaveshsp.to_do.utility.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

/**
 * @author Bhavesh Purohit
 * Created on 05/07/2020
 */

class AddTodoFragment : AppDefaultFragment() {
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var titleText: EditText
    private lateinit var descriptionText: EditText
    private lateinit var reminderSwitch: Switch
    private lateinit var doneButton: FloatingActionButton
    private lateinit var timeText: TextView
    private lateinit var dateText: TextView
    private lateinit var dateInPast: TextView
    private lateinit var atText: TextView
    private lateinit var notifyMe: TextView
    private lateinit var reminderTextView: TextView
    private var theme:String? = LIGHT_THEME
    private var dialogTheme:Int = R.style.DialogLight
    private lateinit var doItem:DoItem
    private lateinit var scheduledCalendar:Calendar
    private lateinit var today:Calendar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title = view.findViewById(R.id.titleTextDisplay)
        titleText = view.findViewById(R.id.todoTitle)
        description = view.findViewById(R.id.descriptionTextDisplay)
        descriptionText = view.findViewById(R.id.todoDescription)
        doneButton = view.findViewById(R.id.doneButton)
        reminderTextView = view.findViewById(R.id.reminderText)
        reminderSwitch = view.findViewById(R.id.reminderSwitch)
        timeText = view.findViewById(R.id.timePickerText)
        dateText = view.findViewById(R.id.datePickerText)
        dateInPast = view.findViewById(R.id.dateInPast)
        atText = view.findViewById(R.id.atText)
        notifyMe = view.findViewById(R.id.notifyMeText)
        val reminderLayout = view.findViewById<LinearLayout>(R.id.reminderLayout)
        val addTodoLinearLayout = view.findViewById<LinearLayout>(R.id.addTodoLinearLayout)
        scheduledCalendar = Calendar.getInstance()
        today = Calendar.getInstance()
        setDateAndTime()
        doItem = DoItem("")
        doneButton.setOnClickListener {
            sendTheData()
        }
        titleText.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val string = s.toString()
                if (string.isNotEmpty()){
                    Log.d(TAG, "onTextChanged: Title$string")
                    val title = Character.toUpperCase(string[0]) + string.substring(1)
                    doItem.setTitle(title)
                }
            }
        })
        descriptionText.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val string = s.toString()
                if (string.isNotEmpty()){
                    Log.d(TAG, "onTextChanged: Description $string")
                    val description = Character.toUpperCase(string[0]) + string.substring(1)
                    doItem.setDescription(description)
                }
            }
        })
        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            doItem.setHasReminder(isChecked)
            if (isChecked){
                reminderLayout.visibility = View.VISIBLE
            }
            else{
                reminderLayout.visibility = View.GONE
            }
        }
        timeText.setOnClickListener {
            createTimePicker()
        }
        dateText.setOnClickListener {
            createDatePicker()
        }
        setThemeFragment(linearLayout = addTodoLinearLayout)
    }
    fun newInstance(): Fragment {
        return AddTodoFragment()
    }
    override fun layoutRes(): Int {
        return R.layout.fragment_add_todo
    }
    private fun setThemeFragment(linearLayout: LinearLayout){
        theme = activity!!.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE).getString(
            THEME_PREF, LIGHT_THEME
        )
        val bgColor:Int
        val actionButtonColor:Int
        val titleColor:Int
         when(theme){
            LIGHT_THEME -> {
                bgColor= ContextCompat.getColor(context!!,R.color.colorBackground)
                titleColor = ContextCompat.getColor(context!!,R.color.colorBackgroundDark)
                actionButtonColor = ContextCompat.getColor(context!!,R.color.colorAccent)
                dialogTheme = R.style.DialogLight
            }
            else -> {
                bgColor = ContextCompat.getColor(context!!,R.color.colorBackgroundDark)
                titleColor = ContextCompat.getColor(context!!,R.color.colorBackground)
                actionButtonColor =  ContextCompat.getColor(context!!,R.color.colorAccentDark)
                dialogTheme = R.style.DialogDark
            }
        }
        title.setTextColor(titleColor)
        descriptionText.setTextColor(titleColor)
        timeText.setTextColor(titleColor)
        dateText.setTextColor(titleColor)
        titleText.setTextColor(titleColor)
        description.setTextColor(titleColor)
        atText.setTextColor(titleColor)
        reminderTextView.setTextColor(titleColor)
        notifyMe.setTextColor(titleColor)
        val accentColor = ColorStateList.valueOf(actionButtonColor)
        ViewCompat.setBackgroundTintList(titleText,accentColor)
        ViewCompat.setBackgroundTintList(descriptionText,accentColor)
        doneButton.backgroundTintList = accentColor
        linearLayout.setBackgroundColor(bgColor)
    }
    private fun sendTheData(){
        Log.d(TAG, "sendTheData: Title ${doItem.getTitle()}")
        if (doItem.getTitle().isNotEmpty()){
            val resultIntent = Intent()
            if (doItem.getHasReminder()){
                doItem.setDate(date = scheduledCalendar.time)
                Log.d(TAG, "Has Reminder ${doItem.getTitle()}")
            }
            resultIntent.putExtra(EXTRA_DO_ITEM,doItem)
            Log.d(TAG, "sendTheData: Data sending.....")
            activity!!.setResult(RESULT_OK,resultIntent)
            activity!!.finish()
        }
    }
    private fun createTimePicker(){
        val hour = scheduledCalendar.get(Calendar.HOUR_OF_DAY)
        val min = scheduledCalendar.get(Calendar.MINUTE)
        val timePicker = TimePickerDialog(context,dialogTheme,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                scheduledCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
                scheduledCalendar.set(Calendar.MINUTE,minute)
                setDateAndTime()
            },hour,min,false)
        timePicker.show()
    }
    private fun createDatePicker(){
        val mDayOfMonth = scheduledCalendar.get(Calendar.DAY_OF_MONTH)
        val mYear = scheduledCalendar.get(Calendar.YEAR)
        val mMonth = scheduledCalendar.get(Calendar.MONTH)
        val datePickerDialog = DatePickerDialog(context!!,dialogTheme,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth -> 
                scheduledCalendar.set(Calendar.YEAR,year)
                scheduledCalendar.set(Calendar.MONTH,month)
                scheduledCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                setDateAndTime()
            },mYear,mMonth,mDayOfMonth)
        datePickerDialog.show()
    }



    private fun setDateAndTime() {
        if (scheduledCalendar.time.before(today.time) || scheduledCalendar.before(today)){
            Log.d(TAG, "setDateAndTime: Get in Past ")
            dateInPast.visibility = View.VISIBLE
            scheduledCalendar = Calendar.getInstance()
        }
        else{
            dateInPast.visibility = View.GONE
        }
        Log.d(TAG, "setDateAndTime: Selected Time ${scheduledCalendar.time}")
        Log.d(TAG, "setDateAndTime: Selected Time ${today.time}")
        timeText.text = getTime(scheduledCalendar)
        when {
            checkToday(scheduledCalendar = scheduledCalendar,today = today) -> {
                dateText.text = getString(R.string.today)
            }
            checkTomorrow(scheduledCalendar = scheduledCalendar) -> {
                dateText.text = getString(R.string.tomorrow)
            }
            else -> {
                dateText.text = getDate(scheduledCalendar = scheduledCalendar,context = context!!)
            }
        }
    }

}