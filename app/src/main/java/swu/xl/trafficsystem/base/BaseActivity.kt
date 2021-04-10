package swu.xl.trafficsystem.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())
        initData()
        initListener()
    }

    abstract fun getLayoutId(): Int

    protected open fun initData() {}

    protected open fun initListener() {}

    inline fun <reified T: BaseActivity> startActivityAndFinish() {
        startActivity(Intent(this, T::class.java))
        finish()
    }
}