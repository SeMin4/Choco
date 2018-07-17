package com.example.xptmx.myapp1;

// NMapActivity를 활용해 간단히 지도를 전체화면으로 표시하는 예제
// 본 예제는 1개의 파일 MainActivity.java 로 구성되어 있습니다.
// (중요) 패키지명은 애플리케이션 설정의 Android 패키지명과 반드시 일치해야 함
import android.os.Bundle;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapView;

public class Shelter extends NMapActivity {

    private NMapView mMapView;// 지도 화면 View
    private final String CLIENT_ID = "aJwTYEg7xKtZ6e15n2ns";// 애플리케이션 클라이언트 아이디 값

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        mMapView = new NMapView(this);
        setContentView(mMapView);
        mMapView.setClientId(CLIENT_ID); // 클라이언트 아이디 값 설정
        mMapView.setClickable(true);
        mMapView.setEnabled(true);
        mMapView.setFocusable(true);
        mMapView.setFocusableInTouchMode(true);
        mMapView.requestFocus();
    }
}








