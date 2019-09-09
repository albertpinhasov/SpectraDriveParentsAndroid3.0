package com.spectraparent.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spectraparent.Helpers.LocalStorage;
import com.spectraparent.Interface.AdapterClickListerner;
import com.spectraparent.Models.Child;
import com.spectraparent.Models.Images;
import com.spectraparent.android.R;

import java.util.ArrayList;

public class ProfileChildAdapter extends RecyclerView.Adapter<ProfileChildAdapter.Profile_ViewHolder> {
    Context mContext;
    ArrayList<Child> mArrayList;
    AdapterClickListerner profileFragment;

    public ProfileChildAdapter(Context context, AdapterClickListerner profileFragment) {
        mContext = context;
        mArrayList = new ArrayList<>();
        this.profileFragment = profileFragment;
        if (LocalStorage.getStudent().getChild() != null) {
            mArrayList = LocalStorage.getStudent().getChild();
        }
    }

    @NonNull
    @Override
    public Profile_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_profile_child, viewGroup, false);
        return new Profile_ViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull Profile_ViewHolder profile_viewHolder, final int i) {
        profile_viewHolder.bindData(mArrayList.get(i));
        profile_viewHolder.mBtnEditNeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragment.onClickItem(1, mArrayList.get(i));
            }
        });
        profile_viewHolder.mBtnEditAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileFragment.onClickItem(2,mArrayList.get(i));

            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class Profile_ViewHolder extends RecyclerView.ViewHolder {
        private final RecyclerView rcImages, rcNeeds;
        TextView mFirstName, mLastName, mAbout,txtChildName;
        Button mBtnEditNeeds, mBtnEditAbout;
        ImageView imageView;

        public Profile_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rcChild);
            mFirstName = itemView.findViewById(R.id.txtFirstName);
            mLastName = itemView.findViewById(R.id.txtLastName);
            mBtnEditNeeds = itemView.findViewById(R.id.btnEditNeeds);
            mBtnEditAbout = itemView.findViewById(R.id.btnEditInfo);
            rcImages = itemView.findViewById(R.id.rcImages);
            rcNeeds = itemView.findViewById(R.id.rcNeeds);
            txtChildName = itemView.findViewById(R.id.txtChildName);

            mAbout = itemView.findViewById(R.id.txtAbout);
        }

        public void bindData(Child childModel) {
            if (childModel.getImages() != null && childModel.getImages().size() > 0) {
                rcImages.setAdapter(new ChildImagesAdapter(itemView.getContext(), (ArrayList<Images>) childModel.getImages()));
            }
            mFirstName.setText(childModel.getFirstName());
            txtChildName.setText(childModel.getFirstName() + "'s" + " " + "information");
            mLastName.setText(childModel.getLastName());
            mAbout.setText(childModel.getAbout());
            rcNeeds.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayout.VERTICAL, false));
            rcNeeds.setAdapter(new ChildNeedsAdapter(itemView.getContext(), childModel));
        }
    }

}
