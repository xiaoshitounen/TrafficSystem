package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_user_love.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.adapter.OnUserLovePathClickListener
import swu.xl.trafficsystem.adapter.UserLoveAdapter
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.manager.UserManager
import swu.xl.trafficsystem.sql.TrafficSystemRoomBase
import swu.xl.trafficsystem.sql.entity.LoveEntity
import swu.xl.trafficsystem.util.AppExecutors
import swu.xl.trafficsystem.util.ThreadUtil

class UserLoveActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserLoveActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private val adapter = UserLoveAdapter()

    override fun getLayoutId() = R.layout.activity_user_love

    override fun initData() {
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        initLove()
    }

    private fun initLove() {
        AppExecutors.IO.execute {
            val dao = TrafficSystemRoomBase.getRoomBase(this).loveDao()
            UserManager.getCurrentUser()?.let {
                val list = dao.queryAll(it.id)
                ThreadUtil.runOnUiThread {
                    adapter.setData(list)
                }
            }
        }
    }
}