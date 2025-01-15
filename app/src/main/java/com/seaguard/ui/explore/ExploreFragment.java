package com.seaguard.ui.explore;

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
import com.seaguard.database.ArticleModel;
import com.seaguard.database.ReportModel;
import com.seaguard.databinding.FragmentExploreBinding;
import com.seaguard.ui.home.AddReportActivity;

public class ExploreFragment extends Fragment {

    private FragmentExploreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ExploreViewModel exploreViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);

        binding = FragmentExploreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout articlesContainer = binding.articlesContainer;

        exploreViewModel.getArticles().observe(getViewLifecycleOwner(), articles -> {
            if (articles != null) {
                articlesContainer.removeAllViews();
                for(ArticleModel article : articles) {
                    articlesContainer.addView(createArticleView(article));
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


    private View createArticleView(ArticleModel elem) {
        @SuppressLint("InflateParams") View articleLayout = LayoutInflater.from(requireContext()).inflate(R.layout.article_layout, null);
        TextView articleTitle = articleLayout.findViewById(R.id.articleTitle);
        TextView articleDescription = articleLayout.findViewById(R.id.articleDescription);
        TextView articleDate = articleLayout.findViewById(R.id.articleDate);

        articleTitle.setText(elem.getTitle());
        articleDescription.setText(elem.getDescription());
        articleDate.setText(elem.getDate() + " - " + elem.getTime());

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
        articleLayout.setLayoutParams(layoutParams);

        // ----- Listener -----
        articleLayout.setOnClickListener(v -> {
            /*Intent intent = new Intent(getContext(), AddReportActivity.class);
            intent.putExtra("reportToEdit", elem);
            getContext().startActivity(intent);*/
        });

        return articleLayout;
    }
}