package com.chongdeng.jufeng.shoppinghappiness;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chongdeng.jufeng.shoppinghappiness.restful_client.ApiClient;
import com.chongdeng.jufeng.shoppinghappiness.restful_client.ApiInterface;
import com.chongdeng.jufeng.shoppinghappiness.restful_client.DefaultProgressListener;
import com.chongdeng.jufeng.shoppinghappiness.restful_client.UploadFileRequestBody;
import com.chongdeng.jufeng.shoppinghappiness.restful_model.UploadResult;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fragments.UploadProgressDialogFragment;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MerchandiseUploadActivity extends AppCompatActivity {

    private GridView PicGridView;
    private SimpleAdapter simpleAdapter;
    private ArrayList<HashMap<String, Object>> ImageItems;
    private Bitmap AddPicBmp;

    private final int PICK_PHOTO = 1;       //打开图片标记
    private final int TAKE_PHOTO = 2;       //拍照标记

    private String ImagePath;                     //选择图片路径
    private static final String IMAGE_FILE_NAME = "merchandise_pic.jpg";

    private TextView cancel_tv;
    private TextView post_tv;

    private TextView merchandise_desc_tv;
    private TextView merchandise_name_tv;
    private TextView merchandise_price_tv;

    private static ProgressDialog pd;

    UploadProgressDialogFragment ProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //设定屏幕为竖屏，不是横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_merchandise_upload);

        cancel_tv = (TextView) findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        post_tv = (TextView) findViewById(R.id.post_tv);
        post_tv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //pd = ProgressDialog.show(MerchandiseUploadActivity.this, null, "is uploading...");
                //new Thread(UploadMerchandise).start();

                UploadMerchandise();
            }
        });

        merchandise_name_tv = (TextView) findViewById(R.id.merchandise_name);
        merchandise_price_tv = (TextView) findViewById(R.id.merchandise_price);
        merchandise_desc_tv = (TextView) findViewById(R.id.merchandise_desc);

        PicGridView = (GridView) findViewById(R.id.PicGridView);

        AddPicBmp = BitmapFactory.decodeResource(getResources(), R.drawable.gridview_addpic);
        ImageItems = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", AddPicBmp);
        map.put("ImagePath", "add_pic");
        ImageItems.add(map);
        simpleAdapter = new SimpleAdapter(this,
                ImageItems, R.layout.griditem_addpic,
                new String[] { "itemImage"}, new int[] { R.id.imageView1});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                // TODO Auto-generated method stub
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView i = (ImageView)view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        PicGridView.setAdapter(simpleAdapter);

        PicGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                if( ImageItems.size() == 10 && position == 0) { //第一张为默认图片
                    Toast.makeText(getApplication(), "图片数9张已满", Toast.LENGTH_SHORT).show();
                }
                else if(position == 0) { //点击图片位置为0, 对应0张图片
                    AddImageDialog();
                }
                else {
                    DeleteDialog(position);
                }
            }
        });
    }

    /*
     * 添加图片 可通过本地添加、拍照添加
     */
    protected void AddImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MerchandiseUploadActivity.this);
        builder.setTitle("添加图片");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false); //不响应back按钮
        builder.setItems(new String[] {"本地相册","手机拍照","取消"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch(which) {
                            case 0: //本地相册
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, PICK_PHOTO);
                                //通过onResume()刷新数据
                                break;
                            case 1: //手机相机
                                dialog.dismiss();
                                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                                startActivityForResult(takeIntent, TAKE_PHOTO);
                                break;
                            case 2: //取消添加
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });
        //显示对话框
        builder.create().show();
    }


    /*
     * Dialog对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void DeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MerchandiseUploadActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                ImageItems.remove(position);
                simpleAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    //获取图片路径 响应startActivityForResult
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开图片
        if(resultCode==RESULT_OK && requestCode== PICK_PHOTO) {
            Uri uri = data.getData();

            Cursor cursor = getContentResolver().query(
                    uri,
                    new String[] { MediaStore.Images.Media.DATA },
                    null,
                    null,
                    null);
            //返回 没找到选择图片
            if (null == cursor) {
                return;
            }
            //光标移动至开头 获取图片路径
            cursor.moveToFirst();
            ImagePath = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
        }
        //拍照
        else if(resultCode==RESULT_OK && requestCode==TAKE_PHOTO) {
            File ImageFile = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
            ImagePath = ImageFile.getPath();
        }
    }

    //刷新图片
    @Override
    protected void onResume() {
        super.onResume();
        //获取传递的处理图片在onResume中显示
        //Intent intent = getIntent();
        //ImagePath = intent.getStringExtra("pathProcess");
        //适配器动态显示图片
        if(!TextUtils.isEmpty(ImagePath)){
            //Bitmap addbmp = BitmapFactory.decodeFile(ImagePath);

            BitmapFactory.Options options = new BitmapFactory.Options();
            // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
            options.inJustDecodeBounds = true;
            Bitmap addbmp = BitmapFactory.decodeFile(ImagePath, options);
            int scale = (int)( options.outWidth / (float)300);
            if(scale <= 0)
                scale = 1;
            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;
            addbmp = BitmapFactory.decodeFile(ImagePath, options);


            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            map.put("ImagePath", ImagePath);
            ImageItems.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    ImageItems, R.layout.griditem_addpic,
                    new String[] { "itemImage"}, new int[] { R.id.imageView1});
            //接口载入图片
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    // TODO Auto-generated method stub
                    if(view instanceof ImageView && data instanceof Bitmap){
                        ImageView i = (ImageView)view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            PicGridView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            ImagePath = null;
        }
    }

//    Runnable UploadMerchandise = new Runnable() {
//        @Override
//        public void run() {
//            String merchandise_name = merchandise_name_tv.getText().toString();
//            String merchandise_price = merchandise_price_tv.getText().toString();
//            String merchandise_desc = merchandise_desc_tv.getText().toString();
//
//            if(TextUtils.isEmpty(merchandise_name)){
//                Toast.makeText(getApplicationContext(), "Please input mechandise name!" , Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if(TextUtils.isEmpty(merchandise_price)){
//                Toast.makeText(getApplicationContext(), "Please input mechandise price!" , Toast.LENGTH_SHORT).show();
//                return;
//            }
//            if(TextUtils.isEmpty(merchandise_desc)){
//                Toast.makeText(getApplicationContext(), "Please input mechandise description!" , Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if(ImageItems.size() == 1){
//                Toast.makeText(getApplicationContext(), "Please add picture to upload!" , Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            ApiInterface apiService =
//                    ApiClient.getClient().create(ApiInterface.class);
//
//            Map<String,RequestBody> params = new HashMap<String, RequestBody>();
//
//            for(HashMap<String, Object> ImageItem : ImageItems){
//                String ImagePath = ImageItem.get("ImagePath").toString();
//                if(ImagePath != "add_pic"){
//                    File ImageFile = new File(ImagePath);
//                    final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),ImageFile);
//                    params.put("upload_file[]\"; filename=\""+ImageFile.getName()+"", requestBody);
//                }
//            }
//
//
//            Call<UploadResult> model = apiService.UploadMerchandise(merchandise_name, merchandise_price, merchandise_desc, params);
//
//
//            model.enqueue(new Callback<UploadResult>() {
//                @Override
//                public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
//                    Toast.makeText(getApplicationContext(), response.body().getMsg() , Toast.LENGTH_SHORT).show();
//                    if (pd.isShowing())
//                        pd.dismiss();
//                    finish();
//                }
//                @Override
//                public void onFailure(Call<UploadResult> call, Throwable t) {
//                    Toast.makeText(getApplicationContext(), "error: " + t.toString(), Toast.LENGTH_LONG).show();
//                    if (pd.isShowing())
//                        pd.dismiss();
//                }
//            });
//        }
//    };

    void UploadMerchandise() {
            String merchandise_name = merchandise_name_tv.getText().toString();
            String merchandise_price = merchandise_price_tv.getText().toString();
            String merchandise_desc = merchandise_desc_tv.getText().toString();

            if (TextUtils.isEmpty(merchandise_name)) {
                Toast.makeText(getApplicationContext(), "Please input mechandise name!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(merchandise_price)) {
                Toast.makeText(getApplicationContext(), "Please input mechandise price!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(merchandise_desc)) {
                Toast.makeText(getApplicationContext(), "Please input mechandise description!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ImageItems.size() == 1) {
                Toast.makeText(getApplicationContext(), "Please add picture to upload!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, String> optionMap = new HashMap<>();
            optionMap.put("merchandise_name", merchandise_name);
            optionMap.put("merchandise_price", merchandise_price);
            optionMap.put("merchandise_desc", merchandise_desc);

            ApiInterface apiService =
                    ApiClient.getFileUpLoadRetrofitClient().create(ApiInterface.class);

            Map<String, RequestBody> requestBodyMap = new HashMap<>();

            int FileIndex = 0;
            for (HashMap<String, Object> ImageItem : ImageItems) {
                String ImagePath = ImageItem.get("ImagePath").toString();
                if (ImagePath != "add_pic") {
                    File ImageFile = new File(ImagePath);

                    UploadFileRequestBody fileRequestBody = new UploadFileRequestBody(ImageFile, new DefaultProgressListener(mHandler, FileIndex));
                    requestBodyMap.put("dc_file[]\"; filename=\"" + ImageFile.getName() + "", fileRequestBody);

                    ++FileIndex;
                }
            }

            FragmentManager fm = getSupportFragmentManager();
            ProgressDialog = new UploadProgressDialogFragment();
            ProgressDialog.show(fm, "Upload Dialog");

            apiService.UploadMerchandiseInfo(optionMap, requestBodyMap).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<UploadResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("---the error is ---" + e);
                    }

                    @Override
                    public void onNext(UploadResult res) {
                        System.out.println("---the next string is --" + res.getMsg());
                    }
            });
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        if (msg.what > 0) {
            ProgressDialog.UpdateProgress(msg.what);
            System.out.println(msg.what + "%");
        }
        }
    };

}
