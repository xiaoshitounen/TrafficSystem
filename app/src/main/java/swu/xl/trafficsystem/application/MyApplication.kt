package swu.xl.trafficsystem.application

import android.app.Application
import swu.xl.trafficsystem.store.TrafficSystemStore

class MyApplication : Application() {
    companion object {
        private var application: Application? = null

        fun getApplication() = application!!
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        TrafficSystemStore.init(this)
    }
}