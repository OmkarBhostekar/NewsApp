package com.omkarcodes.newsapp.utils

import java.text.SimpleDateFormat
import java.util.*

fun String?.toTimeAgo() : String {
    return this?.let {
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val dateInMillis = sdf.parse(it.replace("T"," ")).time
        val sec = (System.currentTimeMillis() - dateInMillis) / 1000
        if (sec < 59)
            "$sec seconds ago"
        else{
            val min = sec / 60
            if (min < 59)
                "$min minutes ago"
            else{
                val hour = min / 60
                if (hour < 24)
                    "$hour hours ago"
                else{
                    val days = hour / 24
                    if (days < 30)
                        "$days days ago"
                    else{
                        val mon = days / 30
                        "$mon months ago"
                    }
                }
            }
        }
    } ?: run { "Just Now" }
}