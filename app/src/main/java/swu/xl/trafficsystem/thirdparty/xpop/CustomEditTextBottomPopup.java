package swu.xl.trafficsystem.thirdparty.xpop;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import swu.xl.trafficsystem.R;

/**
 * Description: 自定义带有输入框的Bottom弹窗
 * Create by dance, at 2019/2/27
 */
public class CustomEditTextBottomPopup extends BottomPopupView {
    public CustomEditTextBottomPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_edittext_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }
}
