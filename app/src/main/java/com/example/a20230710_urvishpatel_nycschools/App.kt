package com.example.a20230710_urvishpatel_nycschools

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //Annotation for marking the Application class where the Dagger components should be generated.
class App:Application() {
}