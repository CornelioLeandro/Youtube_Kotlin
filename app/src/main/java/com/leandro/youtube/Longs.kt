package com.leandro.youtube


    fun Long.formatTime() : String{
        val minute: Long = this/ 1000/60
        val seconds: Long = this/ 1000 % 60
        return  String.format("%02d:%02d", minute,seconds)
    }
