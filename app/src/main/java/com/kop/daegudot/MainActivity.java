package com.kop.daegudot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kop.daegudot.AddSchedule.AddScheduleFragment;
import com.kop.daegudot.MorePage.MoreFragment;
import com.kop.daegudot.MySchedule.MyScheduleFragment;
import com.kop.daegudot.Recommend.RecommendFragment;

public class MainActivity extends AppCompatActivity {
    private ImageButton[] bottomBtns;
    private Fragment[] fragments;
    private TextView[] textViews;

    private FragmentManager fm;
    private FragmentTransaction ft;

    Fragment currentFragment;
    int mCurrentFragNum;

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
                    ft.commit();
                    setBottomBtnsUI(mCurrentFragNum, mNextFragNum);

                    currentFragment = fragment;
                    mCurrentFragNum = mNextFragNum;
                }
            });
        }

        // MyScheduleFragment 첫 화면
        currentFragment = fragments[0];
        mCurrentFragNum = 0;

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction().replace(R.id.fragment, fragments[0]);
        ft.commit();
    }

    public void setBottomBtnsUI(int current, int next) {
        // 클릭한 버튼이 다르면
        System.out.println("current : "+ current + "next : " + next);
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

}
