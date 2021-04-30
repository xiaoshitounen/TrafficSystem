package swu.xl.trafficsystem.ui.fragment

import android.view.View
import kotlinx.android.synthetic.main.fragment_user.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseFragment
import swu.xl.trafficsystem.manager.OnUserChangeCallBack
import swu.xl.trafficsystem.manager.UserManager
import swu.xl.trafficsystem.sql.entity.UserEntity
import swu.xl.trafficsystem.ui.activity.FeedBackActivity
import swu.xl.trafficsystem.ui.activity.LoginActivity
import swu.xl.trafficsystem.ui.activity.WebViewActivity
import swu.xl.trafficsystem.util.ToastUtil

class UserFragment: BaseFragment() {
    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_user, null)
    }

    override fun initData() {
        if (UserManager.isUserLogin()) {
            UserManager.getCurrentUser()?.let { user ->
                doLogin(user)
            }
        }
    }

    private fun doLogin(user: UserEntity) {
        activity?.let {
            user.picture?.let {
                //Glide.with(it).load(user.picture).into(icon)
            }
            nickname.text = user.nickname
        }
    }

    override fun initListener() {
        UserManager.registerOnUserChangeListener(object : OnUserChangeCallBack() {
            override fun onUserLogin(user: UserEntity) {
                doLogin(user)
            }

            override fun onUserLogout() {
                icon.setImageResource(R.drawable.user_default)
                nickname.text = "点击登录"
            }
        })
        user.setOnClickListener {
            activity?.let {
                if (UserManager.isUserLogin()) {

                } else {
                    LoginActivity.start(it)
                }
            }
        }
        user_document.setOnClickListener {
            if (UserManager.isUserLogin()) {
                activity?.let {
                    WebViewActivity.loadURL(it, "https://cache.amap.com/h5/h5/publish/238/index.html")
                }
            } else {
                ToastUtil.toast("请先登录")
            }
        }
        user_feedback.setOnClickListener {
            if (UserManager.isUserLogin()) {
                activity?.let {
                    FeedBackActivity.start(it)
                }
            } else {
                ToastUtil.toast("请先登录")
            }
        }
    }
}