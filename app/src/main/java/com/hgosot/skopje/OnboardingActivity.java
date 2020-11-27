package com.hgosot.skopje;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgosot.skopje.adapter.OnBoardingPagerAdapter;
import com.hgosot.skopje.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jonathanfinerty.once.Once;


public class OnboardingActivity extends BaseActivity {
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.page_dot_1)
    ImageView mPageDot1;
    @BindView(R.id.page_dot_2)
    ImageView mPageDot2;
    @BindView(R.id.page_dot_3)
    ImageView mPageDot3;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    private List<ImageView> dotsList = new ArrayList<>();

    private String[] pageHeaders;
    private String[] pageTexts;
    private OnBoardingPagerAdapter adapter;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        Once.markDone("onboarding");
        ButterKnife.bind(this);
        startAudio();
        pageHeaders = getResources().getStringArray(R.array.page_headers);
        pageTexts = getResources().getStringArray(R.array.page_desc);
        tvHeader.setText(pageHeaders[0]);
        tvDesc.setText(pageTexts[0]);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewPager.getCurrentItem()+1 ==adapter.getCount() ) {
                    startActivity(new Intent(OnboardingActivity.this,DrawerActivity.class));
                    finish();
                }else{
                    mViewPager.setCurrentItem(getItem(), true);
                }


            }
        });
        dotsList.add(0, mPageDot1);
        dotsList.add(1, mPageDot2);
        dotsList.add(2, mPageDot3);


        adapter = new OnBoardingPagerAdapter(this);

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvHeader.setText(pageHeaders[position]);
                tvDesc.setText(pageTexts[position]);
                if (position != 0) {
                    tvTitle.setVisibility(View.INVISIBLE);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                }
                for (int i = 0; i < dotsList.size(); i++) {
                    ImageView view = dotsList.get(i);
                    if (i == position) {
                        view.setBackgroundResource(R.drawable.dot_active);
                    } else {
                        view.setBackgroundResource(R.drawable.dot);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    private int getItem() {
        return mViewPager.getCurrentItem() + 1;
    }

    private void startAudio(){

        String fileName = FileUtils.getEntryAudio(getLanguage());

        int resourceId = getResources().getIdentifier(fileName,
                "raw", getPackageName());

        mediaPlayer = MediaPlayer.create(this, resourceId);




    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mediaPlayer != null ) {
            mediaPlayer.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
