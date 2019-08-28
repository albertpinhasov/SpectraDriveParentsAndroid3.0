package com.spectradrive.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.spectradrive.Models.ChildModel;
import com.spectradrive.android.R;

import java.util.ArrayList;

public class ChildImagesAdapter extends RecyclerView.Adapter<ChildImagesAdapter.Profile_ViewHolder> {
    Context  mContext;
    ArrayList<String> mArrayList;


    public ChildImagesAdapter(Context context, ChildModel childModel) {
        mContext = context;
        mArrayList = new ArrayList<>();
mArrayList.add("");
mArrayList.add("");

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

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class Profile_ViewHolder extends RecyclerView.ViewHolder{
ImageView imageView;
        public Profile_ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.rcChild);
        }
    }
}
