package com.xiaoli.musicplayer2.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.xiaoli.musicplayer2.control.Main;
import com.xiaoli.musicplayer2.control.Player;
import com.xiaoli.musicplayer2.domain.MusicListInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class DlCRUD {

    private static final String TAG = "DlCRUD";
    private  String mUrl;
    private  File mFile;
    public static List<String> sDlList;

    public void getDL(File parentFile){
        sDlList = new ArrayList<String>();
        for(File file : parentFile.listFiles()){
            if (file != null) {
                sDlList.add(file.getName());
            }
        }
    }

    public boolean deleteMusic(File file){
        if (file.exists()){
            file.delete();
            return true;
        }
        return false;
    }

    public void downLoadMusic(int position, File file){
        // 获取音乐列表信息
        MusicListInfo info = MenuList.sMusicListInfo.get(position);
        // 获取音乐下载地址
        mUrl = info.getUrl();
        // 设置保存文件
        mFile = file;
        // 创建OkHttpClient实例
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .build();
        // 创建下载请求
        Request request = new Request.Builder()
                .get()
                .url(mUrl)
                .build();
        // 利用浏览器创建任务
        Call task = client.newCall(request);
        // 异步执行任务
        task.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败回调
                Log.i(TAG,e.toString());
            }
            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                // 请求成功回调
                if(response.code()== HttpURLConnection.HTTP_OK){
                    downLoad(response.body());
                }
            }
        });
    }

    private void downLoad(ResponseBody body) {
        // 获取输入流
        InputStream mInputStream = body.byteStream();
        // 创建消息对象
        Message message = Message.obtain();
        try {
            // 判断文件的父目录是否存在，如果不存在则创建
            if (!mFile.getParentFile().exists()){
                mFile.getParentFile().mkdirs();
            }
            // 如果文件已经存在，则发送消息并返回
            if (mFile.exists()){
                message.what = 303;
                Player.sHandler.sendMessage(message);
                return;
            }
            // 输出文件路径到日志
            Log.i(TAG,mFile.getPath());
            // 创建文件输出流
            FileOutputStream fos = new FileOutputStream(mFile);
            // 创建缓冲区
            byte[] buffer = new byte[1024];
            int len;
            // 从输入流读取数据，并写入到文件中
            while ((len = mInputStream.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }
            // 关闭文件输出流
            fos.close();
            // 发送下载完成的消息
            message.what = 301;
            Player.sHandler.sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
            // 输出错误日志
            Log.i(TAG,e.toString());
            // 发送下载失败的消息
            message.what = 302;
            Player.sHandler.sendMessage(message);
        }
    }
}
