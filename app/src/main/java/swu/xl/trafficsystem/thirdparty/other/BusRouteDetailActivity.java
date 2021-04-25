package swu.xl.trafficsystem.thirdparty.other;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;

import swu.xl.trafficsystem.R;
import swu.xl.trafficsystem.amap.AMapUtil;

public class BusRouteDetailActivity extends Activity implements OnMapLoadedListener,
		OnMapClickListener, InfoWindowAdapter, OnInfoWindowClickListener, OnMarkerClickListener {
	private AMap aMap;
	private MapView mapView;
	private BusPath mBuspath;
	private BusRouteResult mBusRouteResult;
	private TextView mTitleBusRoute, mDesBusRoute;
	private ListView mBusSegmentList;
	private BusSegmentListAdapter mBusSegmentListAdapter;
	private BusRouteOverlay mBusrouteOverlay;
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
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		getIntentData();
		init();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent != null) {
			mBuspath = intent.getParcelableExtra("bus_path");
			mBusRouteResult = intent.getParcelableExtra("bus_result");
		}
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.getUiSettings().setZoomControlsEnabled(false);
		}
		registerListener();
		mTitleBusRoute = (TextView) findViewById(R.id.firstline);
		mDesBusRoute = (TextView) findViewById(R.id.secondline);
		String dur = AMapUtil.getFriendlyTime((int) mBuspath.getDuration());
		String dis = AMapUtil.getFriendlyLength((int) mBuspath.getDistance());
		mTitleBusRoute.setText(dur + "(" + dis + ")");
		int taxiCost = (int) mBusRouteResult.getTaxiCost();
		mDesBusRoute.setText("打车约"+taxiCost+"元");
		mDesBusRoute.setVisibility(View.VISIBLE);
		configureListView();
		initMap();
	}

	private void initMap() {
		aMap.clear();// 清理地图上的所有覆盖物
		mBusrouteOverlay = new BusRouteOverlay(this, aMap,
				mBuspath, mBusRouteResult.getStartPos(),
				mBusRouteResult.getTargetPos());
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
