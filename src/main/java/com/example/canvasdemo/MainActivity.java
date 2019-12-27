package com.example.canvasdemo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.lang.*;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    private ArrayList DataList;
    private ArrayList CentreList;
    private ArrayList prevDataList;
    private ArrayList prevCentreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int N = 10;
        int K = 3;
        prevDataList = new ArrayList();
        prevCentreList = new ArrayList();
        initialize(N, K, 0);
        addListenerOnButton(N, K);
        addListenerOnSeekBar(N, K, 200, 10);
    }

    public void initialize(int N, int K, int mode) {
        // init
        int x, y, color;
        Random r = new Random();
        DemoView d1 = (DemoView) findViewById(R.id.DemoView);
        TextView t3 = (TextView) findViewById(R.id.TextView3);

        // creating list objects
        DataList = new ArrayList();
        CentreList = new ArrayList();

        if (mode == 1) {
            // populating DataList
            for (int i = 0; i < N; i++) {
                color = Color.LTGRAY;
                x = Math.abs(r.nextInt(720));
                y = Math.abs(r.nextInt(720));
                Point p = new Point(x, y, color);
                DataList.add(p);
            }
            CentreList = prevCentreList;
        } else if (mode == 2) {
            // populating CentreList
            for (int i = 0; i < K; i++) {
                color = Color.rgb(r.nextInt(200), r.nextInt(200), r.nextInt(200));
                x = Math.abs(r.nextInt(720));
                y = Math.abs(r.nextInt(720));
                Point p = new Point(x, y, color);
                CentreList.add(p);
            }
            DataList = prevDataList;
        } else {
            // populating DataList
            for (int i = 0; i < N; i++) {
                color = Color.LTGRAY;
                x = Math.abs(r.nextInt(720));
                y = Math.abs(r.nextInt(720));
                Point p = new Point(x, y, color);
                DataList.add(p);
            }
            // populating CentreList
            for (int i = 0; i < K; i++) {
                color = Color.rgb(r.nextInt(200), r.nextInt(200), r.nextInt(200));
                x = Math.abs(r.nextInt(720));
                y = Math.abs(r.nextInt(720));
                Point p = new Point(x, y, color);
                CentreList.add(p);
            }
        }

        // saving data
        prevDataList = DataList;
        prevCentreList = CentreList;

        // print points and centroids in console
        print_points(DataList);
        print_centre(CentreList);

        // calculating rms
        double distance = rms(DataList, CentreList);
        t3.setText(String.format("Mean square point-centroid distance: %.2f", distance));

        // drawing
        d1.setDataList(DataList);
        d1.setCentreList(CentreList);
        d1.invalidate();
    }

    public void addListenerOnButton(int N, int K) {
        final Button b1 = (Button) findViewById(R.id.Button1);
        final TextView t3 = (TextView) findViewById(R.id.TextView3);
        final DemoView d1 = (DemoView) findViewById(R.id.DemoView);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // command
                String str = (String) b1.getText();
                if (str.compareTo("Update Centroid") == 0) {
                    b1.setText("Find Closest Centroid");
                    update_centroid(DataList, CentreList);
                    b1.setBackgroundColor(getResources().getColor(R.color.myBlue));
                }
                if (str.compareTo("Find Closest Centroid") == 0) {
                    b1.setText("Update Centroid");
                    update_points(DataList, CentreList);
                    b1.setBackgroundColor(getResources().getColor(R.color.myGreen));
                }

                // calculating rms
                double distance = rms(DataList, CentreList);
                String oldRMS = (String) t3.getText();
                String newRMS = String.format("Mean square point-centroid distance: %.2f", distance);

                // toasting convergence message
                if (oldRMS.compareTo(newRMS) == 0) {
                    b1.setText("Clustering done!");
                    b1.setBackgroundColor(getResources().getColor(R.color.myPink));
                    //  Toast.makeText(getApplicationContext(), "|Success|", Toast.LENGTH_SHORT).show();
                }
                t3.setText(newRMS);

                // drawing
                d1.setDataList(DataList);
                d1.setCentreList(CentreList);
                d1.invalidate();
            }
        });
    }

    private static void update_centroid(ArrayList DataList, ArrayList CentreList) {
        System.out.println("\nupdate_centroid:");
        int color;
        for (int i = 0; i < CentreList.size(); i++) {
            Point centre = (Point) CentreList.get(i);
            color = centre.getColor();
            Point p;
            int n = 0;
            double sumX = 0.0, sumY = 0.0;
            for (int j = 0; j < DataList.size(); j++) {
                p = (Point) DataList.get(j);
                if (p.getCluster() == i) {
                    sumX += p.getX();
                    sumY += p.getY();
                    n++;
                }
            }
            if (n != 0) {
                sumX /= n;
                sumY /= n;
            } else {
                sumX = centre.getX();
                sumY = centre.getY();
            }
            CentreList.remove(i);
            CentreList.add(i, new Point(sumX, sumY, color));

            System.out.println("N = " + n + " --> " + centre.toString());
        }
    }

    private static void update_points(ArrayList DataList, ArrayList CentreList) {
        System.out.println("\nupdate_points:");
        int ind;
        int color;
        double min, dis;
        for (int i = 0; i < DataList.size(); i++) {
            Point p = (Point) DataList.get(i);
            min = 2000;
            ind = 0;
            color = 0;
            Point centre = null;
            for (int j = 0; j < CentreList.size(); j++) {
                centre = (Point) CentreList.get(j);
                dis = p.distance(centre);
                if (dis < min) {
                    min = dis;
                    ind = j;
                    color = centre.getColor();
                }
            }
            p.setCluster(ind);
            p.setColor(color);
            System.out.println(ind + " <-> " + p.toString());
        }
    }

    private static void print_points(ArrayList DataList) {
        System.out.print("Points: ");
        for (int i = 0; i < DataList.size(); i++) {
            Point point = (Point) DataList.get(i);
            System.out.print(point.toString() + ", ");
        }
        System.out.println("");
    }

    private static void print_centre(ArrayList CentreList) {
        System.out.print("Centroids: ");
        for (int i = 0; i < CentreList.size(); i++) {
            Point centre = (Point) CentreList.get(i);
            System.out.print(centre.toString() + ", ");
        }
        System.out.println("");
    }

    private static double rms(ArrayList DataList, ArrayList CentreList) {

        int total = 0;
        double meanSquaredDistance = 0;
        for (int i = 0; i < CentreList.size(); i++) {
            Point centre = (Point) CentreList.get(i);

            int count = 0;
            for (int j = 0; j < DataList.size(); j++) {
                Point p = (Point) DataList.get(j);
                if (p.getCluster() == i) {
                    double dist = p.distance(centre);
                    meanSquaredDistance += dist * dist;
                    count++;
                    total++;
                }
            }
            System.out.println(i + " -> " + count);
        }
        System.out.println("Total" + " -> " + total);

        meanSquaredDistance /= DataList.size();
        return meanSquaredDistance;
    }

    public void addListenerOnSeekBar(final int N, final int K, final int maxN, final int maxK) {
        final SeekBar s1 = (SeekBar) findViewById(R.id.SeekBar1);
        s1.setMax(maxN);
        s1.setProgress(N);
        final SeekBar s2 = (SeekBar) findViewById(R.id.SeekBar2);
        s2.setMax(maxK);
        s2.setProgress(K);
        final TextView t1 = (TextView) findViewById(R.id.TextView1);
        t1.setText("N = " + N);
        final TextView t2 = (TextView) findViewById(R.id.TextView2);
        t2.setText("K = " + K);
        final Button b1 = (Button) findViewById(R.id.Button1);

        s1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                t1.setText("N = " + progress);
                initialize(s1.getProgress(), s2.getProgress(), 1);
                b1.setText("Find Closest Centroid");
                b1.setBackgroundColor(getResources().getColor(R.color.myBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        s2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                t2.setText("K = " + progress);
                initialize(s1.getProgress(), s2.getProgress(), 2);
                b1.setText("Find Closest Centroid");
                b1.setBackgroundColor(getResources().getColor(R.color.myBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}
