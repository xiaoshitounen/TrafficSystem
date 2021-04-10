package swu.xl.trafficsystem.application

import android.app.Application

class MyApplication : Application() {
    companion object {
        private var application: Application? = null

        fun getApplication() = application!!
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}