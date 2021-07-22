package com.kop.daegudot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kop.daegudot.AddSchedule.AddScheduleFragment;
import com.kop.daegudot.MorePage.MoreFragment;
import com.kop.daegudot.MySchedule.MyScheduleFragment;
import com.kop.daegudot.Recommend.RecommendFragment;
import com.kop.daegudot.Network.Contributor;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomBtns;
    private Fragment[] fragments;
    private TextView[] textViews;

    private FragmentManager fm;
    private FragmentTransaction ft;

    Fragment currentFragment;
    int mCurrentFragNum;
    //private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private long backKeyPressedTime = 0;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBtns = new ImageButton[]{
                findViewById(R.id.myScheduleBtn),
                findViewById(R.id.addScheduleBtn),
                findViewById(R.id.recommendBtn),
                findViewById(R.id.moreBtn)
        };

        textViews = new TextView[] {
                findViewById(R.id.mySch_tv),
                findViewById(R.id.addSch_tv),
                findViewById(R.id.recom_tv),
                findViewById(R.id.more_tv),
        };

        fragments = new Fragment[] {
                new MyScheduleFragment(), new AddScheduleFragment(),
                new RecommendFragment(), new MoreFragment()
        };

        for (int i = 0; i < fragments.length; i++) {
            final Fragment fragment = fragments[i];
            final int mNextFragNum = i;
            bottomBtns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fm = getSupportFragmentManager();
                    ft = fm.beginTransaction().replace(R.id.fragment, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    setBottomBtnsUI(mCurrentFragNum, mNextFragNum);

                    currentFragment = fragment;
                    mCurrentFragNum = mNextFragNum;
                }
            });
        }

        //startRx();

        // MyScheduleFragment 첫 화면
        currentFragment = fragments[0];
        mCurrentFragNum = 0;

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().replace(R.id.fragment, fragments[0]);
        ft.commit();
    }

    public void setBottomBtnsUI(int current, int next) {
        // 클릭한 버튼이 다르면
        if (current != next) {
            textViews[current].setTextColor(getResources().getColor(R.color.lightGray, getTheme()));
            if (current == 0) { // 현재 버튼 태두리
                bottomBtns[current].setImageResource(R.drawable.sch);
            } else if (current == 1) {
                bottomBtns[current].setImageResource(R.drawable.addsch);
            } else if (current == 2) {
                bottomBtns[current].setImageResource(R.drawable.recom);
            } else if (current == 3) {
                bottomBtns[current].setImageResource(R.drawable.more);
            }

            textViews[next].setTextColor(getResources().getColor(R.color.colorPrimary, getTheme()));
            if (next == 0) { // 다음 버튼 색칠
                bottomBtns[next].setImageResource(R.drawable.sch_colored);
            } else if (next == 1) {
                bottomBtns[next].setImageResource(R.drawable.addsch_colored);
            } else if (next == 2) {
                bottomBtns[next].setImageResource(R.drawable.recom_colored);
            } else if (next == 3) {
                bottomBtns[next].setImageResource(R.drawable.more_colored);
            }
        }
    }
    
    public void changeFragment(int from, int to) {
        Fragment fragment = fragments[to];
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().replace(R.id.fragment, fragment);
        ft.addToBackStack(null);
        ft.commit();
        setBottomBtnsUI(from, to);
        
        currentFragment = fragment;
        mCurrentFragNum = to;
    }
    
    @Override
    public void onBackPressed() {
        toast = Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료", Toast.LENGTH_SHORT);
        
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            moveTaskToBack(true);
            finishAndRemoveTask();
            android.os.Process.killProcess(android.os.Process.myPid());
            toast.cancel();
        }
        super.onBackPressed();
    }
    
    /**
     * retrofit + okHttp + rxJava
     */
    /*private void startRx() {
        RestApiService service = RestfulAdapter.getInstance().getServiceApi();
        Observable<List<Contributor>> observable = service.getObContributors(sName, sRepo);

        mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Contributor>>() {
                    @Override
                    public void onNext(List<Contributor> contributors) {
                        for (Contributor c : contributors) {
                            Log.d("RX", c.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("RX", e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("RX", "complete");
                    }
                })


        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }*/
}
