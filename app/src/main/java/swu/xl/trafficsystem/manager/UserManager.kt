package swu.xl.trafficsystem.manager
import swu.xl.trafficsystem.sql.entity.UserEntity
import swu.xl.trafficsystem.util.ThreadUtil
import java.util.concurrent.CopyOnWriteArrayList

object UserManager {
    private var user: UserEntity? = null
    private val listeners = CopyOnWriteArrayList<OnUserChangeListener>()

    fun login(userEntity: UserEntity) {
        user = userEntity
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