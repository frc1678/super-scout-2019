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

import org.jcodec.common.DictionaryCompressor;
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

    Integer kValOne;
    Integer pValOne;
    Integer dValOne;
    Integer kValTwo;
    Integer pValTwo;
    Integer dValTwo;
    Integer kValThree;
    Integer pValThree;
    Integer dValThree;

    String noShowOne;
    String noShowTwo;
    String noShowThree;

    String didRocketRP;
    String didHabClimb;

    String teamOneConflict;
    String teamTwoConflict;
    String teamThreeConflict;

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
        Log.e("COMPRESSED", compressedData);
        displayQR(compressedData);
    }

    public void getExtras() {
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

        //TODO: Currently, the values below display as 0
        // in the QR generation. This needs to be fixed ASAP.

        dValOne = Integer.valueOf(intent.getExtras().getString("teamOneDocking"));
        kValOne = Integer.valueOf(intent.getExtras().getString("teamOneKnocking"));
        pValOne = Integer.valueOf(intent.getExtras().getString("teamOnePathblocking"));
        dValTwo = Integer.valueOf(intent.getExtras().getString("teamTwoDocking"));
        kValTwo = Integer.valueOf(intent.getExtras().getString("teamTwoKnocking"));
        pValTwo = Integer.valueOf(intent.getExtras().getString("teamTwoPathblocking"));
        dValThree = Integer.valueOf(intent.getExtras().getString("teamThreeDocking"));
        kValThree = Integer.valueOf(intent.getExtras().getString("teamThreeKnocking"));
        pValThree = Integer.valueOf(intent.getExtras().getString("teamThreePathblocking"));

        isMute = intent.getExtras().getBoolean("isMute");

        noShowOne = intent.getExtras().getString("noShowOne");
        noShowTwo = intent.getExtras().getString("noShowTwo");
        noShowThree = intent.getExtras().getString("noShowThree");

        didRocketRP = intent.getExtras().getString("didRocketRP");
        didHabClimb = intent.getExtras().getString("didHabClimb");

        teamOneConflict = intent.getExtras().getString("teamOneConflict");
        teamTwoConflict = intent.getExtras().getString("teamTwoConflict");
        teamThreeConflict = intent.getExtras().getString("teamThreeConflict");


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
            if (allianceCompressed.equals("B")) {
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

    public void createCompressedFormat() {
        if (alliance.equals("Blue Alliance")) {
            allianceCompressed = "B";
        } else {
            allianceCompressed = "R";
        }

        compressedData = "S!Q"
                + matchNumber
                + "-"
                + allianceCompressed
                + "|a{b"
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
                + rightFar
                + "},h"
                + generateNoShowList(noShowOne, noShowTwo, noShowThree).toString().replace(", ", ";") + ","
                + getStringAlliance(alliance)
                + score + ","
                + getFoulAlliance(alliance)
                + foul + ","
                + getRocketRPAlliance(alliance)
                + didRocketRP + ","
                + getHabClimbAlliance(alliance)
                + didHabClimb
                + "_1{u"
                + teamNumberOne
                + ";v"
                + teamOneDataScore.get(1)
                + ";w"
                + teamOneDataScore.get(0)
                + ";x"
                + generateTeamDefenseMap(dValOne, kValOne, pValOne).toString().replace(", ", ";")
                + ";y"
                + teamOneDataScore.get(4)
                + ";z\""
                + superNotesOne
                + "\";A"
                + teamOneDataScore.get(2)
                + ";B"
                + teamOneDataScore.get(3)
                + ";j"
                + teamOneConflict
                + "},2{u"
                + teamNumberTwo
                + ";v"
                + teamTwoDataScore.get(1)
                + ";w"
                + teamTwoDataScore.get(0)
                + ";x"
                + generateTeamDefenseMap(dValTwo, kValTwo, pValTwo).toString().replace(", ", ";")
                + ";y"
                + teamTwoDataScore.get(4)
                + ";z\""
                + superNotesTwo
                + "\";A"
                + teamTwoDataScore.get(2)
                + ";B"
                + teamTwoDataScore.get(3)
                + ";j"
                + teamTwoConflict
                + "},3{u"
                + teamNumberThree
                + ";v"
                + teamThreeDataScore.get(1)
                + ";w"
                + teamThreeDataScore.get(0)
                + ";x"
                + generateTeamDefenseMap(dValThree, kValThree, pValThree).toString().replace(", ", ";")
                + ";y"
                + teamThreeDataScore.get(4)
                + ";z\""
                + superNotesThree
                + "\";A"
                + teamThreeDataScore.get(2)
                + ";B"
                + teamThreeDataScore.get(3)
                + ";j"
                + teamThreeConflict
                + "}";

        new Thread() {

            @Override
            public void run() {
                try {
                    file = null;
                    //make the directory of the file
                    dir.mkdir();
                    //can delete when doing the actual thing
                    file = new PrintWriter(new FileOutputStream(new File(dir, ("Q" + matchNumber + "_" + new SimpleDateFormat("MM-dd-yyyy-H:mm:ss").format(new Date())))));
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

    public void displayQR(String qrCode) {
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
            Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            createQRCode(qrCode, charset, hintMap, smallestDimension, smallestDimension);
        } catch (Exception ex) {
            Log.e("QrGenerate", ex.getMessage());
        }
    }

    public void createQRCode(String qrCodeData, String charset, Map hintMap, int qrCodeheight, int qrCodewidth) {

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
        } catch (Exception er) {
            Log.e("QrGenerate", er.getMessage());
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

        if (noShowOne.equals("true")) {
            noShowOne = teamNumberOne;
        } else {
            noShowOne = "";
        }
        if (noShowTwo.equals("true")) {
            noShowTwo = teamNumberTwo;
        } else {
            noShowTwo = "";
        }
        if (noShowThree.equals("true")) {
            noShowThree = teamNumberThree;
        } else {
            noShowThree = "";
        }
        if (didRocketRP.equals("true")) {
            didRocketRP = "T";
        } else {
            didRocketRP = "F";
        }
        Log.e("didHabClimb", didHabClimb + "");
        if (didHabClimb.equals("true")) {
            didHabClimb = "T";
        } else {
            didHabClimb = "F";
        }
        if (teamOneConflict.equals("true")) {
            teamOneConflict = "T";
        } else {
            teamOneConflict = "F";
        }
        if (teamTwoConflict.equals("true")) {
            teamTwoConflict = "T";
        } else {
            teamTwoConflict = "F";
        }
        if (teamThreeConflict.equals("true")) {
            teamThreeConflict = "T";
        } else {
            teamThreeConflict = "F";
        }


    }

    public String getStringAlliance(String alliance) {
        if (alliance.equals("Red Alliance")) {
            return "k";
        } else if (alliance.equals("Blue Alliance")) {
            return "m";
        }
        return "null";
    }

    public String getFoulAlliance(String alliance) {
        if (alliance.equals("Red Alliance")) {
            return "n";
        } else if (alliance.equals("Blue Alliance")) {
            return "p";
        }
        return "null";
    }

    public String getRocketRPAlliance(String alliance) {
        if (alliance.equals("Red Alliance")) {
            return "r";
        } else if (alliance.equals("Blue Alliance")) {
            return "q";
        }
        return "null";
    }

    public String getHabClimbAlliance(String alliance) {
        if (alliance.equals("Red Alliance")) {
            return "t";
        } else if (alliance.equals("Blue Alliance")) {
            return "s";
        }
        return "null";
    }

    public ArrayList<String> generateNoShowList(String noShowOne, String noShowTwo, String noShowThree) {
        ArrayList<String> noShowList = new ArrayList<>();
        if (!noShowOne.equals("")) {
            noShowList.add(noShowOne);
        }
        if (!noShowTwo.equals("")) {
            noShowList.add(noShowTwo);
        }
        if (!noShowThree.equals("")) {
            noShowList.add(noShowThree);
        }
        return noShowList;
    }

    public String generateTeamDefenseMap(Integer knockingValue, Integer dockingValue, Integer pathblockingValue) {
        String knocking = "";
        String docking = "";
        String pathblocking= "";
        if (knockingValue != null) {
            knocking = String.valueOf(knockingValue);
        }
        if (dockingValue != null) {
            docking = String.valueOf(dockingValue);
        }
        if (pathblockingValue != null) {
            pathblocking = String.valueOf(pathblockingValue);
        }
        return "{C"+knocking+"D"+docking+"E"+pathblocking+"}";
    }
}
