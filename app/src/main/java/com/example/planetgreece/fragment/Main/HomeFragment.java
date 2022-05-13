package com.example.planetgreece.fragment.Main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planetgreece.Article;
import com.example.planetgreece.R;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.fragment.ArticleAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ArrayList<Article> articleList;
    private RecyclerView recyclerView;

    private static final String ARG_USER = "param1";

    private User mUser;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user User object.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(User user) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        articleList = new ArrayList<>();

//        if (savedInstanceState != null)
//            return;

        if (getArguments() != null) {
//            param2 = getArguments().getString(ARG_PARAM2);
            mUser = (User) getArguments().getSerializable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rvArticles);

        setArticlesInfo();
        setArticleAdapter();

        return view;
    }

    private void setArticlesInfo() {
        articleList.add(new Article());
        articleList.add(new Article());
        articleList.add(new Article());
        articleList.add(new Article());
        articleList.add(new Article());
    }

    private void setArticleAdapter() {
        ArticleAdapter adapter = new ArticleAdapter(articleList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}