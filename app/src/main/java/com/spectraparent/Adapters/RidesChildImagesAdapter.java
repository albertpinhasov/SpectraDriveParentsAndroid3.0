package com.spectraparent.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spectraparent.Helpers.CircleTransform;
import com.spectraparent.Models.ChildModel;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RidesChildImagesAdapter extends RecyclerView.Adapter<RidesChildImagesAdapter.Profile_ViewHolder> {
    Context mContext;
    ArrayList<ChildModel> mArrayList;


    public RidesChildImagesAdapter(Context context, List<ChildModel> childModel) {
        mContext = context;
        mArrayList = new ArrayList<>();
        this.mArrayList = (ArrayList<ChildModel>) childModel;
    }

    @Override
    public Profile_ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rides_images, viewGroup, false);
        return new Profile_ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(Profile_ViewHolder profile_viewHolder, int i) {
        if (mArrayList.get(i).getChild().getImages() != null &&
                mArrayList.get(i).getChild().getImages().size() > 0 &&
                mArrayList.get(i).getChild().getImages().get(0).getSmallPhotoUrl() != null) {
            Picasso.get().load(mArrayList.get(i).getChild().getImages().get(0).getSmallPhotoUrl()).
                    placeholder(R.drawable.no_profile)
                    .transform(new CircleTransform()).fit().centerCrop().into(profile_viewHolder.imgChild1)
            ;
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class Profile_ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgChild1;

        public Profile_ViewHolder(View itemView) {
            super(itemView);
            imgChild1 = itemView.findViewById(R.id.imgChild1);
        }
    }
}
