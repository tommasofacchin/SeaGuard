package com.seaguard.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.seaguard.R;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.ActivityReportBinding;
import com.seaguard.databinding.CommentLayoutBinding;

public class ReportActivity extends AppCompatActivity {
    private ReportModel currentReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityReportBinding binding = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Toolbar
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);

        // Back in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Load the selected Report
        currentReport = getIntent().getParcelableExtra("report");

        // Location
        binding.locationName.setText(currentReport.getArea());

        // Time and Date
        binding.time.setText(currentReport.getTime());
        binding.date.setText(currentReport.getDate());

        // Urgency
        binding.urgency.setText(getString(R.string.urgency_format, "" + currentReport.getUrgency()));

        // Description
        binding.description.setText(currentReport.getDescription());

        // Category
        binding.category.setText(currentReport.getCategory());

        // Comments
        LinearLayout commentsContainer = binding.commentsContainer;
        commentsContainer.addView(createCommentView());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private View createCommentView () {
        View commentLayout = LayoutInflater.from(this).inflate(R.layout.comment_layout, null);

        TextView username = commentLayout.findViewById(R.id.comment_username);
        TextView utility = commentLayout.findViewById(R.id.comment_utility);
        TextView content = commentLayout.findViewById(R.id.comment_content);

        username.setText("Username");
        utility.setText("5");
        content.setText("This is a comment.");

        return commentLayout;
    }


}