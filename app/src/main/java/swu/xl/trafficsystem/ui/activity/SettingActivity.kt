package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_setting.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.manager.UserManager
import swu.xl.trafficsystem.sql.TrafficSystemRoomBase
import swu.xl.trafficsystem.util.AppExecutors
import swu.xl.trafficsystem.util.ToastUtil

class SettingActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_setting

    override fun initListener() {
        delete_love.setOnClickListener {
            AppExecutors.Default.execute {
                TrafficSystemRoomBase.getRoomBase(this).loveDao().deleteAll()
                ToastUtil.toast("数据已经清空")
            }
        }

        delete_history.setOnClickListener {
            AppExecutors.Default.execute {
                TrafficSystemRoomBase.getRoomBase(this).historyDao().deleteAll()
                ToastUtil.toast("数据已经清空")
            }
        }


        logout.setOnClickListener {
            UserManager.logout()
            finish()
        }
    }
}