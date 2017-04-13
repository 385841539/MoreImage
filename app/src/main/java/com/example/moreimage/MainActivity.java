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
    private View pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRxPermissions = new RxPermissions(this);
        calendar = Calendar.getInstance();
        bt = (Button) findViewById(R.id.query);
        pb = findViewById(R.id.pb);
        gridView = ((GridView) findViewById(R.id.ryMain));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRxPermissions.requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Action1<Permission>() {
                            @Override
                            public void call(Permission permission) {

                                if (permission.granted) {
                                    // 打开相机拍照
                                    takeOnCamera();

                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    //拒绝
                                    Toast.makeText(MainActivity.this, "您拒绝了打开相机的权限，无法完成", Toast.LENGTH_SHORT).show();
                                } else {
                                    // gotoSetting();
                                    Toast.makeText(MainActivity.this, "您拒绝了打开相机的权限，无法完成", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("data", "onActivityResult: " + data);
        //关闭相机之后获得时间；2；
        pb.setVisibility(View.VISIBLE);
        systemTime2 = getSystemTime();

        if (requestCode == REQUEST_SMALL) {
            //这里可以拓展不同按钮，给下面的方法传不同的参数
            getContactList();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getContactList() {
        mRxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if (permission.granted) {
                            //  读取照片然后选择合适的照片保存再list里面
                            final String[] projection = {MediaStore.Images.Media._ID,
                                    MediaStore.Images.Media.DISPLAY_NAME,
                                    MediaStore.Images.Media.DATA};
                            final String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
                            final Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    List<MyBitmap> list2 = getContentProvider(uri, projection, orderBy);//到时候抽取接口
                                    Log.e("list", "call: " + list2.toString() + ".size" + list2.size());
                                    if (list2 != null) {
                                        if (list2.size() > 7) {//这里看要求最多几张照片
                                            list2 = list2.subList(list2.size() - 7, list2.size());
                                        }
                                        final List<MyBitmap> finalList = list2;
                                        gridView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                //TODO 拿到数据源然后拓展
                                                gridView.setAdapter(new Myadapter(MainActivity.this, finalList));//拿到了就可以搞了
                                                pb.setVisibility(View.GONE);

                                            }
                                        });

                                    }

                                }
                            }).start();

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            //拒绝
                            Toast.makeText(MainActivity.this, "您拒绝了读取照片的权限", Toast.LENGTH_SHORT).show();

                        } else {
                            // gotoSetting();
                            Toast.makeText(MainActivity.this, "您拒绝了读取照片的权限", Toast.LENGTH_SHORT).show();
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
                Bitmap bitmap = convertToBitmap(strings, 300, 300);

                MyBitmap myBitmap = new MyBitmap(strings, bitmap);
                lists.add(myBitmap);
            } catch (Exception e) {
                Log.e("exceptionee", "getSystemTime: " + e.toString());

            }

        }

        return lists;
    }

    public void takeOnCamera() {
        //打开相机之前，记录时间1
        systemTime1 = getSystemTime();
        Intent intent = new Intent();
        //此处之所以诸多try catch，是因为各大厂商手机不确定哪个方法
        try {
            intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            startActivityForResult(intent, REQUEST_SMALL);
        } catch (Exception e) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivityForResult(intent, REQUEST_SMALL);

            } catch (Exception e1) {
                try {
                    intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
                    startActivityForResult(intent, REQUEST_SMALL);
                } catch (Exception ell) {
                    Toast.makeText(MainActivity.this, "请从相册选择", Toast.LENGTH_SHORT).show();
                }
            }
        }

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

    /**
     * 根据路径，二次采样并且压缩
     * @param filePath 路径
     * @param destWidth 压缩到的宽度
     * @param destHeight 压缩到的高度
     * @return
     */
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
