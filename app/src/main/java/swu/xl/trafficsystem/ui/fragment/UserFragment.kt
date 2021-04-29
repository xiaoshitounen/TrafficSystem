package swu.xl.trafficsystem.ui.fragment

import android.view.View
import kotlinx.android.synthetic.main.fragment_user.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseFragment
import swu.xl.trafficsystem.ui.activity.WebViewActivity

class UserFragment: BaseFragment() {
    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_user, null)
    }

    override fun initListener() {
        user_document.setOnClickListener {
            activity?.let {
                WebViewActivity.loadURL(it, "https://cache.amap.com/h5/h5/publish/238/index.html")
            }
        }
    }
}