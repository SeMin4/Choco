package com.example.xptmx.myapp1;


import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

public class Shelter extends NMapActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "aJwTYEg7xKtZ6e15n2ns";// 애플리케이션 클라이언트 아이디 값
    private NMapController mMapController;
    private final String LOG_TAG = "ABC";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    // create map view
        mMapView = new NMapView(this);

    // 기존 API key 방식은 deprecated 함수이며, 2016년 말까지만 사용 가능합니다.
    // mMapView.setApiKey(API_KEY);

    // set Client ID for Open MapViewer Library
        mMapView.setClientId(CLIENT_ID);

    // set the activity content to the map view
        setContentView(mMapView);

    // initialize map view
        mMapView.setClickable(true);

    // register listener for map state changes
     //   mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
    //    mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);

    // use map controller to zoom in/out, pan and set map center, zoom level etc.
        mMapController = mMapView.getMapController();
        mMapView.setBuiltInZoomControls(true, null);
    }
    public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) { // success
            mMapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);
        } else { // fail
            Log.e(LOG_TAG, "onMapInitHandler: error=" + errorInfo.toString());
        }
    }




}








