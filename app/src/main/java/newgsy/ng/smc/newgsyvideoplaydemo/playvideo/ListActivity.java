package newgsy.ng.smc.newgsyvideoplaydemo.playvideo;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import newgsy.ng.smc.newgsyvideoplaydemo.R;
import newgsy.ng.smc.newgsyvideoplaydemo.adapter.ListAdapter;

public class ListActivity extends AppCompatActivity {

    private ListAdapter listAdapter;
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
        setContentView(R.layout.activity_list);
        ListView lv = (ListView) findViewById(R.id.lv);
        FrameLayout videoFullContainer = (FrameLayout) findViewById(R.id.video_full_container);
        listVideoUtil = new ListVideoUtil(this);
        listVideoUtil.setFullViewContainer(videoFullContainer);
        listVideoUtil.setHideStatusBar(true);
        //listVideoUtil.setHideActionBar(true);
        listVideoUtil.setNeedLockFull(true);
        listAdapter = new ListAdapter(this, ListActivity.this);
        lv.setAdapter(listAdapter);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                Log.e("信息","firstVisibleItem"+firstVisibleItem+",--->visibleItemCount"+visibleItemCount
                +"--->totalItemCount"+totalItemCount);
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(listAdapter.TAG)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {
//                        //如果滑出去了上面和下面就是否，和今日头条一样
                        GSYVideoPlayer.releaseAllVideos();
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }
}
