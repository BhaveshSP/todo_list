package com.bhaveshsp.to_do.utility

import android.app.IntentService
import android.content.Intent
import android.util.Log
import java.util.*

class DeleteTodoService : IntentService("DeleteTodoService") {

    override fun onHandleIntent(intent: Intent?) {
        val uuid = intent!!.getSerializableExtra(NOTIFICATION_UUID_EXTRA) as UUID
        val storeRetrieveData = StoreRetrieveData(this, FILE_NAME)
        Log.d(TAG, "onHandleIntent: Delete Service Launched")
        val list = storeRetrieveData.loadFromFile()
        var count = 0
        for (item in list){
            if (item.getUUID().toString() == uuid.toString()){
                Log.d(TAG, "onHandleIntent: Item Found Will be Deleted")
                break
            }
            Log.d(TAG, "onHandleIntent: Not Found Yet")
            count++
        }
        Log.d(TAG, "onHandleIntent: Count = $count")
        if (count != list.size) {
            list.removeAt(count)
        }
        storeRetrieveData.saveData(list)
    }

}