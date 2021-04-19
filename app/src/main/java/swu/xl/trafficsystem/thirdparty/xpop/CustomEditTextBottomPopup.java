package swu.xl.trafficsystem.thirdparty.xpop;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.lxj.xpopup.core.BottomPopupView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import swu.xl.trafficsystem.R;
import swu.xl.trafficsystem.adapter.OnTipClickListener;
import swu.xl.trafficsystem.adapter.TipListAdapter;
import swu.xl.trafficsystem.manager.MapRouteManager;
import swu.xl.trafficsystem.model.MapLocation;
import swu.xl.trafficsystem.ui.activity.RoutePlanActivity;

/**
 * Description: 自定义带有输入框的Bottom弹窗
 * Create by dance, at 2019/2/27
 */
public class CustomEditTextBottomPopup extends BottomPopupView implements Inputtips.InputtipsListener {
    private EditText searchEdit;
    private ImageView back;
    private RecyclerView tipList;
    private TipListAdapter adapter;
    private String city = "重庆";

    public CustomEditTextBottomPopup(@NonNull Context context, String city) {
        super(context);
        this.city = city;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_edittext_bottom_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        adapter = new TipListAdapter();
        searchEdit = findViewById(R.id.map_search_edit);
        back = findViewById(R.id.map_search_back);
        tipList = findViewById(R.id.map_tip_list);
        tipList.setLayoutManager(new LinearLayoutManager(getContext()));
        tipList.setAdapter(adapter);
    }

    @Override
    protected void onShow() {
        super.onShow();
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                doSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        adapter.addOnTipClickListener(new OnTipClickListener() {
            @Override
            public void onTipClick(@NotNull Tip tip) {
                RoutePlanActivity.start(getContext());
                MapRouteManager.INSTANCE.setLine(new MapLocation(tip.getPoint(), tip.getName()));
            }
        });
    }

    private void doSearch(String input) {
        InputtipsQuery query = new InputtipsQuery(input, city);
        //查询严格限制当前城市
        query.setCityLimit(true);
        Inputtips inputTips = new Inputtips(getContext(), query);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void onGetInputtips(List<Tip> tips, int code) {
        adapter.setTips(tips);
    }
}
