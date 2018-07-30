package com.example.xptmx.myapp1;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;

public class Disater_message extends AppCompatActivity implements AbsListView.OnScrollListener{

    ListView listView;
    MyListAdapter myListAdapter;
    private boolean lastItemVisibleFlag = false;
    private static int page = 3;
    private ProgressBar progressBar;
    private boolean mLockListView = false;
    boolean isTop=true;
    boolean firstDragFlag = true;
    boolean dragFlag = false;   //현재 터치가 드래그 인지 확인
    float startYPosition = 0;
    float endYPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disater_message);

        listView = findViewById(R.id.my_listview);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);


        myListAdapter = new MyListAdapter(Disater_message.this, MyGlobals.getInstance().getList_itemArrayList());
        listView.setAdapter(myListAdapter);

        listView.setOnScrollListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),Disaster_message_onClicked.class);
                intent.putExtra("create_date",MyGlobals.getInstance().getList_itemArrayList().get(position).getCreate_date());
                intent.putExtra("location_id",MyGlobals.getInstance().getList_itemArrayList().get(position).getLocation_id());
                intent.putExtra("location_name",MyGlobals.getInstance().getList_itemArrayList().get(position).getLocation_name());
                intent.putExtra("md101_sn",MyGlobals.getInstance().getList_itemArrayList().get(position).getMd101_sn());
                intent.putExtra("msg",MyGlobals.getInstance().getList_itemArrayList().get(position).getContent());
                startActivity(intent);
            }
        });

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"새로고침 완료",Toast.LENGTH_SHORT).show();
                MyGlobals.getInstance().listDestroy();
                page=1;
                //progressBar.setVisibility(View.VISIBLE);
                getItem();
                listView.setSelection(0);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        /*listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:       //터치를 한 후 움직이고 있으면
                        dragFlag = true;
                        if(firstDragFlag) {     //터치후 계속 드래그 하고 있다면 ACTION_MOVE가 계속 일어날 것임으로 무브를 시작한 첫번째 터치만 값을 저장함
                            startYPosition = event.getY(); //첫번째 터치의 Y(높이)를 저장
                            firstDragFlag= false;   //두번째 MOVE가 실행되지 못하도록 플래그 변경
                        }
                            return false;

                    case MotionEvent.ACTION_UP :
                        endYPosition = event.getY();
                        firstDragFlag= true;

                        if(dragFlag) {  //드래그를 하다가 터치를 실행
                            // 시작Y가 끝 Y보다 크다면 터치가 아래서 위로 이루어졌다는 것이고, 스크롤은 아래로내려갔다는 뜻이다.
                            // (startYPosition - endYPosition) > 10 은 터치로 이동한 거리가 10픽셀 이상은 이동해야 스크롤 이동으로 감지하겠다는 뜻임으로 필요하지 않으면 제거해도 된다.
                            if((startYPosition > endYPosition) && (startYPosition - endYPosition) > 10) {
                                //TODO 스크롤 다운 시 작업
                                if(isTop)
                                {
                                    Toast.makeText(getApplicationContext(),"최상단에서 드래그 실행됨",Toast.LENGTH_SHORT);
                                }
                            }
                            //시작 Y가 끝 보다 작다면 터치가 위에서 아래로 이러우졌다는 것이고, 스크롤이 올라갔다는 뜻이다.
                            else if((startYPosition < endYPosition) && (endYPosition - startYPosition) > 10) {
                                //TODO 스크롤 업 시 작업
                            }
                        }

                        startYPosition = 0.0f;
                        endYPosition = 0.0f;
                        //motionFlag = false;
                        break;
                }
                return true;
            }
        });*/

        BottomNavigationView bottomNavigation = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent1=new Intent(Disater_message.this,MainActivity.class);
                        startActivity(intent1);
                        return true;
                    case R.id.navigation_shelter:
                        Intent intent2=new Intent(Disater_message.this,Shelter.class);
                        startActivity(intent2);
                        return true;
                    case R.id.navigation_message:
                        return true;
                    case R.id.navigation_tips:
                        Intent intent3=new Intent(Disater_message.this,Tips.class);
                        startActivity(intent3);
                        return true;
                    case R.id.navigation_setting:
                        Intent intent4=new Intent(Disater_message.this,Setting.class);
                        startActivity(intent4);
                        return true;
                }
                return false;
            }
        });

        Menu menu=bottomNavigation.getMenu();
        MenuItem menuItem=menu.getItem(2);
        menuItem.setChecked(true);

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        // 1. OnScrollListener.SCROLL_STATE_IDLE : 스크롤이 이동하지 않을때의 이벤트(즉 스크롤이 멈추었을때).
        // 2. lastItemVisibleFlag : 리스트뷰의 마지막 셀의 끝에 스크롤이 이동했을때.
        // 3. mLockListView == false : 데이터 리스트에 다음 데이터를 불러오는 작업이 끝났을때.
        // 1, 2, 3 모두가 true일때 다음 데이터를 불러온다.
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
            // 화면이 바닦에 닿을때 처리
            // 로딩중을 알리는 프로그레스바를 보인다.
            progressBar.setVisibility(View.VISIBLE);

            // 다음 데이터를 불러온다.
            getItem();
        }
        /*if(isTop&&scrollState==AbsListView.OnScrollListener.SCROLL_STATE_IDLE&&mLockListView == false) {
            Toast.makeText(getApplicationContext(),"최상단에서 드래그 실행됨",Toast.LENGTH_SHORT);
        }*/
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        /*if(firstVisibleItem == 0 && view.getChildAt(0) != null && view.getChildAt(0).getTop() == 0){
            isTop=true;
        }
        else
        {
            isTop=false;
        }*/
    }

    private void getItem(){

        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        // 다음 10개의 데이터를 불러와서 리스트에 저장한다.
        MyGlobals.getInstance().makeData(page);
        MyGlobals.getInstance().makeData(page+1);

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        Thread thread=new Thread(new Runnable(){
            @Override
            public void run(){
                for(int i=0;i<=100;i++)
                {
                    progressBar.setProgress(i);
                    try{
                        Thread.sleep(8);
                    }catch(Exception e)
                    {

                    }
                }
            }
        });
        thread.start();

        page+=2;
        myListAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.VISIBLE);
        mLockListView = false;
    }

    /*public void onClick_2(View v)
    {
        MyGlobals.getInstance().listDestroy();
        page=1;
        progressBar.setVisibility(View.VISIBLE);
        getItem();
        listView.setSelection(0);
    }*/

}
