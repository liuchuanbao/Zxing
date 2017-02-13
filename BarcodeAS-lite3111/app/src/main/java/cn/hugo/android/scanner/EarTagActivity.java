package cn.hugo.android.scanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.hugo.android.scanner.Barcode.ResultObject.DecodeImageData_Result;
import cn.hugo.android.scanner.Barcode.ResultObject.DecodeLargeImageData_Result;

public class EarTagActivity extends Activity {

    ImageView img_right, img_scanf;
    TextView edKind, edchandi, edNUmber, edprovince;
    TextView tvKind;
    EditText  tvNumber;
    String[] kindName;
    int reginID;
    int earMarkNumber;
    SQLiteDatabase db;
    String regino_name = null;
    String regino_code = null;
    int AnimalType;
    String xuliehao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ear_tag);
        initView();
        EventBus.getDefault().register(this);
        kindName = new String[]{"猪", "牛", "羊"};
        //打开数据库输出流
        SQLdm s = new SQLdm();
        db = s.openDatabase(getApplicationContext());

    }

    private void initView() {
        img_right = (ImageView) findViewById(R.id.img_right);
        img_scanf = (ImageView) findViewById(R.id.img_camer);
        edKind = (TextView) findViewById(R.id.ed_version);

        edchandi = (TextView) findViewById(R.id.ed_chandi);
        edNUmber = (TextView) findViewById(R.id.ed_xulie);
        edprovince = (TextView) findViewById(R.id.text_province);

        tvKind = (TextView) findViewById(R.id.text_name);

        tvNumber = (EditText) findViewById(R.id.text_number);

        img_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo 选择畜种
                new AlertDialog.Builder(EarTagActivity.this).setTitle("畜种选择").setIcon(
                        android.R.drawable.ic_dialog_info).setSingleChoiceItems(
                        kindName, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                tvKind.setText(kindName[which]);

                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        img_scanf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启扫描功能
                String names = tvKind.getText().toString();
                if ("".equals(names) || "null".equals(names) || names.isEmpty()) {
                    Toast.makeText(EarTagActivity.this, "请先选择畜种", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(EarTagActivity.this, CaptureActivity.class));
            }
        });

        tvNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                edKind.setText("");
                edNUmber.setText("");
                edchandi.setText("");
                edprovince.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               String sernumbe = s.toString();
                if(sernumbe.length()==15){
                    String strs = sernumbe.substring(0,1);
                    int itype = Integer.valueOf(strs)-1;
                    String strtype2 = kindName[itype];
                    edKind.setText(strtype2);
                    tvKind.setText(strtype2);
                    String regionCode = sernumbe.substring(1,7);
                    selectDatas(regionCode);
                    String strNumbers = sernumbe.substring(7,15);
                    edNUmber.setText(strNumbers);

                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DecodeImageData_Result imageRst) {
        Log.e("xyc1activity", "AnimalType:" + imageRst.EM.AnimalType);
        Log.e("xyc1 activity", "Region:" + imageRst.EM.RegionSerial);
        Log.e("xyc1 activity ", "EarMarkNum:" + imageRst.EM.EarMarkNumber);

        AnimalType = imageRst.EM.AnimalType ;
        String strtype = kindName[AnimalType-1];
        edKind.setText(strtype);
        tvKind.setText(strtype);
        reginID = imageRst.EM.RegionSerial;

        Log.e("TAG", "onMessageEvent: "+reginID );
        earMarkNumber = imageRst.EM.EarMarkNumber;

        String strear = String.valueOf(earMarkNumber);
        selectData(reginID);

        int weishu = strear.length();
        if(weishu==8){
            xuliehao =AnimalType+regino_code+strear;
        }else {
            while(weishu%8!=0){
                strear = "0"+strear;
                weishu = strear.length();
            }

            xuliehao =AnimalType+regino_code+ strear;
        }
        tvNumber.setText(xuliehao);
    }

    /**
     * 按照id查询数据库
     * @param region_id
     */
    private void selectData(int region_id) {
        String strid = String.valueOf(region_id);
        Cursor cursor = db.rawQuery("select * from M_LiteRegion where region_id=?", new String[]{strid});

        if (cursor.moveToFirst()) {
            regino_name = cursor.getString(cursor.getColumnIndex("region_name"));
            regino_code =  cursor.getString(cursor.getColumnIndex("region_code"));
            Log.e("TAG", "onMessageEvent:regino_name= "+regino_code +"  regino_name= "+ regino_name );

            edprovince.setText(regino_name);
            edchandi.setText(regino_code);
            edNUmber.setText(earMarkNumber+"");
        }

        cursor.close();

    }
    /**
     * 按照id 产地市县查询
     * @param region_code
     */
    private void selectDatas(String  region_code) {
        Cursor cursor = db.rawQuery("select * from M_LiteRegion where region_code=?", new String[]{region_code});

        if (cursor.moveToFirst()) {
            regino_name = cursor.getString(cursor.getColumnIndex("region_name"));
            regino_code =  cursor.getString(cursor.getColumnIndex("region_code"));
            if("".equals(regino_name)||"null".equals(regino_name)||regino_name.isEmpty()){
                Toast.makeText(EarTagActivity.this,"输入的耳标号不存在",Toast.LENGTH_SHORT).show();
                return;
            }
            edprovince.setText(regino_name);
            edchandi.setText(regino_code);

        }

        cursor.close();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
