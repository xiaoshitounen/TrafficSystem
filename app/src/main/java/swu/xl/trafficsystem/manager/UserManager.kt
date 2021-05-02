package swu.xl.trafficsystem.manager
import com.amap.api.services.core.LatLonPoint
import kotlinx.android.synthetic.main.fragment_home.*
import swu.xl.trafficsystem.application.MyApplication
import swu.xl.trafficsystem.constant.Constant
import swu.xl.trafficsystem.model.MapLocation
import swu.xl.trafficsystem.sql.TrafficSystemRoomBase
import swu.xl.trafficsystem.sql.entity.UserEntity
import swu.xl.trafficsystem.util.AppExecutors
import swu.xl.trafficsystem.util.ThreadUtil
import java.util.concurrent.CopyOnWriteArrayList

object UserManager {
    private var user: UserEntity? = null
    private val listeners = CopyOnWriteArrayList<OnUserChangeListener>()
    var home: MapLocation? = null
    var company: MapLocation? = null

    fun login(userEntity: UserEntity) {
        user = userEntity
        //查询家和公司数据
        AppExecutors.Default.execute {
            val list = TrafficSystemRoomBase.getRoomBase(MyApplication.getApplication()).locationDao().queryAll(userEntity.id)
            list.forEach {
                ThreadUtil.runOnUiThread {
                    if (it.type == Constant.ROUTE_POINT_HOME) {
                        home = MapLocation(LatLonPoint(it.latitude, it.longitude), it.name)
                    }

                    if (it.type == Constant.ROUTE_POINT_COMPANY) {
                        company = MapLocation(LatLonPoint(it.latitude, it.longitude), it.name)
                    }
                }
            }
        }
        listeners.forEach { it.onUserLogin(userEntity) }
    }

    fun logout() {
        user = null
        listeners.forEach { it.onUserLogout() }
    }

    fun registerOnUserChangeListener(listener: OnUserChangeListener) {
        if (listener !in listeners) {
            listeners.add(listener)
        }
    }

    fun isUserLogin() = user != null

    fun getCurrentUser() = user

    fun setCurrentUser(user: UserEntity) {
        this.user = user
        ThreadUtil.runOnUiThread { listeners.forEach { it.onUserLogin(user) } }
    }
}

interface OnUserChangeListener {
    fun onUserLogin(user: UserEntity)
    fun onUserLogout()
}

open class OnUserChangeCallBack : OnUserChangeListener {
    override fun onUserLogin(user: UserEntity) {}
    override fun onUserLogout() {}
}