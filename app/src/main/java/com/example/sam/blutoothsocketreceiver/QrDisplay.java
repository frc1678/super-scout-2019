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
import android.content.SharedPreferences;

import com.example.sam.blutoothsocketreceiver.Fields.Bay;
import com.example.sam.blutoothsocketreceiver.Utils.TimerUtil;
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

    int[][] defensiveEffectivenessValues = new int[3][6];

    String teamOneDefense;
    String teamTwoDefense;
    String teamThreeDefense;

    String noShowOne;
    String noShowTwo;
    String noShowThree;

    String didRocketRP;
    String didHabClimb;

    String teamOneConflict;
    String teamTwoConflict;
    String teamThreeConflict;

    String[][] allianceDataStructure = new String[3][3];
    String[][] opponentDataStructure = new String[3][3];
    ArrayList<int[]> counterStats = new ArrayList<>();

    public ArrayList<Map<String, String>> timelineRobotOne = new ArrayList<>();
    public ArrayList<Map<String, String>> timelineRobotTwo = new ArrayList<>();
    public ArrayList<Map<String, String>> timelineRobotThree = new ArrayList<>();

    ArrayList<Map<String, String>> opponentRobotOneDataStructure = new ArrayList<>();
    ArrayList<Map<String, String>> opponentRobotTwoDataStructure = new ArrayList<>();
    ArrayList<Map<String, String>> opponentRobotThreeDataStructure = new ArrayList<>();

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

    public String SHARED_PREF = "super_scout_sp";

    public void getExtras() {
        matchNumber = intent.getExtras().getString("matchNumber");
        alliance = intent.getExtras().getString("alliance");
        allianceDataStructure = (String[][]) getIntent().getSerializableExtra("allianceDataStructure");
        opponentDataStructure = (String[][]) getIntent().getSerializableExtra("opponentDataStructure");
        counterStats = (ArrayList<int[]>) getIntent().getSerializableExtra("counterStats");

        timelineRobotOne = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("timelineOne");
        timelineRobotTwo = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("timelineTwo");
        timelineRobotThree = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("timelineThree");
        opponentRobotOneDataStructure = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("opponentRobotOneData");
        opponentRobotTwoDataStructure = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("opponentRobotTwoData");
        opponentRobotThreeDataStructure = (ArrayList<Map<String,String>>) getIntent().getSerializableExtra("opponentRobotThreeData");

        defensiveEffectivenessValues = (int[][]) getIntent().getSerializableExtra("defensiveEffectivenessValues");

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
                //teamONE
                + "|u"
                + allianceDataStructure[0][2]
                + ";v"
                + counterStats.get(0)[1]
                + ";w"
                + counterStats.get(0)[0]
                + ";x"
                + allianceDataStructure[0][0]
                + ";F"
                + String.valueOf(timelineRobotOne).replace(" ", "").replace("{", "")
                .replace("type", "G").replace("=", "").replace(",", "?").replace("}", "")
                .replace("startDefense", "a").replace("endDefense", "b").replace("time", "H").replace("?G",",").replace(",b",",Gb").replace(",a",",Gb")
                + ";E[u" + opponentDataStructure[0][2]
                + "?y" + defensiveEffectivenessValues[0][0]
                + "?z" + defensiveEffectivenessValues[0][3]
                + ",u" + opponentDataStructure[1][2]
                + "?y" + defensiveEffectivenessValues[0][1]
                + "?z" + defensiveEffectivenessValues[0][4]
                + ",u" + opponentDataStructure[2][2]
                + "?y" + defensiveEffectivenessValues[0][2]
                + "?z" + defensiveEffectivenessValues[0][5]
                + "]_"
                //teamTWO
                + "u"
                + allianceDataStructure[1][2]
                + ";v"
                + counterStats.get(1)[1]
                + ";w"
                + counterStats.get(1)[0]
                + ";x"
                + allianceDataStructure[1][0]
                + ";F"
                + String.valueOf(timelineRobotTwo).replace(" ", "").replace("{", "")
                .replace("type", "G").replace("=", "").replace(",", "?").replace("}", "")
                .replace("startDefense", "a").replace("endDefense", "b").replace("time", "H").replace("?G",",").replace(",b",",Gb").replace(",a",",Gb")
                + ";E[u" + opponentDataStructure[0][2]
                + "?y" + defensiveEffectivenessValues[1][0]
                + "?z" + defensiveEffectivenessValues[1][3]
                + ",u" + opponentDataStructure[1][2]
                + "?y" + defensiveEffectivenessValues[1][1]
                + "?z" + defensiveEffectivenessValues[1][4]
                + ",u" + opponentDataStructure[2][2]
                + "?y" + defensiveEffectivenessValues[1][2]
                + "?z" + defensiveEffectivenessValues[1][5]
                + "]_"
                + "u"
                + allianceDataStructure[2][2]
                + ";v"
                + counterStats.get(2)[1]
                + ";w"
                + counterStats.get(2)[0]
                + ";x"
                + allianceDataStructure[2][0]
                + ";F"
                + String.valueOf(timelineRobotThree).replace(" ", "").replace("{", "")
                .replace("type", "G").replace("=", "").replace(",", "?").replace("}", "")
                .replace("startDefense", "a").replace("endDefense", "b").replace("time", "H").replace("?G",",").replace(",b",",Gb").replace(",a",",Gb")
                + ";E[u" + opponentDataStructure[0][2]
                + "?y" + defensiveEffectivenessValues[2][0]
                + "?z" + defensiveEffectivenessValues[2][3]
                + ",u" + opponentDataStructure[1][2]
                + "?y" + defensiveEffectivenessValues[2][1]
                + "?z" + defensiveEffectivenessValues[2][4]
                + ",u" + opponentDataStructure[2][2]
                + "?y" + defensiveEffectivenessValues[2][2]
                + "?z" + defensiveEffectivenessValues[2][5] + "]";

        Log.e("rree", String.valueOf(compressedData));

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


}
