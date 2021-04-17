package swu.xl.trafficsystem.ui.fragment

import android.view.View
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseFragment

class UserFragment: BaseFragment() {
    override fun initView(): View {
        return View.inflate(context, R.layout.fragment_user, null)
    }
}