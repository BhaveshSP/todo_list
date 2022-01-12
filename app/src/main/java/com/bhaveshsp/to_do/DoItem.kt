package com.bhaveshsp.to_do

import com.bhaveshsp.to_do.utility.*
import org.json.JSONObject
import java.io.Serializable
import java.util.*

class DoItem : Serializable {
    private var todoTitle:String
    private var hasReminder:Boolean=false
    private var date:Date? = null
    private var todoDescription:String = ""
    private var todoIdentifier = UUID.randomUUID()
    constructor(todoTitle:String){
        this.todoTitle = todoTitle
    }
    constructor(jsonObject: JSONObject){
        todoTitle = jsonObject.getString(DO_LIST_TITLE)
        todoIdentifier = UUID.fromString(jsonObject.getString(DO_LIST_IDENTIFIER))
        hasReminder = jsonObject.getBoolean(DO_LIST_HAS_REMINDER)
        todoDescription = jsonObject.getString(DO_LIST_DESCRIPTION)


    }
    fun toJson() : JSONObject{
        val jsonObject = JSONObject()
        jsonObject.put(DO_LIST_TITLE,todoTitle)
        jsonObject.put(DO_LIST_IDENTIFIER,todoIdentifier.toString())
        jsonObject.put(DO_LIST_HAS_REMINDER,hasReminder)
        jsonObject.put(DO_LIST_DESCRIPTION,todoDescription)
        return jsonObject
    }
    fun getTitle():String{
        return todoTitle
    }
    fun setTitle(title:String){
        this.todoTitle = title
    }
    fun getUUID():UUID{
        return todoIdentifier
    }
    fun getDate():Date?{
        return date
    }
    fun setDate(date:Date){
        this.date = date
    }
    fun setHasReminder(hasReminder:Boolean){
        this.hasReminder = hasReminder
    }
    fun getHasReminder():Boolean{
        return hasReminder
    }
    fun setDescription(todoDescription:String){
        this.todoDescription = todoDescription
    }
    fun getDescription():String{
        return todoDescription
    }

}