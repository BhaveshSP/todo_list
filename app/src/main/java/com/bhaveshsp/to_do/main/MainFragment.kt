package com.bhaveshsp.to_do.main

import android.app.Activity.RESULT_OK
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bhaveshsp.to_do.DoItem
import com.bhaveshsp.to_do.R
import com.bhaveshsp.to_do.addtodo.AddTodoActivity
import com.bhaveshsp.to_do.app_default.AppDefaultFragment
import com.bhaveshsp.to_do.utility.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

/**
 *  @author Bhavesh Purohit
 *  Created on 28/06/2020
 */
class MainFragment : AppDefaultFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var noTaskLayout: LinearLayout
    private lateinit var adapterView: TodoAdapterView
    private lateinit var codingButton: FloatingActionButton
    private lateinit var showButton: FloatingActionButton
    private var coding = false
    private var show = false
    private var theme:String? = LIGHT_THEME
    private var doItemList:ArrayList<DoItem> = ArrayList()
    private lateinit var addTaskButton:FloatingActionButton
    private lateinit var storeRetrieveData:StoreRetrieveData
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTaskButton = view.findViewById(R.id.addTaskButton)
        showButton = view.findViewById(R.id.floating_show)
        recyclerView = view.findViewById(R.id.taskRecyclerView)
        noTaskLayout = view.findViewById(R.id.emptyTodoListView)
        val linearLayout: LinearLayout = view.findViewById(R.id.mainFragmentLayout)
        val floatingLayout: LinearLayout = view.findViewById(R.id.floatingLayout)
        storeRetrieveData = StoreRetrieveData(context = context!!,filename = FILE_NAME)
        doItemList = storeRetrieveData.loadFromFile()
        Log.d(TAG, "onViewCreated: Main Activity Items Added $doItemList")
        adapterView = context?.let {  TodoAdapterView(doItemList,context = it)  }!!
        recyclerView.adapter = adapterView
        checkListSize()
        addTaskButton.setOnClickListener {
            val addTodoIntent = Intent(context,AddTodoActivity::class.java)
            val todoItem = DoItem(todoTitle = "")
            addTodoIntent.putExtra(EXTRA_DO_ITEM,todoItem)
            startActivityForResult(addTodoIntent, REQUEST_CODE)
        }
        codingButton = view.findViewById(R.id.codeButton)
        coding = context!!.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
            .getBoolean(SWITCH_PREF,false)
        codingButton.setOnClickListener {
            coding = !coding
            val sharedPref = context!!.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean(SWITCH_PREF,coding)
            editor.apply()
            if (coding){
                startBreakCoding()
                Toast.makeText(context!!, "Coding Started", Toast.LENGTH_SHORT).show()
            }
            else{
                endBreakCoding()
                Toast.makeText(context!!, "Coding Stopped", Toast.LENGTH_SHORT).show()
            }
        }
        setThemeFragment(linearLayout = linearLayout)
        val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN , ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.layoutPosition
                val toPosition = target.layoutPosition
                swapItems(fromPosition,toPosition,doItemList)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTodo(viewHolder.layoutPosition,doItemList)
            }

        })
        itemTouchHelper.attachToRecyclerView(recyclerView)

        showButton.setOnClickListener {
            if(!show){
                floatingLayout.visibility = View.VISIBLE
            }
            else{
                floatingLayout.visibility = View.INVISIBLE
            }
            show = !show
        }
    }
    private fun startBreakCoding(){
        val now = Calendar.getInstance().time.time
        val codingTime = now + 6000
        val breakTime = now + (AlarmManager.INTERVAL_FIFTEEN_MINUTES * 3)
        Log.d(TAG, "startBreakCoding: Coding Time :$codingTime")
        Log.d(TAG, "startBreakCoding: Break Time :$breakTime")
        createAlarmBreakCoding(title =  getString(R.string.coding), description =  getString(R.string.coding_break_over),
            startTime = codingTime , requestCode = CODING_REQUEST_CODE)
        createAlarmBreakCoding(title = getString(R.string.break_time), description = getString(R.string.break_coding_over),
            startTime = breakTime, requestCode = BREAK_REQUEST_CODE)


    }
    private fun createAlarmBreakCoding(title: String, description: String, startTime: Long, requestCode: Int) {
        val codingIntent = Intent(context,TodoNotificationService::class.java)
        codingIntent.putExtra(NOTIFICATION_TITLE_EXTRA,title)
        codingIntent.putExtra(NOTIFICATION_DESCRIPTION_EXTRA,description)
        codingIntent.putExtra(NOTIFICATION_UUID_EXTRA,UUID.randomUUID())
        val alarmManager = getAlarmManager()
        val pendingIntent = PendingIntent.getService(context,requestCode,codingIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,startTime,AlarmManager.INTERVAL_HOUR,pendingIntent)
    }
    private fun endBreakCoding(){
        val stopCodingIntent = Intent(context, TodoNotificationService::class.java)
        deleteAlarm(stopCodingIntent, CODING_REQUEST_CODE)
        deleteAlarm(stopCodingIntent, BREAK_REQUEST_CODE)
    }

    private fun deleteTodo(layoutPosition: Int, doItemList: ArrayList<DoItem>) {

        if (doItemList[layoutPosition].getHasReminder()){
            val deleteIntent = Intent(context,TodoNotificationService::class.java)
            deleteAlarm(deleteIntent, doItemList[layoutPosition].getUUID().hashCode())
        }
        doItemList.removeAt(layoutPosition)
        storeRetrieveData.saveData(doItemList)
        checkListSize()
        adapterView.notifyItemRemoved(layoutPosition)
        Log.d(TAG, "deleteTodo: Item Deleted")
    }

    private fun deleteAlarm(deleteIntent: Intent, hashCode: Int) {
        val pendingIntent = PendingIntent.getService(context,hashCode,deleteIntent,PendingIntent.FLAG_NO_CREATE)
        if (pendingIntent != null){
            pendingIntent.cancel()
            getAlarmManager().cancel(pendingIntent)
        }
    }

    private fun swapItems(fromPosition: Int, toPosition: Int, doItemList: ArrayList<DoItem>) {
        Collections.swap(doItemList,fromPosition,toPosition)
        storeRetrieveData.saveData(doItemList)
        adapterView.notifyItemMoved(fromPosition, toPosition)
        Log.d(TAG, "swapItems: Item Swaped")
    }

    override fun layoutRes(): Int {
        return R.layout.fragment_main
    }
    fun newInstance():Fragment{
        return MainFragment()
    }
    private fun setThemeFragment(linearLayout:LinearLayout){
        theme = activity!!.getSharedPreferences(SHARED_PREF,Context.MODE_PRIVATE).getString(
            THEME_PREF, LIGHT_THEME)
        val bgColor:Int
        val actionButtonColor:Int
        when(theme){
            LIGHT_THEME -> {
                bgColor = ContextCompat.getColor(context!!,R.color.colorBackground)
                actionButtonColor = ContextCompat.getColor(context!!,R.color.colorAccent)

            }
            else -> {
                bgColor = ContextCompat.getColor(context!!,R.color.colorBackgroundDark)
                actionButtonColor = ContextCompat.getColor(context!!,R.color.colorAccentDark)
            }
        }
        val floatingColor = ColorStateList.valueOf(actionButtonColor)
        codingButton.backgroundTintList = floatingColor
        addTaskButton.backgroundTintList = floatingColor
        showButton.backgroundTintList = floatingColor
        linearLayout.setBackgroundColor(bgColor)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK){
            Log.d(TAG, "onActivityResult: ItemReceived"+data!!.getSerializableExtra(EXTRA_DO_ITEM))
            val doItemExtra = data.getSerializableExtra(EXTRA_DO_ITEM) as DoItem
            if (doItemExtra.getTitle().isNotEmpty()){
                addToList(doItemExtra)
                if (doItemExtra.getHasReminder()){
                    val reminderIntent = Intent(context,TodoNotificationService::class.java)
                    reminderIntent.putExtra(NOTIFICATION_TITLE_EXTRA,doItemExtra.getTitle())
                    reminderIntent.putExtra(NOTIFICATION_DESCRIPTION_EXTRA,doItemExtra.getDescription())
                    reminderIntent.putExtra(NOTIFICATION_UUID_EXTRA,doItemExtra.getUUID())
                    createAlarm(reminderIntent,doItemExtra.getUUID().hashCode(),doItemExtra.getDate()!!.time)
                }
            }
        }
    }

    private fun createAlarm(reminderIntent: Intent, hashCode: Int, time: Long) {
        val alarmManager = getAlarmManager()
        val pendingIntent = PendingIntent.getService(context!!,hashCode,reminderIntent,PendingIntent.FLAG_UPDATE_CURRENT)
        Log.d(TAG, "createAlarm: Will Repeat Once")
        alarmManager.set(AlarmManager.RTC_WAKEUP,time,pendingIntent)
        Log.d(TAG, "createAlarm: Reminder Set at $time")
    }
    private fun getAlarmManager():AlarmManager{
        return context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun checkListSize(){
        if (doItemList.size>0){
            noTaskLayout.visibility = View.GONE
        }
        else{
            noTaskLayout.visibility = View.VISIBLE
        }
    }

    private fun addToList(doItem:DoItem){
        doItemList.add(doItem)
        storeRetrieveData.saveData(doItemList)
        checkListSize()
        adapterView.notifyItemInserted(doItemList.size-1)
    }

}