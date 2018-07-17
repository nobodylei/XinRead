package com.rjwl.reginet.xinread.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rjwl.reginet.xinread.R;
import com.rjwl.reginet.xinread.model.Constant;
import com.rjwl.reginet.xinread.model.Student;

import com.rjwl.reginet.xinread.presenter.StudentInfoPresent;
import com.rjwl.reginet.xinread.presenter.StudentInfoView;
import com.rjwl.reginet.xinread.utils.BitmapUtils;
import com.rjwl.reginet.xinread.utils.GreenDaoUtil;
import com.rjwl.reginet.xinread.utils.SaveOrDeletePrefrence;
import com.vondear.rxtools.RxPhotoTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.dialog.RxDialogChooseImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.vondear.rxtools.view.dialog.RxDialogChooseImage.LayoutType.TITLE;

/**
 * Created by yanle on 2018/5/18.
 */

public class SetMyInfoActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, StudentInfoView {
    private RelativeLayout rlSetMyHead;
    private static final int IMAGE_PICKER = 1;

    private SimpleDraweeView imgMyHead;//头像
    private EditText etName;//姓名
    private EditText etSchool;//学校

    private Spinner spinnerSchool;//年级
    private Spinner spinnerClass;//班级
    private Spinner spinnerGender;//性别

    private Button btnOk;

    private Student student;
    private GreenDaoUtil greenDaoUtil;

    private ArrayAdapter<CharSequence> gradeAdapter;
    private ArrayAdapter<CharSequence> classAdapter;
    private ArrayAdapter<CharSequence> genderAdapter;

    private Map<String, String> studentMap;
    private Map<String, File> fileMap;
    private Map<String, String> idMap;
    private StudentInfoPresent studentInfoPresent;
    private boolean isHome = false;

    private Bitmap head;// 头像Bitmap
    private static String path = "/storage/emulated/0/Pictures/";// sd路径
    private File file;
    private Uri resultUri;

    @Override
    void initView() {
        Resources r = getResources();
        resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(R.drawable.default_head) + "/"
                + r.getResourceTypeName(R.drawable.default_head) + "/"
                + r.getResourceEntryName(R.drawable.default_head));


        title.setText("个人资料");
        studentInfoPresent = new StudentInfoPresent(this);

        greenDaoUtil = GreenDaoUtil.getDBUtil(getApplicationContext());
        Bundle bundle = getIntent().getBundleExtra("stuBundle");
        isHome = bundle.getBoolean("isHome");
        student = (Student) bundle.get("student");
       /* if (student == null) {
            student = new Student();
           //greenDaoUtil.insertStu(student);
        }*/
        rlSetMyHead = findViewById(R.id.rl_set_my_head);
        imgMyHead = findViewById(R.id.img_set_my_head);
        etName = findViewById(R.id.et_set_my_name);
        etSchool = findViewById(R.id.et_set_school);

        imgMyHead.setImageURI(Uri.parse(Constant.IMG + student.getHeadPic()));

        if (student.getName() == null || "null".equals(student.getName())) {
            etName.setText("");
        } else {
            etName.setText(student.getName());
        }
        etSchool.setText(student.getSchoolname());

        initList();

        spinnerSchool = findViewById(R.id.spinner_school_class);
        spinnerClass = findViewById(R.id.spinner_class);
        spinnerGender = findViewById(R.id.spinner_gender);
        //2、新建ArrayAdapter，数组适配器
        gradeAdapter = ArrayAdapter.createFromResource(this, R.array.grade, android.R.layout.simple_spinner_item);
        classAdapter = ArrayAdapter.createFromResource(this, R.array.myClass, android.R.layout.simple_spinner_item);
        genderAdapter = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        //3、adapter 设置一个下拉列表的样式
        gradeAdapter.setDropDownViewResource(R.layout.spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderAdapter.setDropDownViewResource(R.layout.spinner_item);

        spinnerSchool.setAdapter(gradeAdapter);
        spinnerClass.setAdapter(classAdapter);
        spinnerGender.setAdapter(genderAdapter);

        spinnerSchool.setSelection(student.getGradeID() == 0 ? 0 : (student.getGradeID() - 1));
        spinnerClass.setSelection(student.getYesorno() == 0 ? 0 : (student.getYesorno() - 1));

        spinnerSchool.setOnItemSelectedListener(this);
        spinnerClass.setOnItemSelectedListener(this);
        spinnerGender.setOnItemSelectedListener(this);

        btnOk = findViewById(R.id.btn_ok);
        rlSetMyHead.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        initPermission();
    }

    @Override
    int getResId() {
        return R.layout.activity_my;
    }

    private void initList() {

        studentMap = new HashMap<>();
        studentMap.put("id", SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "");
        studentMap.put("name", student.getName() + "");
        studentMap.put("y", student.getYesorno() + "");
        studentMap.put("gender", student.getGender() + "");
        studentMap.put("gradeId", student.getGradeID() + "");
        studentMap.put("schoolname", student.getSchoolname() + "");
        fileMap = new HashMap<>();
        idMap = new HashMap<>();
        file = new File(path);
        idMap.put("id", SaveOrDeletePrefrence.lookInt(getApplicationContext(), "id") + "");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("LAG1", "选项框:" + position + "long id:" + id);
        switch (parent.getId()) {
            case R.id.spinner_school_class:
                //tvSchoolClass.setText(gradeList.get(position) + "");
                student.setGradeID(position + 1);
                Log.d("LAG1", "年级id:" + position);
                break;
            case R.id.spinner_class:
                //tvClass.setText(classList.get(position) + "");
                student.setYesorno(position + 1);
                Log.d("LAG1", "班级id:" + position);
                break;
            case R.id.spinner_gender:
                student.setGender((String) spinnerGender.getSelectedItem());
                //Log.d("LAG1", "性别:" + gendreList.get(position));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initPermission() {
        String permissions[] = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_SETTINGS,
                Manifest.permission.READ_EXTERNAL_STORAGE

        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                //进入到这里代表没有权限.
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_set_my_head:
                initDialogChooseImage();
                break;
            case R.id.btn_ok:
                student.setName(etName.getText() + "");
                student.setSchoolname(etSchool.getText() + "");

                if ("".equals(student.getName()) || student.getYesorno() == 0
                        || "".equals(student.getGender()) || student.getGradeID() == 0
                        || "".equals(student.getSchoolname())) {
                    Toast.makeText(this, "请完善信息", Toast.LENGTH_SHORT).show();
                    return;
                }

                studentMap.put("name", student.getName() + "");
                studentMap.put("y", student.getYesorno() + "");
                studentMap.put("gender", student.getGender() + "");
                studentMap.put("gradeId", student.getGradeID() + "");
                studentMap.put("schoolname", student.getSchoolname() + "");

                //fileMap.put("f1", new File(path + "head.jpg"));

                studentInfoPresent.updataStudent(studentMap);
                break;
        }
    }

    private void initDialogChooseImage() {
        //新建一个File，传入文件夹目录
        File file = new File(path);
        //判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建<span style="color:#FF0000;">目录中包含却不存在</span>的文件夹
            file.mkdirs();
        }
        RxDialogChooseImage dialogChooseImage = new RxDialogChooseImage(SetMyInfoActivity.this, TITLE);
        dialogChooseImage.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG1", "选择相机之后:" + resultCode + " ;" + requestCode);
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_FROM_PHONE://选择相册之后的处理
                if (resultCode == RESULT_OK) {
//                    RxPhotoTool.cropImage(ActivityUser.this, );// 裁剪图片
                    initUCrop(data.getData());
                }
                break;
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                Log.d("TAG1", "选择相机之后:" + resultCode);
                if (resultCode == RESULT_OK) {
                    //data.getExtras().get("data");
                    // RxPhotoTool.cropImage(SetMyInfoActivity.this, RxPhotoTool.imageUriFromCamera);// 裁剪图片
                    initUCrop(RxPhotoTool.imageUriFromCamera);
                    Log.d("TAG1", "照片的Url" + RxPhotoTool.imageUriFromCamera);
                    Log.d("TAG1", "照片的绝对路径:" + RxPhotoTool.getImageAbsolutePath(this, RxPhotoTool.imageUriFromCamera));
                }
                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.nothing)
                        //异常占位图(当加载异常的时候出现的图片)
                        .error(R.drawable.nothing)
                        //禁止Glide硬盘缓存缓存
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

                Glide.with(SetMyInfoActivity.this).
                        load(RxPhotoTool.cropImageUri).
                        apply(options).
                        thumbnail(0.5f).
                        into(imgMyHead);
                Log.d("TAAG1", "普通剪裁后处理：" + file.getAbsolutePath());
//                RequestUpdateAvatar(new File(RxPhotoTool.getRealFilePath(mContext, RxPhotoTool.cropImageUri)));
                break;

            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    resultUri = UCrop.getOutput(data);
                    file = roadImageView(resultUri, imgMyHead);
                    fileMap.put("f1", file);
                    idMap.put("fileName", file.getName());
                    //idMap.put("f1", file.getAbsolutePath());
                    Log.d("TAG1", "文件集合" + fileMap + ";" + idMap);

                    studentInfoPresent.updataHead(idMap, fileMap);//更新头像
                    Log.d("TAAG1", "文件路径：" + file.getAbsolutePath());

                    RxSPTool.putContent(SetMyInfoActivity.this, "AVATAR", resultUri.toString());
                } else if (resultCode == UCrop.RESULT_ERROR) {
                    final Throwable cropError = UCrop.getError(data);
                }
                break;
            case UCrop.RESULT_ERROR://UCrop裁剪错误之后的处理
                final Throwable cropError = UCrop.getError(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //从Uri中加载图片 并将其转化成File文件返回
    private File roadImageView(Uri uri, ImageView imageView) {
        Glide.with(SetMyInfoActivity.this).
                load(uri).
                thumbnail(0.5f).
                into(imageView);

        return (new File(RxPhotoTool.getImageAbsolutePath(this, uri)));
    }

    private void initUCrop(Uri uri) {
        //Uri destinationUri = RxPhotoTool.createImagePathUri(this);

        SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));

        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), imageName + ".jpeg"));

        UCrop.Options options = new UCrop.Options();
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
        //设置隐藏底部容器，默认显示
        //options.setHideBottomControls(true);
        //设置toolbar颜色
        options.setToolbarColor(ActivityCompat.getColor(this, R.color.colorPrimary));
        //设置状态栏颜色
        options.setStatusBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryDark));

        //开始设置
        //设置最大缩放比例
        options.setMaxScaleMultiplier(5);
        //设置图片在切换比例时的动画
        options.setImageToCropBoundsAnimDuration(666);
        //options.setCircleDimmedLayer(true);
        //设置裁剪窗口是否为椭圆
        //options.setOvalDimmedLayer(true);
        //设置是否展示矩形裁剪框
        // options.setShowCropFrame(false);
        //设置裁剪框横竖线的宽度
        //options.setCropGridStrokeWidth(20);
        //设置裁剪框横竖线的颜色
        //options.setCropGridColor(Color.GREEN);
        //设置竖线的数量
        //options.setCropGridColumnCount(2);
        //设置横线的数量
        //options.setCropGridRowCount(1);

        UCrop.of(uri, destinationUri)
                .withAspectRatio(1, 1)
                .withMaxResultSize(1000, 1000)
                .withOptions(options)
                .start(this);
    }


    @Override
    public void showLoading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
            progressDialog.show();
            //progressDialog = ProgressDialog.show(this, "", "请稍后...");
        } else if (progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog.setTitle("");
            progressDialog.setMessage("请稍后...");
        }
        progressDialog.show();
    }

    @Override
    public void hidenLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Log.d("TAG1", "Toast信息：" + msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void skipView() {
        greenDaoUtil.updataStudent(student);
        if (isHome) {
            Intent intent = new Intent(SetMyInfoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            onBackPressed();
        }

    }

    @Override
    public void getHead() {
        student.setHeadPic(studentInfoPresent.getHeadPicture());
    }
}
