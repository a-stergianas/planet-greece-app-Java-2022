package com.example.planetgreece.fragment.Main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.planetgreece.BrowserActivity;
import com.example.planetgreece.MainActivity;
import com.example.planetgreece.RecyclerViewInterface;
import com.example.planetgreece.db.DatabaseHelper;
import com.example.planetgreece.db.model.Article;
import com.example.planetgreece.R;
import com.example.planetgreece.db.model.User;
import com.example.planetgreece.adapter.ArticleAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RecyclerViewInterface {
    private DatabaseHelper db;

    private static final String ARG_USER = "param1";

    private User mUser;

    private ArrayList<Article> articleList;
    private RecyclerView recyclerView;

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

        db = DatabaseHelper.getInstance(getContext());

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rvArticles);

        // Get articles from database
        articleList = (ArrayList<Article>) db.getArticles();

        // Display articles in recycler view
        setArticleAdapter();

        return view;
    }

    private void setArticleAdapter() {
        ArticleAdapter adapter = new ArticleAdapter(mUser.getId(), articleList, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent (getActivity(), BrowserActivity.class);
        intent.putExtra("LINK", articleList.get(position).getLink());
        startActivity(intent);
    }
}