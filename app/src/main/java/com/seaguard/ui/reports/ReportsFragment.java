package com.seaguard.ui.reports;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.seaguard.R;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.FragmentReportsBinding;
import com.seaguard.ui.home.AddReportActivity;

public class ReportsFragment extends Fragment {

    private FragmentReportsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ReportsViewModel reportsViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);

        binding = FragmentReportsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout reportsContainer = binding.reportsContainer;

        setEmptySting(true);

        reportsViewModel.getReports().observe(getViewLifecycleOwner(), reports -> {
            if (reports != null) {
                reportsContainer.removeAllViews();
                for(ReportModel report : reports) {
                    reportsContainer.addView(createReportView(report));
                }
            }
            setEmptySting(reports == null || reports.isEmpty());
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private View createReportView(ReportModel elem) {
        @SuppressLint("InflateParams") View reportLayout = LayoutInflater.from(requireContext()).inflate(R.layout.report_layout, null);
        TextView areaView = reportLayout.findViewById(R.id.areaView);
        TextView dateView = reportLayout.findViewById(R.id.dateView);

        areaView.setText(elem.getArea());
        dateView.setText(elem.getDate());

        // Convert 16dp to pixels
        int marginInDp = 16; // Margin in dp
        float scale = requireContext().getResources().getDisplayMetrics().density; // Get the scale factor for converting dp to pixels
        int marginInPx = (int) (marginInDp * scale + 0.5f); // Convert dp to pixels

        // Create LayoutParams with margins
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // Width
                LinearLayout.LayoutParams.WRAP_CONTENT  // Height
        );

        // Set margins (left, top, right, bottom)
        layoutParams.setMargins(0, 0, 0, marginInPx); // Set only bottom margin

        // Apply the LayoutParams to the reportLayout
        reportLayout.setLayoutParams(layoutParams);

        // ----- Listener -----
        reportLayout.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddReportActivity.class);
            intent.putExtra("reportToEdit", elem);
            getContext().startActivity(intent);
        });

        return reportLayout;
    }

    private void setEmptySting (boolean val) {
        if (val) {
            binding.reportsContainer.setVisibility(View.GONE);
            binding.emptyReports.setVisibility(View.VISIBLE);
        }
        else {
            binding.reportsContainer.setVisibility(View.VISIBLE);
            binding.emptyReports.setVisibility(View.GONE);
        }
    }

}
