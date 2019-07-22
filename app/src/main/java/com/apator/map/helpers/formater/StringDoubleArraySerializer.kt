package com.apator.map.helpers.formater

import com.apator.map.R
import java.lang.reflect.Array
import java.lang.reflect.Array.*

class StringDoubleArraySerializer {
    private val separator = ';'
    fun serialize(data:List<Double?>?):String
    {

        var result = ""
        data!!.forEach { result+= "$separator$it" }
        return result
    }
    fun deserialize(data: String): ArrayList<Double> {
        val arrayString = data.split(separator)
        val result = arrayListOf<Double>()

        arrayString.forEach {
            if (it.isNotEmpty() && it != "null") {
                result.add(it.toDouble())
            }
        }
        return result
    }
}