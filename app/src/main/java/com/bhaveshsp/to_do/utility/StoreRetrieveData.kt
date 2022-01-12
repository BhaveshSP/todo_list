package com.bhaveshsp.to_do.utility

import android.content.Context
import android.util.Log
import com.bhaveshsp.to_do.DoItem
import org.json.JSONArray
import java.io.*
import java.lang.StringBuilder

/**
 * @author Bhavesh Purohit
 * Created on 06/07/2020
 */
class StoreRetrieveData(private val context: Context,private var filename: String) {

    private fun toJsonArray(doList: ArrayList<DoItem>):JSONArray{
        val jsonArray = JSONArray()
        for (item in doList){
            val jsonObject = item.toJson()
            jsonArray.put(jsonObject)
        }
        return jsonArray
    }
    fun saveData(doList:ArrayList<DoItem>){
        try {
            val fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.write(toJsonArray(doList).toString())
            outputStreamWriter.close()
            fileOutputStream.close()
            Log.d(TAG, "saveData: Written in file")
            Log.d(TAG, "saveData: File OutputStream Path ${fileOutputStream.fd}")
        }catch (fileNotFound:FileNotFoundException){
            Log.d(TAG, "saveData: File Not Found")

        }
    }
    fun loadFromFile():ArrayList<DoItem>{
        val items = ArrayList<DoItem>()
        var bufferReader:BufferedReader? = null
        var fileInputStream:FileInputStream? =null
        try {
            fileInputStream = context.openFileInput(filename)
            bufferReader = BufferedReader(InputStreamReader(fileInputStream))
            val stringBuilder= StringBuilder()
            var line = bufferReader.readLine()
            while( line != null){
                stringBuilder.append(line)
                line = bufferReader.readLine()
            }
            
            val jsonArray = JSONArray(stringBuilder.toString())
            Log.d(TAG, "loadFromFile: Data loaded from file $filename data $jsonArray")
            for (i in 0 until jsonArray.length()){
                val doItem = DoItem(jsonObject = jsonArray.getJSONObject(i))
                items.add(doItem)
            }
        }catch (fileNotFound:FileNotFoundException){
            Log.d(TAG, "loadFromFile: File not Found}")
        }finally {
            bufferReader?.close()
            fileInputStream?.close()
        }
        return items
    }


}