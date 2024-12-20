package com.seaguard.ui.reports;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.seaguard.R;
import com.seaguard.databinding.FragmentReportsBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;
    private ReportsViewModel reportsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);

        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout reportsContainer = binding.reportsContainer;

        reportsViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            if (reports != null) {
                reportsContainer.removeAllViews();

                for (Map<String, Object> report : reports) {
                    String idArea = getIdArea(report);
                    String date = getDate(report);

                    View reportView = createReportView(idArea, date);
                    reportsContainer.addView(reportView);
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getIdArea(Map<String, Object> report) {
        Object idAreaObj = report.get("idArea");
        if (idAreaObj instanceof String) {
            return (String) idAreaObj;
        } else if (idAreaObj instanceof Long) {
            return String.valueOf(idAreaObj);
        } else {
            return "Unknown Area";
        }
    }

    private String getDate(Map<String, Object> report) {
        Object dateObj = report.get("date");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        if (dateObj instanceof com.google.firebase.Timestamp) {
            Date date = ((com.google.firebase.Timestamp) dateObj).toDate();
            return sdf.format(date);
        } else if (dateObj instanceof String) {
            return (String) dateObj;
        } else if (dateObj instanceof Long) {
            Date date = new Date((Long) dateObj);
            return sdf.format(date);
        } else {
            return "Unknown date";
        }
    }


    private View createReportView(String idArea, String date) {
        LinearLayout reportLayout = new LinearLayout(requireContext());
        reportLayout.setOrientation(LinearLayout.VERTICAL);

        int colorPrimary = ContextCompat.getColor(requireContext(), R.color.lavender);

        reportLayout.setBackgroundColor(colorPrimary);

        TextView idAreaView = new TextView(requireContext());
        idAreaView.setText(idArea);
        idAreaView.setTextSize(16);
        idAreaView.setTypeface(null, Typeface.BOLD);
        idAreaView.setPadding(25, 25, 0, 0);

        TextView dateView = new TextView(requireContext());
        dateView.setText(date);
        dateView.setTextSize(14);
        dateView.setPadding(25, 5, 0, 0);

        reportLayout.addView(idAreaView);
        reportLayout.addView(dateView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(1, 16, 0, 16);
        reportLayout.setLayoutParams(params);

        reportLayout.setMinimumHeight(175);

        return reportLayout;
    }






}
