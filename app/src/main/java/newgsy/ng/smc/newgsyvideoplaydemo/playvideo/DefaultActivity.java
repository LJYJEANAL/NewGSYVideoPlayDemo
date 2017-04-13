package newgsy.ng.smc.newgsyvideoplaydemo.playvideo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.GSYPreViewManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import newgsy.ng.smc.newgsyvideoplaydemo.R;
import newgsy.ng.smc.newgsyvideoplaydemo.listener.VideoAllCallBackListener;

public class DefaultActivity extends AppCompatActivity {

    private StandardGSYVideoPlayer default_video_player;
    private Button start_play;
    private OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        default_video_player = (StandardGSYVideoPlayer) findViewById(R.id.default_video_player);
        start_play = (Button) findViewById(R.id.start_play);
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.xxx1);
        default_video_player.setThumbImageView(imageView);
        String source2 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
        default_video_player.setUp(source2,true,"");
        default_video_player.setThumbPlay(true);//是否点击封面可以播放
        default_video_player.getTitleTextView().setVisibility(View.VISIBLE);
        default_video_player.getTitleTextView().setText("默认播放器");
         //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, default_video_player);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);
        default_video_player.setIsTouchWiget(true);
        //开启自动旋转
        default_video_player.setRotateViewAuto(false);
        //全屏首先横屏
        default_video_player.setLockLand(false);
        //是否需要全屏动画效果
        default_video_player.setShowFullAnimation(false);

        default_video_player.setNeedLockFull(true);

//       default_video_player.getCurrentPositionWhenPlaying();
//        default_video_player.setOpenPreView(true);
        default_video_player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();
                start_play.setVisibility(View.GONE);
                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                default_video_player.startWindowFullscreen(DefaultActivity.this, true, true);
            }
        });
        start_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                default_video_player.startPlayLogic();//开始播放
            }
        });
        default_video_player.setStandardVideoAllCallBack(new VideoAllCallBackListener(){
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
                orientationUtils.setEnable(true);
                isPlay = true;
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                if (orientationUtils != null) {
                    orientationUtils.backToProtVideo();
                }
            }

          }
         );
    }

    private boolean isPlay;
    private boolean isPause;
    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        default_video_player.release();
        default_video_player.setStandardVideoAllCallBack(null);

        GSYVideoPlayer.releaseAllVideos();
        GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    /**
     * 全屏的时候按返回键返回竖屏
     */
    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!default_video_player.isIfCurrentIsFullscreen()) {
                    start_play.setVisibility(View.GONE);
                    //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                    default_video_player.startWindowFullscreen(DefaultActivity.this, true, true);
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (default_video_player.isIfCurrentIsFullscreen()) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                if (orientationUtils != null) {
                    orientationUtils.setEnable(true);
                }
            }
        }
    }
}
