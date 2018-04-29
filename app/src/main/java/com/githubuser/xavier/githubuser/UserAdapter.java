package com.githubuser.xavier.githubuser;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.githubuser.xavier.githubuser.databinding.ItemUserBinding;
import com.githubuser.xavier.githubuser.object.UserObject;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context             mContext;
    private List<UserObject>    mUserList;

    private RequestManager      mGlide;

    public UserAdapter(Context context, List<UserObject> list) {
        mContext = context;
        mUserList = list;

        mGlide = Glide.with(mContext);
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemUserBinding binding = ItemUserBinding.inflate(inflater, parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final UserObject user = mUserList.get(position);
        holder.bind(user);

        //region Display User image
        mGlide.load(user.avatar_url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(holder.binding.avatar);
        //endregion

        //region Display staff label
        holder.binding.label.getRoot().setVisibility(user.site_admin ? View.VISIBLE : View.GONE);
        //endregion

        //region Click event that brings to User Detail
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserDetailActivity.class);
                intent.putExtra(UserDetailActivity.LOGIN_KEY, user.login);
                mContext.startActivity(intent);
            }
        });
        //endregion
    }

    @Override
    public int getItemCount() {
        return mUserList != null ? mUserList.size() : 0;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ItemUserBinding binding;

        UserViewHolder(ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(UserObject user) {
            binding.setObj(user);
            binding.executePendingBindings();
        }
    }
}
