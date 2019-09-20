package com.example.yfsl.clearcache_demo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yfsl.clearcache_demo.database.DataBaseManager;
import com.example.yfsl.clearcache_demo.utils.DataCleanUtil;
import com.example.yfsl.clearcache_demo.utils.SharedPreferenceUtil;
import com.yfpj.lib.util.compress.BitmapUtil;
import com.yfpj.lib.util.compress.CompressFileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mSaveShareBtn,mSaveInnerBtn,mSaveExternalBtn,mSaveDatabaseBtn,mSaveContentBtn,mSaveNetBtn;
    private Button mClearShareBtn,mClearInnerBtn,mClearExternalBtn,mClearDatabaseBtn;
    private TextView mShowSharedPreSize,mShowInnerSize,mShowExternalSize,mShowDataBaseSize;
    private Button mClearAllBtn;
    private TextView mShowCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initData(){
        setCacheSize(mShowCacheSize);
        mSaveShareBtn.setOnClickListener(this);
        mSaveInnerBtn.setOnClickListener(this);
        mSaveExternalBtn.setOnClickListener(this);
        mSaveDatabaseBtn.setOnClickListener(this);
        mSaveContentBtn.setOnClickListener(this);
        mSaveNetBtn.setOnClickListener(this);

        mClearShareBtn.setOnClickListener(this);
        mClearInnerBtn.setOnClickListener(this);
        mClearExternalBtn.setOnClickListener(this);
        mClearDatabaseBtn.setOnClickListener(this);

        mClearAllBtn.setOnClickListener(this);

        String sharedPrefSize = DataCleanUtil.getFormatSize(getSharedPrefCacheSize());
        String innerSize = DataCleanUtil.getFormatSize(getInnerCacheSize());
        String externalSize = DataCleanUtil.getFormatSize(getExternalCacheSize());
        String databaseSize = DataCleanUtil.getFormatSize(getDatabaseCacheSize());
        String totalSize = DataCleanUtil.getFormatSize(getTotalCacheSize());
        mShowSharedPreSize.setText(sharedPrefSize);
        mShowInnerSize.setText(innerSize);
        mShowExternalSize.setText(externalSize);
        mShowDataBaseSize.setText(databaseSize);
        mShowCacheSize.setText(totalSize);
    }



    private void initView() {
        mSaveShareBtn = findViewById(R.id.btn_save_to_sharepreference);
        mSaveInnerBtn = findViewById(R.id.btn_save_to_inner_data);
        mSaveExternalBtn = findViewById(R.id.btn_save_to_external_data);
        mSaveDatabaseBtn = findViewById(R.id.btn_save_to_database);
        mSaveContentBtn = findViewById(R.id.btn_save_to_contentprovider);
        mSaveNetBtn = findViewById(R.id.btn_save_to_net);

        mClearShareBtn = findViewById(R.id.btn_clear_sharepreference);
        mClearInnerBtn = findViewById(R.id.btn_clear_inner_data);
        mClearExternalBtn = findViewById(R.id.btn_clear_external_data);
        mClearDatabaseBtn = findViewById(R.id.btn_clear_database);

        mShowSharedPreSize = findViewById(R.id.showSharedPreCacheSize);
        mShowInnerSize = findViewById(R.id.showInnerCacheSize);
        mShowExternalSize = findViewById(R.id.showExternalCacheSize);
        mShowDataBaseSize = findViewById(R.id.showDataBaseCacheSize);

        mClearAllBtn = findViewById(R.id.btn);

        mShowCacheSize = findViewById(R.id.showCacheSize);
    }

    /**
     * 获取总缓存大小
     */
    private long getTotalCacheSize()  {
        return getSharedPrefCacheSize() + getInnerCacheSize() + getExternalCacheSize() + getDatabaseCacheSize();
    }

    /**
     * 获取SharedPreference缓存大小
     */
    private long getSharedPrefCacheSize(){
        long sharedPrefsCacheSize = DataCleanUtil.getFolderSize(new File(this.getCacheDir().getParentFile(),"shared_prefs"));//SharePreference
        return sharedPrefsCacheSize;
    }

    /**
     * 获取内存缓存大小
     */
    private long getInnerCacheSize(){
        long innerCacheSize = DataCleanUtil.getFolderSize(this.getCacheDir());//内存缓存
        return innerCacheSize;
    }

    /**
     * 获取外存缓存大小
     */
    private long getExternalCacheSize(){
        long externalCacheSize = DataCleanUtil.getFolderSize(this.getExternalCacheDir());//外存缓存
        return externalCacheSize;
    }

    /**
     * 获取database缓存大小
     */
    private long getDatabaseCacheSize(){
        long databaseCacheSize = DataCleanUtil.getFolderSize(new File(this.getCacheDir().getParentFile(),"databases"));//database
        return databaseCacheSize;
    }

    /**
     * 缓存
     */
    private void setCacheSize(TextView textView) {
        String totalSize = "";
        try {
            long innerCacheSize = DataCleanUtil.getFolderSize(this.getCacheDir());//内存缓存
            long externalCacheSize = DataCleanUtil.getFolderSize(this.getExternalCacheDir());//外存缓存
            long totalCacheSize = innerCacheSize + externalCacheSize;
            totalSize = DataCleanUtil.getFormatSize(totalCacheSize);//格式化
        } catch (Exception e) {
            e.printStackTrace();
        }
        textView.setText(totalSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn://清除全部缓存
                DataCleanUtil.cleanApplicationData(this,this.getCacheDir().getAbsolutePath(),this.getExternalCacheDir().getAbsolutePath());
                break;
            case R.id.btn_save_to_sharepreference://保存到Share Preference
                String[] data = {"张三","23","false"};
                saveToSharedPreference(data);
                break;
            case R.id.btn_save_to_inner_data://保存到内存中
                String dataInner = "张三  23  false";
                saveToInnerStorge(dataInner);
                break;
            case R.id.btn_save_to_external_data://保存到SD卡中
                savePicToSdcard();
                break;
            case R.id.btn_save_to_database://保存到数据库
                saveToDataBase();
                break;
            case R.id.btn_save_to_contentprovider://保存到数据共享区
                break;
            case R.id.btn_save_to_net://保存到网络服务器
                break;
            case R.id.btn_clear_sharepreference://清除Share Preference
                DataCleanUtil.cleanSharedPreference(this);
                break;
            case R.id.btn_clear_inner_data://清除内存
                DataCleanUtil.cleanInternalCache(this);
                break;
            case R.id.btn_clear_external_data://清除外存
                DataCleanUtil.cleanExternalCache(this);
                break;
            case R.id.btn_clear_database://清除数据库
                DataCleanUtil.cleanDatabaseByName(this,SQLTable.TABLE_NAME);
                break;
        }
    }

    private void saveToDataBase() {
        ContentValues values = new ContentValues();
        values.put(SQLTable.NAME,"张三");
        values.put(SQLTable.OLD,23);
        values.put(SQLTable.MARRIED,"否");
        DataBaseManager.getInstance(this).saveData(values);
    }

    /**
     * 保存照片到SD卡
     */
    private void savePicToSdcard() {
        Bitmap bitmap = getBitmap(this,R.drawable.ic_launcher_foreground);
        //创建存放图片的文件目录
        File appDir = new File(Environment.getExternalStorageDirectory(), "CLEAR_CACHE_DEMO");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String filename =  "demoimg.jpg";
        //创建存放图片的文件（文件名后缀文件格式）
        File file = new File(appDir, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //创建文件输出流
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //将图片插入到系统图库（file.getAbsolutePath()这个路径）
            MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), filename, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //发动广播  通知图库更新显示图片（更新的位置由第二个参数决定 Uri.parse("file://"+Environment.getExternalStorageDirectory())）
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
    }

    private static Bitmap getBitmap(Context context,int vectorDrawableId) {
        Bitmap bitmap=null;
        if (Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            Drawable vectorDrawable = context.getDrawable(vectorDrawableId);
            bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                    vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            vectorDrawable.draw(canvas);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), vectorDrawableId);
        }
        return bitmap;
    }

    /**
     * 保存到内存中
     */
    private void saveToInnerStorge(String dataInner) {
        BufferedWriter bufferedWriter = null;
        try {
            FileOutputStream fos = openFileOutput("demo_file",Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fos));
            bufferedWriter.write(dataInner);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bufferedWriter != null) {
                try {
                    //不要忘记
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存到Shared Preference
     */
    private void saveToSharedPreference(String[] data) {
        SharedPreferenceUtil.saveName(this,data[0]);
        SharedPreferenceUtil.saveOld(this,Integer.parseInt(data[1]));
        SharedPreferenceUtil.saveMarried(this,Boolean.parseBoolean(data[2]));
    }
}
