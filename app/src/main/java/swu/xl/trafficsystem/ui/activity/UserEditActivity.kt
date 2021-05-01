package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_user_edit.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.manager.UserManager

class UserEditActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_user_edit

    override fun initData() {
        UserManager.getCurrentUser()?.let {
            it.picture?.let { }
            user_nick_name.text = it.nickname
            user_password.text = getPassword(it.password)
        }
    }

    private fun getPassword(password: String): String {
        var length = ""
        repeat(password.length) {
            length += "*"
        }
        return length
    }
}