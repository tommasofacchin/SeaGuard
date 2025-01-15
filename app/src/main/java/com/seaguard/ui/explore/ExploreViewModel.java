package com.seaguard.ui.explore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.seaguard.database.ArticleModel;
import com.seaguard.database.DbHelper;
import com.seaguard.database.ReportModel;

import java.util.List;

public class ExploreViewModel extends ViewModel {
    private final MutableLiveData<List<ArticleModel>> articles;

    public ExploreViewModel() {
        articles = new MutableLiveData<>();
        loadArticles();
    }

    public LiveData<List<ArticleModel>> getArticles() {
        return articles;
    }

    private void loadArticles () {
        DbHelper.getArticles(
                articles::setValue,
                e -> {}
        );
    }
}