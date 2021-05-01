package swu.xl.trafficsystem.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.bumptech.glide.Glide
import com.huantansheng.easyphotos.EasyPhotos
import com.huantansheng.easyphotos.callback.SelectCallback
import com.huantansheng.easyphotos.models.album.entity.Photo
import kotlinx.android.synthetic.main.activity_user_edit.*
import kotlinx.android.synthetic.main.fragment_user.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity
import swu.xl.trafficsystem.manager.UserManager
import swu.xl.trafficsystem.sql.TrafficSystemRoomBase
import swu.xl.trafficsystem.tencent.TencentCOS
import swu.xl.trafficsystem.thirdparty.easyphoto.GlideEngine
import swu.xl.trafficsystem.util.AppExecutors
import swu.xl.trafficsystem.util.ToastUtil
import swu.xl.trafficsystem.widget.InputDialog
import swu.xl.trafficsystem.widget.InputListener
import java.io.File
import java.util.*

class UserEditActivity : BaseActivity() {
    companion object {
        private const val NICK_NAME = "修改昵称"
        private const val PASSWORD = "修改密码"

        fun start(context: Context) {
            val intent = Intent(context, UserEditActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    private var inputDialog: InputDialog? = null

    override fun getLayoutId() = R.layout.activity_user_edit

    override fun initData() {
        UserManager.getCurrentUser()?.let {
            it.picture?.let { pic ->
                Glide.with(this).load(pic).into(user_icon)
            }
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

    override fun initListener() {
        icon_module.setOnClickListener { doSelectIcon() }
        nick_name_module.setOnClickListener { doEdit(NICK_NAME) }
        password_module.setOnClickListener { doEdit(PASSWORD) }
    }

    private fun doSelectIcon() {
        EasyPhotos.createAlbum(this, true,false, GlideEngine.getInstance())
            .setFileProviderAuthority("androidx.core.content.FileProvider")
            .setCount(1)
            .start(object : SelectCallback() {
                override fun onResult(photos: ArrayList<Photo>, isOriginal: Boolean) {
                    val bitmap = photos[0].path
                    user_icon.setImageURI(Uri.fromFile(File(bitmap)))

                    doSaveIcon(bitmap)
                }

                override fun onCancel() {
                    ToastUtil.toast("未选择照片")
                }
            })
    }

    private fun doEdit(title: String) {
        inputDialog = InputDialog().apply {
            this.title = title
            this.listener = object : InputListener {
                override fun onTextInput(input: String) {
                    if (input.isNotEmpty()) {
                        if (title == NICK_NAME) {
                            doSaveNickname(input)
                        } else {
                            doSavePassword(input)
                        }
                    }
                    dismissInputDialog()
                }

                override fun onCanceled() {
                    dismissInputDialog()
                }
            }
        }
        inputDialog?.show(supportFragmentManager, "")
    }

    private fun dismissInputDialog() {
        inputDialog?.let {
            it.dismiss()
            inputDialog = null
        }
    }

    private fun doSaveIcon(bitmap: String) {
        AppExecutors.Default.execute {
            val user = UserManager.getCurrentUser() ?: return@execute
            TencentCOS.upload(this, bitmap) {
                user.picture = it
                UserManager.setCurrentUser(user)
                TrafficSystemRoomBase.getRoomBase(this).userDao().update(user)
            }
        }
    }

    private fun doSaveNickname(name: String) {
        AppExecutors.Default.execute {
            val user = UserManager.getCurrentUser() ?: return@execute
            user.nickname = name
            UserManager.setCurrentUser(user)
            TrafficSystemRoomBase.getRoomBase(this).userDao().update(user)
        }
        user_nick_name.text = name
    }

    private fun doSavePassword(password: String) {
        AppExecutors.Default.execute {
            val user = UserManager.getCurrentUser() ?: return@execute
            user.password = password
            UserManager.setCurrentUser(user)
            TrafficSystemRoomBase.getRoomBase(this).userDao().update(user)
        }
        user_password.text = getPassword(password)
    }
}