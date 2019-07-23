package com.apator.map.helpers

import java.text.SimpleDateFormat
import java.util.*

class ValuesGenerator {
    fun getActualDate(): String{
        val simpleDateFormat = SimpleDateFormat("yyyy-MMM-dd HH:mm")
        return simpleDateFormat.format(Date())
    }
}