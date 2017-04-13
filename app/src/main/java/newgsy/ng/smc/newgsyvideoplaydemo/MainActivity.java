package newgsy.ng.smc.newgsyvideoplaydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import newgsy.ng.smc.newgsyvideoplaydemo.playvideo.DefaultActivity;
import newgsy.ng.smc.newgsyvideoplaydemo.playvideo.ListActivity;
import newgsy.ng.smc.newgsyvideoplaydemo.playvideo.RecyclerVideoPlayerActivity;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button default_btn = (Button) findViewById(R.id.default_btn);
        default_btn.setOnClickListener(this);
        Button list_btn = (Button) findViewById(R.id.list_btn);
        list_btn.setOnClickListener(this);
        Button recycler_btn = (Button) findViewById(R.id.recycler_btn);
        recycler_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.default_btn:
                //默认播放支持重力
                startActivity(new Intent(this, DefaultActivity.class));
                break;
            case R.id.list_btn:
                //ListView列表
                startActivity(new Intent(this, ListActivity.class));
                break;
            case R.id.recycler_btn:
                //recycler列表支持小窗口播放
                startActivity(new Intent(this, RecyclerVideoPlayerActivity.class));
                break;
        }
    }
}
