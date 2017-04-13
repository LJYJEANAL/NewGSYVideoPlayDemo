package newgsy.ng.smc.newgsyvideoplaydemo.playvideo;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.Window;
import android.widget.FrameLayout;

import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;

import newgsy.ng.smc.newgsyvideoplaydemo.R;
import newgsy.ng.smc.newgsyvideoplaydemo.adapter.RecyclerAdapter;
import newgsy.ng.smc.newgsyvideoplaydemo.listener.VideoAllCallBackListener;

public class RecyclerVideoPlayerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ListVideoUtil listVideoUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            // 设置一个exit transition
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
                getWindow().setEnterTransition(new Explode());
                getWindow().setExitTransition(new Explode());
            }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_video_player);
        initView();
    }
    int lastVisibleItem;
    int firstVisibleItem;
    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.list_item_recycler);
        FrameLayout videoFullContainer = (FrameLayout) findViewById(R.id.video_full_container);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        listVideoUtil = new ListVideoUtil(this);
        listVideoUtil.setFullViewContainer(videoFullContainer);
        listVideoUtil.setHideStatusBar(true);
        final RecyclerAdapter adapter=new RecyclerAdapter(this, listVideoUtil);
        recyclerView.setAdapter(adapter);
        listVideoUtil.setHideActionBar(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem   = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                Debuger.printfLog("firstVisibleItem " + firstVisibleItem +" lastVisibleItem " + lastVisibleItem);
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals("RecyclerBaseAdapter")) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //如果是小窗口就不需要处理
                        if (!listVideoUtil.isSmall() && !listVideoUtil.isFull()) {
                            //小窗口
                            int size = CommonUtil.dip2px(RecyclerVideoPlayerActivity.this, 150);
                            //actionbar为true才不会掉下面去
                            listVideoUtil.showSmallVideo(new Point(size, size), true, true);
                        }
                    } else {
                        if (listVideoUtil.isSmall()) {
                            listVideoUtil.smallVideoToNormal();
                        }
                    }
                }
            }
        });
        //小窗口关闭被点击的时候回调处理回复页面
        listVideoUtil.setVideoAllCallBack(new VideoAllCallBackListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                Debuger.printfLog("Duration " + listVideoUtil.getDuration() + " CurrentPosition " + listVideoUtil.getCurrentPositionWhenPlaying());
            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {
                super.onQuitSmallWidget(url, objects);
                //大于0说明有播放,//对应的播放列表TAG
                if (listVideoUtil.getPlayPosition() >= 0 && listVideoUtil.getPlayTAG().equals("RecyclerBaseAdapter")) {
                    //当前播放的位置
                    int position = listVideoUtil.getPlayPosition();
                    //不可视的是时候
                    if ((position < firstVisibleItem || position > lastVisibleItem)) {
                        //释放掉视频
                        listVideoUtil.releaseVideoPlayer();
                        adapter.notifyDataSetChanged();
                    }
                }

            }
        });
    }
    @Override
    public void onBackPressed() {
        if (listVideoUtil.backFromFull()) {
            return;
        }
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        listVideoUtil.releaseVideoPlayer();
        GSYVideoPlayer.releaseAllVideos();
    }

}
