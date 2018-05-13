package com.example.wlk.zzuar.utils;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

public class DownloadUtil {
    private Context mContext;

    private String downloadUrl = "http://10.101.252.175";

    private String fileName = "sdfsd.txt";


    public DownloadUtil(Context context){
        this.mContext = context;
    }
    public void t() {

        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request mrequest = new DownloadManager.Request(Uri.parse(downloadUrl));
        //指定下载路径和下载文件名
        mrequest.setDestinationInExternalPublicDir("/download/", fileName);
        //获取下载管理器
        DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        downloadManager.enqueue(mrequest);

    }
}
