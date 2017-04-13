package com.example.moreimage;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;


import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;

public class MainActivity extends Activity {


    private Button bt;
    private List<Map<String, Bitmap>> list;
    private RxPermissions mRxPermissions;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int REQUEST_SMALL = 111;
    private Calendar calendar;
    private long systemTime1;
    private long systemTime2;
    private GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRxPermissions = new RxPermissions(this);
        calendar = Calendar.getInstance();
        bt = (Button) findViewById(R.id.query);
        gridView = ((GridView) findViewById(R.id.ryMain));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxPermissions.requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) {

                                if (permission.granted) {
                                    // 打开相机，
                                    small();

                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    //拒绝
                                    Toast.makeText(MainActivity.this, "您拒绝了打开相机的权限，无法完成", Toast.LENGTH_SHORT);
                                } else {
                                    // gotoSetting();
                                }
                            }
                        });

            }
        });


    }


    private void getContactList() {
        mRxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {

                        if (permission.granted) {
                            //  savePicture();

                            String[] projection = {MediaStore.Images.Media._ID,
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    MediaStore.Images.Media.DATA};
                            String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
                            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            List<MyBitmap> list2 = getContentProvider(uri, projection, orderBy);//抽取接口到时候
                            Log.e("list", "call: " + list2.toString() + ".size" + list2.size());
                            if (list2 != null) {
                                if (list2.size() > 7) {
                                    list2 = list2.subList(list2.size() - 7, list2.size());
                                }
                                gridView.setAdapter(new Myadapter(MainActivity.this, list2));
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            //拒绝
                            Toast.makeText(MainActivity.this, "您拒绝了读取照片的权限", Toast.LENGTH_SHORT);
                        } else {
                            // gotoSetting();
                        }
                    }
                });


    }

    /**
     * 获取ContentProvider
     *
     * @param projection
     * @param orderBy
     */
    public List<MyBitmap> getContentProvider(Uri uri, String[] projection, String orderBy) {
        // TODO Auto-generated method stub

        List<MyBitmap> lists = new ArrayList<MyBitmap>();
        HashSet<String> set = new HashSet<String>();
        Cursor cursor = getContentResolver().query(uri, projection, null,
                null, orderBy);
        if (null == cursor) {
            return null;
        }

        while (cursor.moveToNext()) {
            Log.e("lengthpro", "getContentProvider: " + projection.length);
            for (int i = 0; i < projection.length; i++) {
                String string = cursor.getString(i);
                if (string != null) {
                    int length = string.length();
                    String ss = null;
                    if (length >= 30) {//根据实际路径得到的。大一点保险
                        ss = string.substring(length - 23, length);
                        String substring = ss.substring(0, 4);//大致判断一下是系统图片，后面严格塞选
                        String hen = ss.substring(12, 13);
                        if (substring.equals("IMG_") && hen.equals("_")) {
                            String laststring = ss.substring(4, 19).replace("_", "");

                            try {
                                long time = Long.valueOf(laststring).longValue();
                                if (time > systemTime1 && time <= systemTime2) {

                                    set.add(string);

                                }
                            } catch (Exception e) {
                                Log.e("exception", "getContentProvider: " + e.toString());

                            }

                        }

                    }

                }

            }
        }

        for (String strings : set) {

            Log.e("setsize", "getContentProvider: " + strings);
            try {
                Bitmap bitmap = convertToBitmap(strings,300,300);

                MyBitmap myBitmap = new MyBitmap(strings, bitmap);
                lists.add(myBitmap);
            } catch (Exception e) {
                Log.e("exceptionee", "getSystemTime: " + e.toString());

            }

        }

        return lists;
    }

    public void small() {
        //打开相机之前，记录时间1
        systemTime1 = getSystemTime();

        Intent intent = new Intent();

        intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);

        startActivityForResult(intent, REQUEST_SMALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("data", "onActivityResult: " + data);
        //关闭相机之后获得时间；2；
        systemTime2 = getSystemTime();

        if (requestCode == REQUEST_SMALL) {
            //
            getContactList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public long getSystemTime() {
//("yyyy年MM月dd日 HH时MM分ss秒"
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        long times = System.currentTimeMillis();
        System.out.println(times);
        Date date = new Date(times);
        String time = sdf.format(date).toString();
        Log.e("timeintimet", "timeint: " + time.toString());
        long timeint = 0;
        try {
            ;
            timeint = Long.valueOf(time).longValue();

        } catch (Exception e) {
            Log.e("exception", "getSystemTime: " + e.toString());
        }


        return timeint;
    }

    public Bitmap convertToBitmap(String filePath, int destWidth, int destHeight) {



        //第一采样
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int sampleSize = 1;
        while ((outWidth / sampleSize > destWidth) || (outHeight / sampleSize > destHeight)) {

            sampleSize *= 2;
        }
        //第二次采样
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(filePath, options);

    }

}
