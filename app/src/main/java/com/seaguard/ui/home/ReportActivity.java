package com.seaguard.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ListenerRegistration;
import com.seaguard.R;
import com.seaguard.database.CommentModel;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.ActivityReportBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private ReportModel currentReport;
    private MutableLiveData<List<CommentModel>> comments;
    private TextView commentField;
    private int rating;
    private ArrayList<ImageView> stars;
    private ListenerRegistration listenerRef;

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


        // Post your comment
        commentField = binding.commentField;
        rating = 0;

        stars = new ArrayList<>(Arrays.asList(
                binding.star1,
                binding.star2,
                binding.star3,
                binding.star4,
                binding.star5
        ));

        for(int i = 0; i < stars.size(); i++) {
            int current = i;
            stars.get(i).setOnClickListener(v -> {
                rating = current + 1;
                for(int j = 0; j < stars.size(); j++) {
                    if(j <= current) stars.get(j).setColorFilter(Color.BLUE);
                    else stars.get(j).clearColorFilter();
                }
            });
        }

        binding.postComment.setOnClickListener(v -> postComment());

        // Other Comments
        comments = new MutableLiveData<>();
        LinearLayout commentsContainer = binding.commentsContainer;

        loadComments();
        comments.observe(this, list -> {
            if (list != null) {
                commentsContainer.removeAllViews();
                for (CommentModel elem : list)
                    commentsContainer.addView(createCommentView(elem));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRef != null) listenerRef.remove();
    }

    private void postComment () {
        String content = commentField.getText().toString();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Timestamp timestamp = Timestamp.now();

        if (currentUser != null && !content.isEmpty() && rating != 0) {
            DbHelper.add(
                    new CommentModel(
                            currentReport.getId(),
                            currentUser.getUid(),
                            currentUser.getDisplayName(),
                            rating,
                            content,
                            timestamp
                    ),
                    docRef -> {
                        Toast.makeText(
                            this,
                            getString(R.string.comment_saved),
                            Toast.LENGTH_SHORT
                        ).show();
                        // Clean text field and stars
                        clearCommentFields();
                    },
                    e -> Toast.makeText(
                        this,
                        getString(R.string.error_while_saving),
                        Toast.LENGTH_SHORT
                    ).show()
            );
        }
        else Toast.makeText(
            this,
            getString(R.string.incomplete_fields),
            Toast.LENGTH_SHORT
        ).show();
    }

    private void loadComments () {
        listenerRef = DbHelper.getComments(
                currentReport.getId(),
                comments::setValue,
                e ->  Toast.makeText(
                    this,
                    getString(R.string.loading_error),
                    Toast.LENGTH_SHORT
                ).show()
        );
    }

    private void clearCommentFields () {
        commentField.setText("");
        rating = 0;
        for (ImageView elem : stars) elem.clearColorFilter();
    }

    private View createCommentView (CommentModel elem) {
        View commentLayout = LayoutInflater.from(this).inflate(R.layout.comment_layout, null);

        TextView username = commentLayout.findViewById(R.id.comment_username);
        TextView rating = commentLayout.findViewById(R.id.comment_rating);
        TextView timeAndDate = commentLayout.findViewById(R.id.comment_timeAndDate);
        TextView content = commentLayout.findViewById(R.id.comment_content);

        username.setText(elem.getUsername());
        rating.setText(String.valueOf(elem.getRating()));
        timeAndDate.setText(getString(R.string.timeAndDate_format, elem.getDate(), elem.getTime()));
        content.setText(elem.getContent());

        return commentLayout;
    }


}