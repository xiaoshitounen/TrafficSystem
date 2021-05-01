package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity

class UserEditActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, UserEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_user_edit
}