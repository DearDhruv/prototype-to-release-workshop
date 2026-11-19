package com.deardhruv.ptr

import android.app.Application
import com.deardhruv.ptr.di.AppContainer

/** Holds the app-wide [AppContainer] (manual DI — no Hilt/Koin needed for a single-module MVP). */
class PtrApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
