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
    String blueSwitch;
    String redSwitch;
    String scale;
    String compressedData;
    String autoQuestCompressed;
    String faceBossCompressed;
    String blueSwitchCompressed;
    String redSwitchCompressed;
    String scaleCompressed;
    Boolean isRed;
    Boolean isMute;
    Integer boostForPowerUp;
    Integer forceForPowerUp;
    Integer levitateForPowerUp;
    Integer boostInVaultFinal;
    Integer forceInVaultFinal;
    Integer levitateInVaultFinal;
    Boolean didAutoQuest;
    Boolean didFaceBoss;
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

        boostForPowerUp = intent.getExtras().getInt("boostForPowerUp");
        forceForPowerUp = intent.getExtras().getInt("forceForPowerUp");
        levitateForPowerUp = intent.getExtras().getInt("levitateForPowerUp");
        boostInVaultFinal = intent.getExtras().getInt("boostInVaultFinal");
        forceInVaultFinal = intent.getExtras().getInt("forceInVaultFinal");
        levitateInVaultFinal = intent.getExtras().getInt("levitateInVaultFinal");

        didAutoQuest = intent.getExtras().getBoolean("didAutoQuest");
        didFaceBoss = intent.getExtras().getBoolean("didFaceBoss");
        score = intent.getExtras().getString("score");
        foul = intent.getExtras().getString("foul");

        blueSwitch = intent.getExtras().getString("blueSwitch");
        redSwitch = intent.getExtras().getString("redSwitch");
        scale = intent.getExtras().getString("scale");

        teamOneDataName = intent.getStringArrayListExtra("teamOneDataName");
        teamOneDataScore = intent.getStringArrayListExtra("teamOneDataScore");
        teamTwoDataName = intent.getStringArrayListExtra("teamTwoDataName");
        teamTwoDataScore = intent.getStringArrayListExtra("teamTwoDataScore");
        teamThreeDataName = intent.getStringArrayListExtra("teamThreeDataName");
        teamThreeDataScore = intent.getStringArrayListExtra("teamThreeDataScore");

        isMute = intent.getExtras().getBoolean("isMute");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.qr, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
            allianceCompressed = "0";
        } else {
            allianceCompressed = "1"; }
        if (didAutoQuest.equals(false)){
            autoQuestCompressed = "0";
        } else {
            autoQuestCompressed = "1"; }
        if (didFaceBoss.equals(false)){
            faceBossCompressed = "0";
        } else {
            faceBossCompressed = "1"; }
        if (blueSwitch.equals("blue")){
            blueSwitchCompressed = "0";
        } else {
            blueSwitchCompressed = "1"; }
        if (redSwitch.equals("blue")){
            redSwitchCompressed = "0";
        } else {
            redSwitchCompressed = "1"; }
        if (scale.equals("blue")){
            scaleCompressed = "0";
        } else {
            scaleCompressed= "1"; }
        compressedData = "S!" + matchNumber + "_" + allianceCompressed + "|p(g" + boostForPowerUp.toString() + "h" + forceForPowerUp.toString() + "i" + levitateForPowerUp.toString() + "),v(g" + boostInVaultFinal.toString() + "h" + forceInVaultFinal.toString() + "i" + levitateInVaultFinal.toString() + "),q" + autoQuestCompressed + ",b" + faceBossCompressed + ",s" + score + ",j" + foul + ",r(d" + blueSwitchCompressed + "e" + scaleCompressed + "f" + redSwitchCompressed + "),t(" + "M" + teamNumberOne + "B" + teamOneDataScore.get(3) + "G" + teamOneDataScore.get(1) + "A" + teamOneDataScore.get(2) + "D" + teamOneDataScore.get(0) + "S" + teamOneDataScore.get(4) + "N[" + superNotesOne + "]," + "M" + teamNumberTwo + "B" + teamTwoDataScore.get(3) + "G" + teamTwoDataScore.get(1) + "A" + teamTwoDataScore.get(2) + "D" + teamTwoDataScore.get(0) + "S" + teamTwoDataScore.get(4) + "N[" + superNotesTwo + "]," + "M" + teamNumberThree + "B" + teamThreeDataScore.get(3) + "G" + teamThreeDataScore.get(1) + "A" + teamThreeDataScore.get(2) + "D" + teamThreeDataScore.get(0) + "S" + teamThreeDataScore.get(4) + "N[" + superNotesThree + "])";

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



}
