package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_location.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.manager.UserManager

class UserLocationActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserLocationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_location

    override fun initData() {
        UserManager.home?.let {
            home.text = it.name
        }

        UserManager.company?.let {
            company.text = it.name
        }
    }
}