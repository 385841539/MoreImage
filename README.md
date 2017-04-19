# MoreImage
 
## 效果图 :

![效果图](https://github.com/385841539/MoreImage/blob/master/app/src/main/res/mipmap-xxhdpi/lastimage0310269.gif)


**今日，产品经理又疯了，搞这么一个功能，也是没事做了，算了，先来说一下具体功能吧，简化来就是：**
###**在一个界面上，点击某个按钮，打开相机，拍很多张照片，返回以后，把刚才所拍的照片显示出来，照片数小于等于7。** 

各位看官，别着急，是不是觉得特简单，不就Intent跳转打开相机，拍几张照片，在**onActivityResult**方法里面拿到刚才拍的照片，显示出来不就可以了么？嗯，是的，是这么简单。我来帮您写过程。


![这里写图片描述](http://img.blog.csdn.net/20170417100804053?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

----------

----------


###**一、第一个想法实现功能**
####**1.1、新建工程，布局文件，初始化View,并添加点击事件：**
这个过程非常的简单，就看下代码，直接跳过。




**布局文件：**
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.exampleenen.ruedy.moreimagetwo.MainActivity">

    <Button
        android:id="@+id/bt_main"
        android:layout_width="200dp"
        android:layout_height="70dp"
        android:layout_centerHorizontal="true"
        android:text="点击打开相机" />

    <GridView
        android:id="@+id/gv_main"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_below="@id/bt_main"
        android:numColumns="3"></GridView>

</RelativeLayout>

```

**初始化View:**

```
public class MainActivity extends AppCompatActivity {

    private View btMain;
    private GridView gvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        btMain = findViewById(R.id.bt_main);
        gvMain = ((GridView) findViewById(R.id.gv_main));
    }
}

```
**1.2、添加点击事件打开相机：**
这里也应该非常的简单和熟悉，直接上代码：

**点击事件：**

```
       btMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putOnCamera();//打开相机的方法
            }
        });
```

**打开相机：**

```
       ACTION_IMAGE_CAPTURE //打开相机拍一次
       ACTION_IMAGE_CAPTURE_SECURE
       INTENT_ACTION_STILL_IMAGE_CAMERA
       INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE
```
这个intent是隐式跳转的，有四个常量，第一个大家都很熟悉，就是打开相机拍一张照片完事，所以我们不选他，那该选择哪个呢？不着急，我们一个个试一下，打个log试试先


**1.2.2、ACTION_IMAGE_CAPTURE_SECURE：**

```
    /**
     * 打开相机
     */
    private void putOnCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
        startActivityForResult(intent, REQUEST_SMALL);
    }
```


在onActivityResult中我们得到返回值

```
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SMALL) {
            Log.e("datadata", "requestCode:"+requestCode+",resultCode:"+resultCode+",data:"+data);
        }

    }
```
开机运行啦，测试机是华为小米的6.0系统，看一下效果，
嗯，不错，打开相机是这样的：


![这里写图片描述](http://img.blog.csdn.net/20170414115306060?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



图片比较大，就贴一张看一下，和直接打开系统相机是一样的，没错，可以多次拍照，哈哈，原来真的这么简单，拍完返回看一下log如下：

```
04-13 16:38:10.934 28785-28785/com.exampleenen.ruedy.moreimagetwo E/datadata: requestCode:11111,resultCode:0,data:null

```

![这里写图片描述](http://img.blog.csdn.net/20170413164518008?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


**那好，咱们看截图：**

![这里写图片描述](http://img.blog.csdn.net/20170413163932328?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


哈哈，眼看就要完成了，最后竟然 得到一个null，**所以这个"ACTION_IMAGE_CAPTURE_SECURE"模式完成不了此功能**，我们再看看另外两个参数，额。。。各位看官，不用试了，小生已经试过了，结果是一样的。
所以嘛，没这么简单，这么简单，我也不会费这么大力气写篇博客了。

###**既然这条路不通过，那咱们怎么办呢？和产品经理say NO? 我看行，等等，那位朋友讲话了:**



![这里写图片描述](http://img.blog.csdn.net/20170413164834856?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

嗯，想想,可以的，您先想，应该有其他方法，我也想到了一个方法，您看这样可以么？前方高能，同志请带好安全帽：

**自己重新写个相机，点击按钮的时候 我们打开自己的相机，在相机里面写个方法，把照片数据都返回出去？嗯，听着可以啊，你去写呗，我可不写，我的项目估计还没写个相机复杂。那好吧，那这样行么？我给两组log数据您看下：**

111小米6.0系统：

```
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165443.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165451.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165447.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165442.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165448.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165449.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165445.jpg
```

222华为7.0系统：

```
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165655.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165652.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165641.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165647.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165643.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165645.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165650.jpg
E/setsize: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_165659.jpg
```
不要问是什么 ，从哪里来的，您就看熟不熟悉？


![这里写图片描述](http://img.blog.csdn.net/20170413165934049?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

对了，是在哪里见过，在哪里呢？打开你手机拍出来的照片，打开照片信息，看一下路径。对嘛，我就说在快播，额不 ， 手机照片上见过嘛。

**有点意思，有点意思**

**可这有个卵用啊 ， 你的data里面还是拿不到刚才拍的照片呀，是的，说得对奥，data还是拿不到打开相机拍得照片，可是。。。。。，这样行么大兄弟，不一定非要从data里面拿呀，咱可以从系统里面拿呀，打开相机是必须的，所以东西还得在onActivityResult里面进行，说的是轻松，从系统里面拿？怎么拿？根据什么拿？ **

问的好啊 ， 那我来说一下我的几个思路吧

**111思路一**：

先看一下这个路径
```
/storage/emulated/0/DCIM/Camera/
```
手机拍的照片好像都在这个手机文件夹下面，直接读取这个文件夹下面的文件，不就能拿到了么。

是的，

不过有个问题，拍摄的照片所在的路径不一定，是的，照片的储存位置用户是可以设置、改变的，所以本着最近《人民的名义》 所教导的精神，做一名良心程序员，咱们还是把这个情况考虑进去吧，所以这个方法有缺陷，很大的缺陷。


**222思路二**：
 安卓有个四大组件之一的内容提供者，也就是ContentProvider，这玩意很少用到吧，**安卓系统把手机里面所有的照片的 路径 （记住是路径啊） 做成了一个数据库** ， 我们可以通过内容提供者**ContentProvider**拿到**所有（记住是所有）**照片的路径，然后根据某些规律 ， 找到 打开相机以后所拍的照片不就可以了吗 ？ 是的，那么问题转化为这个**规律** 是什么 ，仔细看这些数据：

```
/storage/emulated/0/DCIM/Camera/IMG_20170413_165655.jpg
/storage/emulated/0/DCIM/Camera/IMG_20170413_165652.jpg
/storage/emulated/0/DCIM/Camera/IMG_20170413_165641.jpg
/storage/emulated/0/DCIM/Camera/IMG_20170413_165647.jpg
/storage/emulated/0/DCIM/Camera/IMG_20170413_165643.jpg
/storage/emulated/0/DCIM/Camera/IMG_20170413_165645.jpg
/storage/emulated/0/DCIM/Camera/IMG_20170413_165650.jpg
/storage/emulated/0/DCIM/Camera/IMG_20170413_165659.jpg
```
规律发现不了的大兄弟应该不怎么会玩游戏吧，比如我认识的一位老司机：**某浒**，规律精华如下：



**“IMG_20170413_165655.jpg”---------这个是照片文件的文件名，中间的”20170413_165655“中，“_”前面是年月日嘛，后面是时分秒嘛，哈哈，可以了，规律可以了，这些照片名是按时间组合起来的，那我们就可以根据时间，判断哪些是我们打开相机后拍的，可以伐？可以。**




###**二、正式开启思路二**
**根据思路 我们要用到的地方我做了以下总结,真正写的时候还是很多坑的，总结如下：**

> - 内容提供者ContentProvider与内容接受者ContentResolver
> - 数据库、多线程（这个可以忽略但是要知道，数据库 ，上一条已经把这个做好了，多线程是因为查数据库其实是耗时的，最好在子线程里面，避免ANR，咱这里就不提了）
> - 路径转换出时间再转换成数字（时间格式转化，String类型转换成其他类型）
> - 塞选图片路径(正则表达式)
 > - 根据路径path找到图片
> - 把图片压缩放后放到集合里面（二次采样）


目测东西不少呢，

等等，根据路径path找到图片，是要读取手机内存卡内容的呀，所以针对6.0及以上，还要做权限申请那一块，为了减少篇幅，这一块就不细说了，大家知道就好。

那么来吧

####**2.1、内容提供者ContentProvider与内容接受者ContentResolver**

让我自己写肯定写不出来，不怎么好记，百度了一下，如下得到图片数据库,因为是退出相机以后查询，所以要在**onActivityResult里面，如下：**：

```
  
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SMALL) {

            final String[] projection = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATA};
            final String orderBy = MediaStore.Images.Media.DISPLAY_NAME;
            final Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
 

        }
    }

```
这里拿到了uri，基本就能拿到内容提供者了，go on ，由于代码比较多，还是封装起来吧；

```
        getContentProvider(uri, projection, orderBy);
```

```

    public void getContentProvider(Uri uri, String[] projection, String orderBy) {

        Cursor cursor = getContentResolver().query(uri, projection, null,
                null, orderBy);
        if (null == cursor) {
            return ;
        }

        while (cursor.moveToNext()) {
            for (int i = 0; i < projection.length; i++) {
                String string = cursor.getString(i);
                if (string != null) {     
                  Log.e("string", "getContentProvider: "+string);
                }
            }
        }
    }
    
```

OK，run一下，打个log:


![这里写图片描述](http://img.blog.csdn.net/20170413181024204?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


数据太大，截个图看一下吧就，还是比较喜观的，这里是所有图片的路径，下面我们来赛选。

####**2.2、正则表达式塞选图片**

```
04-13 18:08:42.808 28206-28206/com.exampleenen.ruedy.moreimagetwo E/string: getContentProvider: /storage/emulated/0/DCIM/Camera/IMG_20170413_154316.jpg
```

> "/storage/emulated/0/DCIM/Camera/IMG_20170413_154316.jpg"------**这个就是我们的目标，我简化了一下，锁定方法，仁者见仁，智者见智，判断方法有很多种**，我的判断代码如下， 在上面的if中加入代码：

```
                         int length = string.length();
                    if (length >= 30) {//根据实际路径得到的，大一点保险
                        ss = string.substring(length - 23, length);//判断后23位的字符串，
                        String substring = ss.substring(0, 4);//大致判断一下是系统图片，后面严格塞选
                        String hen = ss.substring(12, 13);
                        if (substring.equals("IMG_") && hen.equals("_")) {//懒得写正则了，就这样吧，可自行拓展
                            String laststring = ss.substring(4, 19).replace("_", "");
                            Log.e("string", "getContentProvider: " + string);
                        }
                    }
```


再打个log看一下：


![这里写图片描述](http://img.blog.csdn.net/20170413181728183?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


对了，逮住了，就是这些，下面就是拿到路径对应时间的数学数字time（注意是long，不是int，很多人会栽跟头的），在打开相机之前根据java代码，拿到时间systemTime1;方法为：

```
    public long getSystemTime() {
//("yyyy年MM月dd日 HH时MM分ss秒"
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
```

然后返回界面以后拿到时间systemTime2;再在上上面代码中加入图下判断，

```
                      try {
                               long time = Long.valueOf(laststring).longValue();
                                if (time > systemTime1 && time <= systemTime2) {//位于两个时间之间的就是我们拍摄的图片


                                }
                            } catch (Exception e) {
                                Log.e("exception", "getContentProvider: " + e.toString());
                            }

```
加个try、catch保险一点。



继续run，拍照，返回，log如下：

![这里写图片描述](http://img.blog.csdn.net/20170413183026513?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)




嗯嗯，可以了，拿到了，终于拿到所谓的data了；



![这里写图片描述](http://img.blog.csdn.net/20170413183227704?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



下面就简单了，根据路径拿到图片并且二次采样如下：


```

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

```
后面就可以把路径和图片放起来add到List里面就可以了，我是新建了一个实体类。

这样数据源就有了可以设置到GridView的适配器里面啦，哈哈，

工作我来做，您休息
。。。。。
。。。。。
。。。。。

搞定了，看下效果：

![这里写图片描述](http://img.blog.csdn.net/20170413190533799?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaWFtZGluZ3J1aWhhaGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


额，这次动图比较大，所以压缩了一下，不大好看，终于有效果了。不过	这里面还有几个问题，如果真的要搞到项目你需注意以下几点：
- 打开相机那里需要优化，各大厂商系统改的面目全非
- 权限必须打开 相机和读内存卡文件权限都得打开， 这个必须
- 按照上面代码，每张图片会得到两次，所以去重一下，我用Hashset去重的（实践而来）
- 如果用户打开手机刷刷刷，每一秒牌好几张，那一秒里面拍的照片 这里的代码这个是得不到的，因为这种情况到秒的时间都是一样的，多出来了毫秒加在了秒后面，如果想得到，请自己在塞选那里自行改善，不改善的话，就提醒用户慢点拍，因为一秒钟拍那么多，肯定是一个视角拍出来的，何必呢
- 真的要整合到项目里的话，这里还有个漏洞，比如拍完照片，用户，按Home键，然后改变时间，然后进我们的应用再按返回，应该不会有人这么做的，这里可以提醒一下用户，请拍完照片直接按返回，避免失败。

以上是自己的踩得坑，测试手机有限，问题肯定是有的，欢迎多多反馈！
 
 本来 想抽取成轮子，无奈其中有很多Activity的生命周期问题。
###**经验总结：**
很多刚入手安卓的朋友，面对一个功能、需求的时候，有时候很迷茫，无从下手，其实我们一定要善于思考，知其然，知其所以然，才能把学到的知识糅合到一起，用哪里的时候知道去哪找，不用高端的东西，咱也能实现一些意想不到的功能。本篇文章的需求，应该还有其他方法，有兴趣的朋友可以探索一下。 有问题欢迎指正，一起进步，谢谢各位！





