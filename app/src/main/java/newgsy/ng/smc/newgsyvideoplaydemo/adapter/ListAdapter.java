package newgsy.ng.smc.newgsyvideoplaydemo.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.List;

import newgsy.ng.smc.newgsyvideoplaydemo.R;
import newgsy.ng.smc.newgsyvideoplaydemo.listener.VideoAllCallBackListener;

/**
 * Created by Administrator on 2017/3/24.
 */

public class ListAdapter extends BaseAdapter{
    private Context context;
   public  Holder holder;
    private Activity activity;
    private List<Integer> list = new ArrayList<>();
    private String url;
    public static final String TAG = "ListAdapter";
    public ListAdapter(Context context,Activity activity) {
        this.context = context;
        this.activity=activity;
        for (int i = 0; i < 40; i++) {
            list.add(i);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            holder=new Holder();
            convertView= LayoutInflater.from(context).inflate(R.layout.list_video_item_default,null);
            holder.lis_VideoPlayer=(StandardGSYVideoPlayer)convertView.findViewById(R.id.list_videoPlay);
            holder.list_item=(TextView)convertView.findViewById(R.id.list_item);
            holder.imageView=new ImageView(context);
            convertView.setTag(holder);
        }else{
            holder=(Holder) convertView.getTag();
        }
        //增加封面
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER);

        if (position%2==0){//取整
            holder.imageView.setImageResource(R.mipmap.xxx1);
            url = "http://baobab.wdjcdn.com/14564977406580.mp4";
        } else {
            url=  "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
            holder.imageView.setImageResource(R.mipmap.xxx2);
        }
        if (holder.imageView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) holder.imageView.getParent();
            viewGroup.removeView(holder.imageView);
        }
            holder.lis_VideoPlayer.setThumbImageView(holder.imageView);
        holder.list_item.setText(""+position);
        holder.lis_VideoPlayer.setUp(url,true,null,""+position);
        holder.lis_VideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        holder.lis_VideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        holder.lis_VideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lis_VideoPlayer.startWindowFullscreen(context,true,true);
            }
        });
        holder.lis_VideoPlayer.setRotateViewAuto(true);
        holder.lis_VideoPlayer.setLockLand(true);
        holder.lis_VideoPlayer.setPlayTag(TAG);
        holder.lis_VideoPlayer.setShowFullAnimation(false);
        //循环
        //holder.gsyVideoPlayer.setLooping(true);
        holder.lis_VideoPlayer.setNeedLockFull(true);
        holder.lis_VideoPlayer.setPlayPosition(position);
        holder.lis_VideoPlayer.setStandardVideoAllCallBack(new VideoAllCallBackListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                if (!holder.lis_VideoPlayer.isIfCurrentIsFullscreen()) {
                    GSYVideoManager.instance().setNeedMute(true);
                }
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(true);
            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                super.onEnterFullscreen(url, objects);
                GSYVideoManager.instance().setNeedMute(false);
            }
        });
        return convertView;
    }
    public class Holder{
        public  StandardGSYVideoPlayer lis_VideoPlayer;
        public  TextView list_item;
        public  ImageView imageView;
    }
}
