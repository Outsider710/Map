package com.apator.map.helpers.formater

object DoubleToString {
    fun format(separator:Char,data:List<Double?>?):String
    {

        var result = ""
        data!!.forEach { result+="$separator$it" }
        return result
    }
}