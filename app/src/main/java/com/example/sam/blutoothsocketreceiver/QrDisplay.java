package com.example.sam.blutoothsocketreceiver;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sam.blutoothsocketreceiver.Fields.Bay;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QrDisplay extends ActionBarActivity {
    String matchNumber;
    String alliance;
    String allianceCompressed;
    String teamNumberOne;
    String teamNumberTwo;
    String teamNumberThree;
    String superNotesOne;
    String superNotesTwo;
    String superNotesThree;
    String score;
    String foul;
    String compressedData;
    Boolean isRed;
    Boolean isMute;
    ArrayList<String> teamOneDataName;
    ArrayList<String> teamOneDataScore;
    ArrayList<String> teamTwoDataName;
    ArrayList<String> teamTwoDataScore;
    ArrayList<String> teamThreeDataName;
    ArrayList<String> teamThreeDataScore;
    ImageView QRImage;
    Intent intent;
    Activity context;
    File dir;
    PrintWriter file;
    String leftNear;
    String leftMid;
    String leftFar;
    String rightNear;
    String rightMid;
    String rightFar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_display);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/Super_scout_data");
        context = this;
        intent = getIntent();
        getExtras();
        QRImage = (ImageView) findViewById(R.id.QRCode_Display);
        convertValues();
        createCompressedFormat();
        displayQR(compressedData);
    }

    public void getExtras(){
        matchNumber = intent.getExtras().getString("matchNumber");
        alliance = intent.getExtras().getString("alliance");
        teamNumberOne = intent.getExtras().getString("teamNumberOne");
        teamNumberTwo = intent.getExtras().getString("teamNumberTwo");
        teamNumberThree = intent.getExtras().getString("teamNumberThree");

        superNotesOne = intent.getExtras().getString("superNotesOne");
        superNotesTwo = intent.getExtras().getString("superNotesTwo");
        superNotesThree = intent.getExtras().getString("superNotesThree");

        score = intent.getExtras().getString("score");
        foul = intent.getExtras().getString("foul");

        teamOneDataName = intent.getStringArrayListExtra("teamOneDataName");
        teamOneDataScore = intent.getStringArrayListExtra("teamOneDataScore");
        teamTwoDataName = intent.getStringArrayListExtra("teamTwoDataName");
        teamTwoDataScore = intent.getStringArrayListExtra("teamTwoDataScore");
        teamThreeDataName = intent.getStringArrayListExtra("teamThreeDataName");
        teamThreeDataScore = intent.getStringArrayListExtra("teamThreeDataScore");

        leftNear = intent.getStringExtra(Constants.leftNear);
        leftMid = intent.getStringExtra(Constants.leftMid);
        leftFar = intent.getStringExtra(Constants.leftFar);
        rightNear = intent.getStringExtra(Constants.rightNear);
        rightMid = intent.getStringExtra(Constants.rightMid);
        rightFar = intent.getStringExtra(Constants.rightFar);

        isMute = intent.getExtras().getBoolean("isMute");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qr, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.backToMainActivity) {
            Intent backToHome = new Intent(context, MainActivity.class);
            backToHome.putExtra("number", matchNumber);
            backToHome.putExtra("leftViewColor", intent.getExtras().getString("leftViewColor"));
            if (allianceCompressed.equals("0")){
                isRed = false;
            } else {
                isRed = true;
            }
            backToHome.putExtra("mute", isMute);
            backToHome.putExtra("shouldBeRed", isRed);
            startActivity(backToHome);
        }
        return super.onOptionsItemSelected(item);
    }
    public void createCompressedFormat(){
        if (alliance.equals("Blue Alliance")){
            allianceCompressed = "B";
        } else {
            allianceCompressed = "R"; }

        compressedData = "S!"
                + matchNumber
                + "_"
                + allianceCompressed
                + "a{b"
                + leftNear
                + ";c"
                + leftMid
                + ";d"
                + leftFar
                + ";e"
                + rightNear
                + ";f"
                + rightMid
                + ";g"
                + rightFar;

        new Thread() {
            @Override
            public void run() {
                try {
                    file = null;
                    //make the directory of the file
                    dir.mkdir();
                    //can delete when doing the actual thing
                    file = new PrintWriter(new FileOutputStream(new File(dir, ("Q" + matchNumber + "_"  + new SimpleDateFormat("MM-dd-yyyy-H:mm:ss").format(new Date())))));
                } catch (IOException IOE) {
                    return;
                }
                file.println(compressedData);
                file.close();
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Sent Match Data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }.start();
    }

    public void displayQR(String qrCode){
        try {
            //setting size of qr code
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallestDimension = width < height ? width : height;
            //setting parameters for qr code
            String charset = "UTF-8"; // or "ISO-8859-1"
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap =new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCode, charset, hintMap, smallestDimension, smallestDimension);
        } catch (Exception ex) {
            Log.e("QrGenerate",ex.getMessage());
        }
    }
    public void createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth){

        try {
            //generating qr code in bitmatrix type
            BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset), BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
            //converting bitmatrix to bitmap
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[width * height];
            // All are 0, or black, by default
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = matrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //setting bitmap to image view
            QRImage.setImageBitmap(null);
            QRImage.setImageBitmap(bitmap);
        }catch (Exception er){
            Log.e("QrGenerate",er.getMessage());
        }
    }

    public void convertValues() {
        if (leftNear.equals(Bay.yellowValue)) {
            leftNear = "L";
        } else if (leftNear.equals(Bay.orangeValue)) {
            leftNear = "G";
        }
        if (leftMid.equals(Bay.yellowValue)) {
            leftMid = "L";
        } else if (leftMid.equals(Bay.orangeValue)) {
            leftMid = "G";
        }
        if (leftFar.equals(Bay.yellowValue)) {
            leftFar = "L";
        } else if (leftFar.equals(Bay.orangeValue)) {
            leftFar = "G";
        }
        if (rightNear.equals(Bay.yellowValue)) {
            rightNear = "L";
        } else if (rightNear.equals(Bay.orangeValue)) {
            rightNear = "G";
        }
        if (rightMid.equals(Bay.yellowValue)) {
            rightMid = "L";
        } else if (rightMid.equals(Bay.orangeValue)) {
            rightMid = "G";
        }
        if (rightFar.equals(Bay.yellowValue)) {
            rightFar = "L";
        } else if (rightFar.equals(Bay.orangeValue)) {
            rightFar = "G";
        }






    }
}
