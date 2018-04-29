package com.githubuser.xavier.githubuser;

import android.databinding.DataBindingUtil;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.githubuser.xavier.githubuser.databinding.ActivityMainBinding;
import com.githubuser.xavier.githubuser.networking.GitHubService;
import com.githubuser.xavier.githubuser.object.UserObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private UserAdapter         mUserAdapter;
    private RecyclerView        mRecyclerView;

    private int                 mUserId = 0;
    private List<UserObject>    mUserList = new ArrayList<>();

    private GitHubService       mGetUsersApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //region Setup UI
        mRecyclerView = mainBinding.recyclerview;
        //endregion

        //region Setup Adapter
        setupAdapter();
        //endregion

        //region API call
        /** Need to set permit if were to call API in the main thread */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mGetUsersApi = RetrofitClient.getClient().create(GitHubService.class);

        getNewUsers();
        //endregion
    }

    private void setupAdapter() {
        mUserAdapter = new UserAdapter(this, mUserList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mUserAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
                    //region Pagination
                    Toast.makeText(MainActivity.this, "Loading More", Toast.LENGTH_SHORT).show();

                    getNewUsers();
                    //endregion
                }
            }
        });
    }

    private void getNewUsers() {
        try {
            Response<List<UserObject>> response = mGetUsersApi.users(mUserId, 20).execute();

            if (response.isSuccessful() && response.body() != null) {
                mUserList.addAll(response.body());
                mUserAdapter.notifyDataSetChanged();

                //region Set the last user id for paginatino
                mUserId = mUserList.get(mUserList.size() - 1).id;
                //endregion

            } else {
                Toast.makeText(this, "API call Fail", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
