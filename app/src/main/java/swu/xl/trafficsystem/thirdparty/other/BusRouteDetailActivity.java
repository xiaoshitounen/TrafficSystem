package swu.xl.trafficsystem.thirdparty.other;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import swu.xl.trafficsystem.R;
import swu.xl.trafficsystem.amap.AMapUtil;
import swu.xl.trafficsystem.manager.MapRouteManager;
import swu.xl.trafficsystem.manager.UserManager;
import swu.xl.trafficsystem.sql.RoomHelper;
import swu.xl.trafficsystem.sql.TrafficSystemRoomBase;
import swu.xl.trafficsystem.sql.dao.LoveDao;
import swu.xl.trafficsystem.sql.entity.LoveEntity;
import swu.xl.trafficsystem.util.AppExecutors;
import swu.xl.trafficsystem.util.ThreadUtil;
import swu.xl.trafficsystem.util.ToastUtil;

public class BusRouteDetailActivity extends Activity implements OnMapLoadedListener,
		OnMapClickListener, InfoWindowAdapter, OnInfoWindowClickListener, OnMarkerClickListener {
	private AMap aMap;
	private MapView mapView;
	private BusPath mBuspath;
	private ListView mBusSegmentList;
	private BusSegmentListAdapter mBusSegmentListAdapter;
	private BusRouteOverlay mBusrouteOverlay;
	private ImageView love;
	private boolean hasLoved = false;

	private LatLonPoint start;
	private LatLonPoint target;

	public static void start(Context context, BusPath path, LatLonPoint start, LatLonPoint target) {
		Intent intent = new Intent(context, BusRouteDetailActivity.class);
		intent.putExtra("bus_path", path);
		intent.putExtra("start", start);
		intent.putExtra("target", target);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
					View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
					View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
			);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
			getWindow().setNavigationBarColor(Color.TRANSPARENT);
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		mapView = (MapView) findViewById(R.id.route_map);
		love = findViewById(R.id.route_line_love);
		mapView.onCreate(savedInstanceState);// ?????????????????????
		getIntentData();
		init();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			mBuspath = intent.getParcelableExtra("bus_path");
			start = intent.getParcelableExtra("start");
			target = intent.getParcelableExtra("target");
		}
	}

	private void init() {
		initStaticView();
		registerListener();
		configureListView();
		initMap();
		initLove();
	}

	private void initLove() {
		final LoveDao dao = TrafficSystemRoomBase.Companion.getRoomBase(getApplicationContext()).loveDao();

		AppExecutors.getIO().execute(new Runnable() {
			@Override
			public void run() {
				if (UserManager.INSTANCE.isUserLogin()) {
					final List<LoveEntity> loves = dao.queryAll(UserManager.INSTANCE.getCurrentUser().getId());
					for (final LoveEntity loveEntity : loves) {
						if (TextUtils.equals(loveEntity.getTarget(), MapRouteManager.INSTANCE.getLine().getEnd().getName())) {
							if (TextUtils.equals(loveEntity.getSteps().get(0).getName(), AMapUtil.getBusStepList(mBuspath).get(0).getName())) {
								if (loveEntity.getSteps().size() == AMapUtil.getBusStepList(mBuspath).size()) {
									ThreadUtil.INSTANCE.runOnUiThread(new Function0<Unit>() {
										@Override
										public Unit invoke() {
											love.setImageResource(R.drawable.route_line_select);
											return null;
										}
									});
								}
							}
							return;
						}
					}
				}
			}
		});

		love.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (UserManager.INSTANCE.isUserLogin()) {
					if (hasLoved) {
						love.setImageResource(R.drawable.route_line_normal);

						AppExecutors.getIO().execute(new Runnable() {
							@Override
							public void run() {
								final List<LoveEntity> loves = dao.queryAll(UserManager.INSTANCE.getCurrentUser().getId());
								for (LoveEntity loveEntity : loves) {
									if (TextUtils.equals(loveEntity.getTarget(), MapRouteManager.INSTANCE.getLine().getEnd().getName())) {
										if (TextUtils.equals(loveEntity.getSteps().get(0).getName(), AMapUtil.getBusStepList(mBuspath).get(0).getName())) {
											if (loveEntity.getSteps().size() == AMapUtil.getBusStepList(mBuspath).size()) {
												dao.delete(loveEntity);
												return;
											}
										}
										return;
									}
								}
							}
						});
					} else {
						love.setImageResource(R.drawable.route_line_select);

						AppExecutors.getIO().execute(new Runnable() {
							@Override
							public void run() {
								String start = MapRouteManager.INSTANCE.getLine().getStart().getName();
								if (TextUtils.equals(start, "????????????")) {
									start = MapRouteManager.INSTANCE.getCurrentAddress();
								}
								LoveEntity love = new LoveEntity(
										0,
										start,
										MapRouteManager.INSTANCE.getLine().getEnd().getName(),
										AMapUtil.getFriendlyTime((int) mBuspath.getDuration()),
										AMapUtil.getFriendlyLength((int) mBuspath.getWalkDistance()),
										AMapUtil.getBusStepList(mBuspath),
										UserManager.INSTANCE.getCurrentUser().getId()
								);
								dao.insert(love);
							}
						});
					}

					hasLoved = !hasLoved;
				} else {
					ToastUtil.INSTANCE.toast("????????????");
				}
			}
		});
	}

	@SuppressLint("SetTextI18n")
	private void initStaticView() {
		//??????
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.getUiSettings().setZoomControlsEnabled(false);
		}
		//??????
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//????????????
		TextView walk = findViewById(R.id.route_walk);
		walk.setText("?????????" + AMapUtil.getFriendlyLength((int) mBuspath.getWalkDistance()));
		//????????????
		TextView cost = findViewById(R.id.route_cost);
		cost.setText((int) mBuspath.getCost() + "???");
		//??????
		int number = 0;
		for (int i = 0; i < mBuspath.getSteps().size(); i++) {
			if (mBuspath.getSteps().get(i).getBusLines().size() != 0) {
				number += mBuspath.getSteps().get(i).getBusLines().get(0).getPassStationNum() + 1;
			}
		}
		TextView station = findViewById(R.id.route_station_number);
		station.setText(number + "???");
		//?????????
		TextView time = findViewById(R.id.route_time);
		time.setText("?????? " + AMapUtil.getFriendlyTime((int) mBuspath.getDuration()));
	}

	private void initMap() {
		aMap.clear();// ?????????????????????????????????
		mBusrouteOverlay = new BusRouteOverlay(this, aMap,
				mBuspath, start, target);
		mBusrouteOverlay.removeFromMap();
	}

	private void registerListener() {
		aMap.setOnMapLoadedListener(this);
		aMap.setOnMapClickListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);
	}

	private void configureListView() {
		mBusSegmentList = (ListView) findViewById(R.id.bus_segment_list);
		mBusSegmentListAdapter = new BusSegmentListAdapter(
				this.getApplicationContext(), mBuspath.getSteps());
		mBusSegmentList.setAdapter(mBusSegmentListAdapter);
	}

	@Override
	public void onMapLoaded() {
		if (mBusrouteOverlay != null) {
			mBusrouteOverlay.addToMap();
			mBusrouteOverlay.zoomToSpan();
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
