package newgsy.ng.smc.newgsyvideoplaydemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shuyu.gsyvideoplayer.utils.ListVideoUtil;

import java.util.ArrayList;
import java.util.List;

import newgsy.ng.smc.newgsyvideoplaydemo.R;

/**
 * Created by Administrator on 2017/3/24.
 */

public class RecyclerAdapter extends RecyclerView.Adapter <RecyclerAdapter.MyViewHolder>{
    private final static String TAG = "RecyclerBaseAdapter";
    private List<Integer> list = new ArrayList<>();
    private Context context = null;
    private ListVideoUtil listVideoUtil;
    private MyViewHolder holder;

    public RecyclerAdapter(Context context,ListVideoUtil listVideoUtil) {
        this.context = context;
        this.listVideoUtil=listVideoUtil;
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(context).inflate(R.layout.list_video_item, parent, false);
            holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final  int position) {

        //增加封面
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.imageView.setImageResource(R.mipmap.xxx1);

        listVideoUtil.addVideoPlayer(position, holder.imageView, TAG, holder.videoContainer, null);

//        holder.playerBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                notifyDataSetChanged();
                //listVideoUtil.setLoop(true);
                listVideoUtil.setPlayPositionAndTag(position, TAG);
                final String url = "http://baobab.wdjcdn.com/14564977406580.mp4";
                //listVideoUtil.setCachePath(new File(FileUtils.getPath()));
                listVideoUtil.startPlay(url);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout  videoContainer;
//        ImageView playerBtn;
        ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
          videoContainer=(FrameLayout)view.findViewById(R.id.list_item_container);
//          playerBtn = (ImageView) view.findViewById(R.id.list_item_btn);
           imageView = new ImageView(context);
        }
    }
}
