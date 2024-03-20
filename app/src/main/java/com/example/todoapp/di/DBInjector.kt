package com.example.todoapp.di

import android.app.Application

class DBInjector:Application() {
    companion object {
        val dbContainer =  DBContainer()
    }
}