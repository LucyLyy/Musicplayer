package com.xiaoli.musicplayer2.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.xiaoli.musicplayer2.domain.MusicListInfo;

import java.util.ArrayList;
import java.util.List;

public class CRUD {
    private static final String TAG = "CRUD";
    public void likeAdd(Context context, int position){
        String like_id = MenuList.sMusicListInfo.get(position).getId();
        Log.i(TAG,"音乐的ID："+MenuList.sMusicListInfo.get(position).toString());
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("like_id",like_id);
            long l =db.insert("like_info",null,contentValues);
            if(l == -1){
                Toast.makeText(context, "该歌曲已经添加为我的喜爱", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "加入我的喜爱成功", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
        //db.close();
    }
    public void listAdd(Context context, int position){
        String list_id = MenuList.sMusicListInfo.get(position).getId();
        Log.i(TAG,"音乐的ID："+MenuList.sMusicListInfo.get(position).toString());
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("list_id",list_id);
            long l =db.insert("list_info",null,contentValues);
            if(l == -1){
                Toast.makeText(context, "该歌曲已经添加", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "加入列表成功", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}
        //db.close();
    }
    public void likeDelete(Context context, int position){
        MusicListInfo info = MenuList.sMusicListInfo.get(position);
        String like_id = info.getId();
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("like_id",like_id);
        int i = db.delete("like_info","like_id = ?",new String[]{like_id});
        if(i == 0){
            Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,info.getName()+"已从你的喜爱列表中删除",Toast.LENGTH_SHORT).show();
        }
        //db.close();
    }public void listDelete(Context context, int position){
        MusicListInfo info = MenuList.sMusicListInfo.get(position);
        String list_id = info.getId();
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("list_id",list_id);
        int i = db.delete("list_info","list_id = ?",new String[]{list_id});
        if(i == 0){
            Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,info.getName()+"已从你的列表中删除",Toast.LENGTH_SHORT).show();
        }
       // db.close();
    }

    public boolean likeSelete(Context context,int count){
        int mCount = count;
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        List<String> likeIds = new ArrayList<String>();
        Cursor cs = db.query("like_info",null,null,null,null,null,null);
        while(cs.moveToNext()){
            String value = cs.getString(cs.getColumnIndex("like_id"));
            likeIds.add(value);
        }
        cs.close();
        //db.close();
        if(likeIds.size()<mCount){
            mCount = likeIds.size();
        }
        if (likeIds.size()==0){
            return false;
        }
        new MenuList(likeIds,mCount);
        return true;
    }
    public boolean listSelete(Context context,int count){
        int mCount = count;
        SqlHelper helper = new SqlHelper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        List<String> listIds = new ArrayList<String>();
        Cursor cs = db.query("list_info",null,null,null,null,null,null);
        while(cs.moveToNext()){
            String value = cs.getString(cs.getColumnIndex("list_id"));
            listIds.add(value);
        }
        cs.close();
       // db.close();
        if(listIds.size()<mCount){
            mCount = listIds.size();
        }
        if (listIds.size()==0){
            return false;
        }
        new MenuList(listIds,mCount);
        return true;
    }
}
