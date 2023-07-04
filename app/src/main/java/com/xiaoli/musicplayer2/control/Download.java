package com.xiaoli.musicplayer2.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xiaoli.musicplayer2.R;
import com.xiaoli.musicplayer2.domain.MusicListInfo;
import com.xiaoli.musicplayer2.utils.DlCRUD;
import com.xiaoli.musicplayer2.utils.CRUD;
import com.xiaoli.musicplayer2.utils.MenuList;

import java.io.File;

public class Download extends AppCompatActivity {
    private ListView mDl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mDl = findViewById(R.id.lv_dl);
        setDl();
    }
    private void setDl() {
        File parentFile = new File(getFilesDir(),"/music");
        new DlCRUD().getDL(parentFile);
            dlAdapter aldapter=new dlAdapter();
            if (aldapter.getCount()!=0)
             mDl.setAdapter(aldapter);
            else{
                Toast.makeText(Download.this, "没有本地音乐！请先下载", Toast.LENGTH_SHORT).show();
                finish();}
    }
    class dlAdapter extends BaseAdapter{
        @Override
        public int getCount() {return DlCRUD.sDlList.size();}
        @Override
        public Object getItem(int position) {return null;}
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dllist, null);
            String fileName = DlCRUD.sDlList.get(position);
            TextView id = view.findViewById(R.id.tv_dl);
            id.setText(fileName);

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Download.this);
                    builder.setMessage(fileName);
                    builder.setCancelable(true);
                    builder.setTitle("删除确认");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(new DlCRUD().deleteMusic(new File(getFilesDir(),"/music/"+fileName))){
                                Toast.makeText(Download.this, "删除成功", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());
                            }else {
                                Toast.makeText(Download.this, "删除失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return false;
                }
            });
            return view;
        }
    }
}
