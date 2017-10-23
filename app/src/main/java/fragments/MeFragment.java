package fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import helper.FileUtil;
import helper.SelectPicPopupWindow;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.chongdeng.jufeng.shoppinghappiness.MerchandiseUploadActivity;
import com.chongdeng.jufeng.shoppinghappiness.R;
import com.chongdeng.jufeng.shoppinghappiness.restful_client.ApiClient;
import com.chongdeng.jufeng.shoppinghappiness.restful_client.ApiInterface;
import com.chongdeng.jufeng.shoppinghappiness.restful_model.UploadResult;

import java.io.File;
import java.io.IOException;

public class MeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    CircleImageView UserThumbnail;
    ImageView order_management;

    private SelectPicPopupWindow MenuWindow;


    private static final String IMAGE_FILE_NAME = "AvatarImage.jpg";
    private String UrlPath;
    private String ResultStr = "";
    private static ProgressDialog pd;
    private static final int REQUESTCODE_PICK = 0;
    private static final int REQUESTCODE_TAKE = 1;
    private static final int REQUESTCODE_CUTTING = 2;

    public MeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_me, container, false);

        UserThumbnail = (CircleImageView) view.findViewById(R.id.UserThumbnail);
        UserThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(), "yes", Toast.LENGTH_SHORT).show();
                MenuWindow = new SelectPicPopupWindow(getContext(), itemsOnClick);
                MenuWindow.showAtLocation(view.findViewById(R.id.me_layout),
                        Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        order_management = (ImageView) view.findViewById(R.id.order_management);
        order_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "order_management", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), MerchandiseUploadActivity.class));
            }
        });

        return view;
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MenuWindow.dismiss();
            switch (v.getId()) {
                case R.id.takePhotoBtn:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                case R.id.pickPhotoBtn:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUESTCODE_PICK:
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                break;
            case REQUESTCODE_TAKE:
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {

            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            UrlPath = FileUtil.saveFile(getContext(), "temphead.jpg", photo);
            UserThumbnail.setImageDrawable(drawable);

            pd = ProgressDialog.show(getContext(), null, "is uploading...");
            new Thread(UploadImageRunnable).start();
        }
    }

    Runnable UploadImageRunnable = new Runnable() {
        @Override
        public void run() {

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            File AvatarImgFile = new File(UrlPath);
            final RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),AvatarImgFile);
            int UserId = 123;
            Call<UploadResult> model = apiService.uploadProfileImage(UserId, requestBody);

            model.enqueue(new Callback<UploadResult>() {
                @Override
                public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                    Toast.makeText(MeFragment.this.getContext(), response.body().getMsg() , Toast.LENGTH_SHORT).show();
                    pd.hide();
                }
                @Override
                public void onFailure(Call<UploadResult> call, Throwable t) {
                    Toast.makeText(MeFragment.this.getContext(), "error: " + t.toString(), Toast.LENGTH_LONG).show();
                    pd.hide();
                }
            });
        }
    };





}
