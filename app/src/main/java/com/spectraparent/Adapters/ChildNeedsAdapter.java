package com.spectraparent.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spectraparent.Models.Child;
import com.spectraparent.Models.ChildModel;
import com.spectraparent.android.R;

import java.util.ArrayList;

class ChildNeedsAdapter extends RecyclerView.Adapter<ChildNeedsAdapter.Profile_ViewHolder> {
    Context  mContext;
    ArrayList<String> mArrayList;

    public ChildNeedsAdapter(Context context, Child childModel) {
        mContext = context;
        mArrayList = new ArrayList<>();

        if( childModel.getSpecialNeeds()!=null &&  childModel.getSpecialNeeds().trim().length() > 0){
            String[] needs =  childModel.getSpecialNeeds().split(",");
            for(String need : needs){
                if(need.trim().length() >0)
                    mArrayList.add(need);
            }
        }

        if( childModel.getOtherSpecialNeeds()!=null &&  childModel.getOtherSpecialNeeds().trim().length() > 0){
            String[] needs =  childModel.getOtherSpecialNeeds().split(",");
            for(String need : needs){
                if(need.trim().length() >0)
                    mArrayList.add(need);
            }
        }
    }

    @NonNull
    @Override
    public ChildNeedsAdapter.Profile_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_profile_child_special_needs, viewGroup, false);
        return new ChildNeedsAdapter.Profile_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildNeedsAdapter.Profile_ViewHolder vh, int i) {
vh.mTitle.setText(mArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class Profile_ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        public Profile_ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle= itemView.findViewById(R.id.txtTitle);
        }
    }
}
