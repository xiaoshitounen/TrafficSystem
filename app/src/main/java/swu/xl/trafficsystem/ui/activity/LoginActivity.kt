package swu.xl.trafficsystem.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_login.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.util.AppExecutors
import swu.xl.trafficsystem.util.PhoneUtil
import swu.xl.trafficsystem.util.ToastUtil

class LoginActivity : BaseActivity() {
    companion object {
        const val REQUEST_CODE = 10010

        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun initListener() {
        login.setOnClickListener {
            if (account.text.toString().isEmpty()) {
                ToastUtil.toast("请输入手机号")
                return@setOnClickListener
            }

            if (!PhoneUtil.isPhoneNumberValid(account.text.toString())) {
                ToastUtil.toast("手机号不合法")
                return@setOnClickListener
            }

            if (password.text.toString().isEmpty()) {
                ToastUtil.toast("请输入密码")
                return@setOnClickListener
            }

            AppExecutors.Default.execute {

            }
        }

        register.setOnClickListener {
            //startActivityForResult(Intent(this, RegisterActivity::class.java), REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.let {
                //account.setText(data.getStringExtra(RegisterActivity.ACCOUNT))
                //password.setText(data.getStringExtra(RegisterActivity.PASSWORD))
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}