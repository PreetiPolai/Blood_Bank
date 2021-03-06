package com.android.BloodBank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.BloodBank.Model.UserProfileData;
import com.android.BloodBank.OtherUserProfileActivity;
import com.android.BloodBank.R;
import com.android.BloodBank.UI.Messaging.MessagingActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class SearchItemAdapter  extends RecyclerView.Adapter<SearchItemAdapter.SearchViewHolder> implements Filterable {

    
    private List<UserProfileData> users;
    private Context context;
    private  List<UserProfileData> backup;

    public SearchItemAdapter(List<UserProfileData> musers, Context context) {
        this.users = musers;
        this.context = context;
        backup = new ArrayList<>(musers);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_searchitem, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.userName.setText(users.get(position).getUserName());

        String address =  users.get(position).getUserAddress().get("City") + ", " + users.get(position).getUserAddress().get("District") + ", " + users.get(position).getUserAddress().get("State");

        holder.address.setText(address);
        holder.bloodGroup.setText(users.get(position).getUserBloodGroup());

        Glide.with(context)
                .asBitmap()
                .load(users.get(position).getUserImage())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OtherUserProfileActivity.class);
                intent.putExtra("selectedUserId",users.get(position).getUserId());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {

            ArrayList<UserProfileData> filteredData = new ArrayList<>();
            if (keyword.toString().isEmpty())
                filteredData.addAll(backup);
            else{
                for (UserProfileData obj : backup){
                    if(obj.getUserBloodGroup().toString().toLowerCase().contains(keyword.toString().toLowerCase())){
                        filteredData.add(obj);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                users.clear();
                users.addAll((ArrayList<UserProfileData>)filterResults.values);
                notifyDataSetChanged();
        }
    };





    class SearchViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView bloodGroup,address,userName;

        public SearchViewHolder(@NonNull View itemView)  {
            super(itemView);

            imageView = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.itemUserName);
            address = itemView.findViewById(R.id.itemAddress);
            bloodGroup = itemView.findViewById(R.id.itemBloodGroup);


        }
    }
}
