package com.githubuser.xavier.githubuser;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.githubuser.xavier.githubuser.databinding.ActivityUserdetailBinding;
import com.githubuser.xavier.githubuser.networking.GitHubService;
import com.githubuser.xavier.githubuser.object.UserObject;

import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {
    public static final String LOGIN_KEY = "LOGIN_KEY";

    private ActivityUserdetailBinding   mUserDetailBinding;

    private UserObject                  mUser = new UserObject();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String loginName = getIntent().getStringExtra(LOGIN_KEY);

        //region Bind xml to view
        mUserDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_userdetail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //endregion

        //region API call
        /** Need to set permit if were to call API in the main thread */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        GitHubService apiService = RetrofitClient.getClient().create(GitHubService.class);

        try {
            Response<UserObject> response = apiService.userDetail(loginName).execute();

            if (response.isSuccessful() && response.body() != null) {
                mUser = response.body();

                notifyDataChanged();

            } else {
                Toast.makeText(this, "API call Fail", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //endregion
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void notifyDataChanged() {
        //region Display User image
        Glide.with(this)
                .load(mUser.avatar_url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(mUserDetailBinding.avatar);
        //endregion

        //region Display Name
        if (mUser.name != null) {
            mUserDetailBinding.name.setText(mUser.name);
        } else {
            mUserDetailBinding.name.setVisibility(View.GONE);
        }
        //endregion

        //region Display Bio
        if (mUser.bio != null) {
            mUserDetailBinding.bio.setText(mUser.bio);
        } else {
            mUserDetailBinding.bio.setVisibility(View.GONE);
        }
        //endregion

        //region Display staff label
        mUserDetailBinding.label.getRoot().setVisibility(mUser.site_admin ? View.VISIBLE : View.GONE);
        //endregion

        //region Display Login Name
        if (mUser.login != null) {
            mUserDetailBinding.login.setText(mUser.login);
        } else {
            mUserDetailBinding.profileLayout.setVisibility(View.GONE);
        }
        //endregion

        //region Display Location
        if (mUser.location != null) {
            mUserDetailBinding.location.setText(mUser.location);
        } else {
            mUserDetailBinding.locationLayout.setVisibility(View.GONE);
        }
        //endregion

        //region Display Link
        if (mUser.blog != null) {
            mUserDetailBinding.link.setText(mUser.blog);
        } else {
            mUserDetailBinding.linkLayout.setVisibility(View.GONE);
        }
        //endregion
    }
}
