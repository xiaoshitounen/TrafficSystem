package swu.xl.trafficsystem.ui.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import kotlinx.android.synthetic.main.activity_register.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.sql.TrafficSystemRoomBase
import swu.xl.trafficsystem.sql.entity.UserEntity
import swu.xl.trafficsystem.util.AppExecutors
import swu.xl.trafficsystem.util.PhoneUtil
import swu.xl.trafficsystem.util.ThreadUtil
import swu.xl.trafficsystem.util.ToastUtil


class RegisterActivity : BaseActivity() {
    companion object {
        const val ACCOUNT = "account"
        const val PASSWORD = "password"
    }

    private var bitmap: String = ""

    override fun getLayoutId() = R.layout.activity_register

    override fun initListener() {
        /*icon.setOnClickListener {
            EasyPhotos.createAlbum(this, true,false, GlideEngine.getInstance())
                .setFileProviderAuthority("androidx.core.content.FileProvider")
                .setCount(1)
                .start(object : SelectCallback() {
                    override fun onResult(photos: ArrayList<Photo>, isOriginal: Boolean) {
                        bitmap = photos[0].path
                        icon.setImageURI(Uri.fromFile(File(bitmap)))
                    }

                    override fun onCancel() {
                        ToastUtil.toast("未选择照片")
                    }
                })
        }*/

        register.setOnClickListener {
            if (nickname.text.toString().isEmpty()) {
                ToastUtil.toast("请输入昵称")
                return@setOnClickListener
            }

            if (account_up.text.toString().isEmpty()) {
                ToastUtil.toast("请输入手机号")
                return@setOnClickListener
            }

            if (!PhoneUtil.isPhoneNumberValid(account_up.text.toString())) {
                ToastUtil.toast("手机号不合法")
                return@setOnClickListener
            }

            if (password_up.text.toString().isEmpty()) {
                ToastUtil.toast("请输入密码")
                return@setOnClickListener
            }

            AppExecutors.Default.execute {
                val dao = TrafficSystemRoomBase.getRoomBase(this).userDao()
                dao.queryAll().forEach {
                    if (TextUtils.equals(it.account, account_up.text.toString())) {
                        ToastUtil.toast("手机号已经注册过了")
                        return@execute
                    }
                }
                dao.insert(UserEntity(
                    0,
                    nickname.text.toString(),
                    null,
                    account_up.text.toString(),
                    password_up.text.toString()
                ))
                ThreadUtil.runOnUiThread {
                    val intent = Intent().apply {
                        putExtra(ACCOUNT, account_up.text.toString())
                        putExtra(PASSWORD, password_up.text.toString())
                    }
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}