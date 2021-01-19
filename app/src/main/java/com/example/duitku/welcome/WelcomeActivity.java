package com.example.duitku.welcome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.duitku.R;
import com.example.duitku.firebase.GetStarted;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    private int mCurrentPage;
    private TextView[] mdots;
    private Button mNextBtn;
    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotLayout = findViewById(R.id.mdotslayout);

        mNextBtn = findViewById(R.id.nextbtn);

        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);

        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letsGo();
            }
        });


    }

    private void letsGo(){
        Intent getStartedIntent = new Intent(this, GetStarted.class);
        startActivity(getStartedIntent);
        this.finish();
    }

    public void addDotsIndicator(int position){
        mdots = new TextView[4];
        mDotLayout.removeAllViews();

        for(int i=0 ; i<mdots.length ; i++){
            mdots[i] = new TextView(this);
            mdots[i].setText(Html.fromHtml("&#8226;"));
            mdots[i].setTextSize(35);
            mdots[i].setTextColor(getResources().getColor(R.color.textDark));

            mDotLayout.addView(mdots[i]);
        }

        if (mdots.length > 0){
            mdots[position].setTextColor(getResources().getColor(R.color.textLight));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener(){

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            mCurrentPage = position;
            if (mCurrentPage != mdots.length-1){
                mNextBtn.setEnabled(false);
                mNextBtn.setVisibility(View.INVISIBLE);
            } else {
                mNextBtn.setEnabled(true);
                mNextBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onBackPressed() {
        // do nothing
    }
}