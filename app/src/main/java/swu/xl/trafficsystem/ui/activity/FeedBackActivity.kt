package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import kotlinx.android.synthetic.main.activity_feedback.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.util.ToastUtil

class FeedBackActivity : BaseActivity() {
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, FeedBackActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_feedback

    override fun initListener() {
        push.setOnClickListener {
            if (dec.text.toString().isEmpty()) {
                ToastUtil.toast("请填写问题描述")
                return@setOnClickListener
            }

            if (email.text.toString().isEmpty()) {
                ToastUtil.toast("请填写邮箱")
                return@setOnClickListener
            }

            ToastUtil.toast("感谢您的反馈")
            finish()
        }
    }
}