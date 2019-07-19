package com.apator.map.helpers.formater

object StringToDouble {
    fun format(separator: Char, data: String): ArrayList<Double> {
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