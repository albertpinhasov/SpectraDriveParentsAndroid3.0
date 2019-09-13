package com.spectraparent.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spectraparent.Helpers.CircleTransform;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.ChildModel;
import com.spectraparent.Models.Images;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChildImagesAdapter extends RecyclerView.Adapter<   ChildImagesAdapter.Profile_ViewHolder> {
    Context mContext;
    ArrayList<Images> imageList;


    public ChildImagesAdapter(Context context, ArrayList<Images> imageList) {
        mContext = context;
        this.imageList = imageList;

    }

    @NonNull
    @Override
    public Profile_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_add_image, viewGroup, false);
        return new Profile_ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull Profile_ViewHolder profile_viewHolder, int i) {
        Picasso.get().load(imageList.get(i).getSmallPhotoUrl()).
                placeholder(R.drawable.no_profile)
                .transform(new CircleTransform()).fit().centerCrop().into(profile_viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class Profile_ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public Profile_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rcChild);
        }
    }
}
