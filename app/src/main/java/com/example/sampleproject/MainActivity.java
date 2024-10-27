package com.example.sampleproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private LinearLayout overlayLayout;
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        overlayLayout = findViewById(R.id.overlayLayout);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        // 로컬에 저장된 동영상의 Uri를 설정
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lucy_boogieman);
        videoView.setVideoURI(videoUri);

        // 재생 완료 리스너 설정하여 자동 반복 재생
        videoView.setOnCompletionListener(mediaPlayer -> videoView.start());

        // 비율에 맞게 크기 조정
        videoView.setOnPreparedListener(mediaPlayer -> {
            int videoWidth = mediaPlayer.getVideoWidth();
            int videoHeight = mediaPlayer.getVideoHeight();
            float videoProportion = (float) videoWidth / (float) videoHeight;

            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int screenWidth = getResources().getDisplayMetrics().widthPixels;

            LayoutParams layoutParams = (LayoutParams) videoView.getLayoutParams();
            layoutParams.height = screenHeight;
            layoutParams.width = (int) (screenHeight * videoProportion);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            videoView.setLayoutParams(layoutParams);

            videoView.start();  // 비디오 자동 재생
        });

        // 터치 리스너 설정
        View.OnClickListener toggleOverlayVisibility = view -> {
            if (overlayLayout.getVisibility() == View.VISIBLE) {
                overlayLayout.setVisibility(View.GONE);
            } else {
                overlayLayout.setVisibility(View.VISIBLE);
            }
        };

        // VideoView 및 배경 터치 시 레이아웃 보이게 설정
        videoView.setOnClickListener(toggleOverlayVisibility);
        findViewById(R.id.videoView).setOnClickListener(toggleOverlayVisibility); // 배경에 터치 리스너 연결

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.naver.com"));
                startActivity(intent1);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, ScanQr.class);
                startActivity(intent2);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // 앱이 포그라운드로 돌아올 때 동영상 다시 시작
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 앱이 백그라운드로 갈 때 동영상 일시 정지
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }
}