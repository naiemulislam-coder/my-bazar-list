package com.naiemul.mybazarlist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class DashBoardFragment extends Fragment {

       private BarChart barChart;

      private TextView tvOverallTotalBazar;
      private   BazarDatabase db;

    public DashBoardFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_dash_board, container, false);

                barChart = view.findViewById(R.id.dashboardChart);
                tvOverallTotalBazar = view.findViewById(R.id.tvTotalBazarOverall); // আপনার XML এ এই আইডিটি দিন
                db = BazarDatabase.getInstance(getContext());

        // ১. সর্বমোট কয়টি বাজার হয়েছে (শুরু থেকে আজ পর্যন্ত)
                db.bazarDao().getTotalBazarCountOverall().observe(getViewLifecycleOwner(), count -> {
                    if (count != null) {
                        tvOverallTotalBazar.setText("সর্বমোট বাজার করেছেন: " + count + " টি");
                    }
                });

        // ২. মাসিক রিপোর্ট (খরচ এবং বাজারের সংখ্যা)
                db.bazarDao().getMonthlySummary().observe(getViewLifecycleOwner(), summaryList -> {
                    if (summaryList != null && summaryList.size() > 0) {
                        updateChartWithDetails(summaryList);
                    }
                });

        return view;
    }

    //=========================================================

    private void updateChartWithDetails(List<BazarDao.MonthlySummary> list) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> monthLabels = new ArrayList<>(); // মাসের নাম (যেমন: 05-2024) রাখার জন্য

        for (int i = 0; i < list.size(); i++) {
            BazarDao.MonthlySummary data = list.get(i);

            // গ্রাফে বারের উচ্চতা হবে মাসের মোট খরচ (totalCost)
            entries.add(new BarEntry(i, (float) data.totalCost));

            // ডাটাবেস থেকে পাওয়া মাসের নাম লিস্টে রাখা (যা গ্রাফের নিচে দেখাবে)
            monthLabels.add(data.month);
        }

        // আপনার তৈরি করা getBarDataSet মেথডটি কল করা হচ্ছে
        BarDataSet dataSet = getBarDataSet(entries);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // --- X-অক্ষে (নিচের দিকে) মাসের নাম সেট করার কোড ---
        com.github.mikephil.charting.components.XAxis xAxis = barChart.getXAxis();

        // মাসের নামের লিস্টটি গ্রাফের নিচে সেট করা
        xAxis.setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(monthLabels));

        // মাসের নামগুলো গ্রাফের নিচে (Bottom) দেখাবে
        xAxis.setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        // যাতে প্রতিটি বারের নিচে একটি করে মাসের নাম থাকে
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(monthLabels.size());

        // গ্রাফের ভেতরের লম্বা গ্রিড লাইনগুলো বন্ধ করা (দেখতে সুন্দর লাগে)
        xAxis.setDrawGridLines(false);
        // ------------------------------------------------

        // গ্রাফের ডানদিকের সংখ্যাগুলো হাইড করা (ঐচ্ছিক, ডিজাইন ক্লিন রাখার জন্য)
        barChart.getAxisRight().setEnabled(false);

        // গ্রাফের ডেসক্রিপশন এবং এনিমেশন
        barChart.getDescription().setText("মাসের ভিত্তিতে বাজারের হিসাব");
        barChart.animateY(1000); // ১ সেকেন্ডের এনিমেশন
        barChart.invalidate(); // গ্রাফটি রিফ্রেশ করা
    }

    // এই মেথডটিও নিশ্চিত করুন আপনার কোডে আছে
    @NonNull
    private BarDataSet getBarDataSet(ArrayList<BarEntry> entries) {
        BarDataSet dataSet = new BarDataSet(entries, "মাসিক খরচ (টাকা)");
        dataSet.setColors(com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueTextSize(12f);

        // বারের ওপর টাকার চিহ্ন (৳) সহ সংখ্যা দেখাবে
        dataSet.setValueFormatter(new com.github.mikephil.charting.formatter.ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return (int) value + " ৳";
            }
        });
        return dataSet;
    }

    //====================================================
}