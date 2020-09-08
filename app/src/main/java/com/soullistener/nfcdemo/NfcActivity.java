package com.soullistener.nfcdemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;


/**
 * @author kuan
 * Created on 2019/2/25.
 * @description
 */
public class NfcActivity extends AppCompatActivity {
    private NfcAdapter mNfcAdapter;
    private Tag mTag;

    public static byte[] hexToBytes(String hex,int count) {

        byte[] bytes = new byte[count];
        try {

            bytes = Hex.decodeHex(StringUtils.rightPad(hex, count*2, "0").substring(0,count*2).toCharArray());
        }
        catch (Exception ex){

        }

        return bytes;
    }

    private String formatToName(String data){

        String name = null;
        try {
            byte[] bytes = Hex.decodeHex(data.toCharArray());
            name = new String(bytes, "GBK").trim();
        }
        catch (Exception ex){
            name = "";
        }

        return name;
    }

    private KbCardRecord formatToRecrod(String data){

        KbCardRecord kbCardRecord = new KbCardRecord();

        if(data == null) {
            return kbCardRecord;
        }

        String nameAsc = data.substring(0,20);
        String timeHex = data.substring(20,30);
        String typeHex = data.substring(30,32);

        String name = null;
        try {
            byte[] bytes = Hex.decodeHex(nameAsc.toCharArray());
            name = new String(bytes, "GBK").trim();
        }
        catch (Exception ex){
            name = "";
        }

        Long time = null;
        try {
            time = Long.parseLong(timeHex, 16);
        }
        catch (Exception ex){
            time = 0l;
        }

        Integer type = null;
        try {
            type = Integer.parseInt(typeHex, 16);
        }
        catch (Exception ex){
            type = 0;
        }


        kbCardRecord.setUserName(name);
        kbCardRecord.setRecordDatetime(time);
        kbCardRecord.setType(type);


        return kbCardRecord;
    }

    private byte[] formatFromName(String name){

        String nameData = "";
        try {
            char[] nameChars = Hex.encodeHex(name.getBytes("GBK"));
            nameData =  String.valueOf(nameChars);
        }
        catch (Exception ex){
            nameData = "";
        }
        nameData = StringUtils.rightPad(nameData, 32, "0");

        return hexToBytes(nameData,16);
    }

    private byte[] formatFromModel(KbCardRecord kbCardRecord){

        String nameData = "";
        try {
            char[] nameChars = Hex.encodeHex(kbCardRecord.getUserName().getBytes("GBK"));
            nameData =  String.valueOf(nameChars);
        }
        catch (Exception ex){
            nameData = "";
        }
        nameData = StringUtils.rightPad(nameData, 10, "0");

        String timeData = String.format("%010X",kbCardRecord.getRecordDatetime());

        String type = String.format("%02X",kbCardRecord.getType());

        String data = nameData + timeData + type;

        return hexToBytes(data,16);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNfcAdapter = M1CardUtils.isNfcAble(this);
        M1CardUtils.setPendingIntent(PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0));
        mTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);

        TextView textView = findViewById(R.id.tv_content);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        //M1卡类型
        findViewById(R.id.btn_read_m1).setOnClickListener(v -> {
            if (M1CardUtils.hasCardType(mTag, this, "MifareClassic")) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    String[][] m1Content = M1CardUtils.readCard(mTag);

                    //解析钥匙名称
                    stringBuilder.append("钥匙：" + formatToName(m1Content[1][0]) +"\n");
                    //解析记录
                    KbCardRecord kbCardRecord;
                    kbCardRecord = formatToRecrod(m1Content[1][1]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");
                    kbCardRecord = formatToRecrod(m1Content[1][2]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");

                    kbCardRecord = formatToRecrod(m1Content[2][0]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");
                    kbCardRecord = formatToRecrod(m1Content[2][1]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");
                    kbCardRecord = formatToRecrod(m1Content[2][2]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");

                    kbCardRecord = formatToRecrod(m1Content[3][0]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");
                    kbCardRecord = formatToRecrod(m1Content[3][1]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");
                    kbCardRecord = formatToRecrod(m1Content[3][2]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");

                    kbCardRecord = formatToRecrod(m1Content[4][0]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");
                    kbCardRecord = formatToRecrod(m1Content[4][1]);
                    stringBuilder.append("人员：" + kbCardRecord.getUserName() + "|时间:" + kbCardRecord.getRecordDatetime() + "|类型:" + kbCardRecord.getType() +"\n");

                    textView.setText(stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    textView.setText("");
                }
            }
        });

        //CPU
        findViewById(R.id.btn_read_cpu).setOnClickListener(v->{


            String keyName = "测试钥匙1";

            KbCardRecord kbCardRecord1 = new KbCardRecord();
            kbCardRecord1.setUserName("测试人员1");
            kbCardRecord1.setRecordDatetime(1598492563l);
            kbCardRecord1.setType(0);

            KbCardRecord kbCardRecord2 = new KbCardRecord();
            kbCardRecord2.setUserName("测试人员2");
            kbCardRecord2.setRecordDatetime(1598492563l);
            kbCardRecord2.setType(0);

            KbCardRecord kbCardRecord3 = new KbCardRecord();
            kbCardRecord3.setUserName("测试人员3");
            kbCardRecord3.setRecordDatetime(1598492563l);
            kbCardRecord3.setType(0);

            KbCardRecord kbCardRecord4 = new KbCardRecord();
            kbCardRecord4.setUserName("测试人员4");
            kbCardRecord4.setRecordDatetime(1598492563l);
            kbCardRecord4.setType(0);

            KbCardRecord kbCardRecord5 = new KbCardRecord();
            kbCardRecord5.setUserName("测试人员5");
            kbCardRecord5.setRecordDatetime(1598492563l);
            kbCardRecord5.setType(0);

            KbCardRecord kbCardRecord6 = new KbCardRecord();
            kbCardRecord6.setUserName("测试人员6");
            kbCardRecord6.setRecordDatetime(1598492563l);
            kbCardRecord6.setType(0);

            KbCardRecord kbCardRecord7 = new KbCardRecord();
            kbCardRecord7.setUserName("测试人员7");
            kbCardRecord7.setRecordDatetime(1598492563l);
            kbCardRecord7.setType(0);

            KbCardRecord kbCardRecord8 = new KbCardRecord();
            kbCardRecord8.setUserName("测试人员8");
            kbCardRecord8.setRecordDatetime(1598492563l);
            kbCardRecord8.setType(0);

            KbCardRecord kbCardRecord9 = new KbCardRecord();
            kbCardRecord9.setUserName("测试人员9");
            kbCardRecord9.setRecordDatetime(1598492563l);
            kbCardRecord9.setType(0);

            KbCardRecord kbCardRecord10 = new KbCardRecord();
            kbCardRecord10.setUserName("测试人员10");
            kbCardRecord10.setRecordDatetime(1598492563l);
            kbCardRecord10.setType(0);





            if (M1CardUtils.hasCardType(mTag, this, "MifareClassic")) {
                try {



                    byte[] data = formatFromName("测试钥匙1");
                    Boolean re = M1CardUtils.writeBlock(mTag,4,data);
                    if(re == false){
                        textView.setText("写卡失败-0-4block");
                        return;
                    }
                    data = formatFromModel(kbCardRecord1);
                    re = M1CardUtils.writeBlock(mTag,5,data);
                    if(re == false){
                        textView.setText("写卡失败-1-5block");
                        return;
                    }
                    data = formatFromModel(kbCardRecord2);
                    re = M1CardUtils.writeBlock(mTag,6,data);
                    if(re == false){
                        textView.setText("写卡失败-2-6block");
                        return;
                    }

                    data = formatFromModel(kbCardRecord3);
                    re = M1CardUtils.writeBlock(mTag,8,data);
                    if(re == false){
                        textView.setText("写卡失败-3-8block");
                        return;
                    }
                    data = formatFromModel(kbCardRecord4);
                    re = M1CardUtils.writeBlock(mTag,9,data);
                    if(re == false){
                        textView.setText("写卡失败-4-9block");
                        return;
                    }
                    data = formatFromModel(kbCardRecord5);
                    re = M1CardUtils.writeBlock(mTag,10,data);
                    if(re == false){
                        textView.setText("写卡失败-5-10block");
                        return;
                    }

                    data = formatFromModel(kbCardRecord6);
                    re = M1CardUtils.writeBlock(mTag,12,data);
                    if(re == false){
                        textView.setText("写卡失败-6-12block");
                        return;
                    }
                    data = formatFromModel(kbCardRecord7);
                    re = M1CardUtils.writeBlock(mTag,13,data);
                    if(re == false){
                        textView.setText("写卡失败-7-13block");
                        return;
                    }
                    data = formatFromModel(kbCardRecord8);
                    re = M1CardUtils.writeBlock(mTag,14,data);
                    if(re == false){
                        textView.setText("写卡失败-8-14block");
                        return;
                    }

                    data = formatFromModel(kbCardRecord9);
                    re = M1CardUtils.writeBlock(mTag,16,data);
                    if(re == false){
                        textView.setText("写卡失败-9-16block");
                        return;
                    }
                    data = formatFromModel(kbCardRecord10);
                    re = M1CardUtils.writeBlock(mTag,17,data);
                    if(re == false){
                        textView.setText("写卡失败-10-17block");
                        return;
                    }


                    textView.setText("写卡成功");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mNfcAdapter = M1CardUtils.isNfcAble(this);
        M1CardUtils.setPendingIntent(PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()), 0));
        Log.e("onNewIntent","onNewIntent");
        mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String CardId =ByteArrayToHexString(mTag.getId());
        Toast.makeText(this, CardId, Toast.LENGTH_LONG).show();


    }

    //转为16进制字符串
    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
                "B", "C", "D", "E", "F" };
        String out = "";


        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch(this, M1CardUtils.getPendingIntent(),
                    null, null);
        }
    }

}