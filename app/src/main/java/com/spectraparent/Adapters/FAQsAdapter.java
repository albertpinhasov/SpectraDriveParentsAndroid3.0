package com.spectraparent.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.spectraparent.Activities.HelpFAQ.FAQActivity;
import com.spectraparent.Models.FAQModel;
import com.spectraparent.android.R;

import java.util.ArrayList;

public class FAQsAdapter extends RecyclerView.Adapter<FAQsAdapter.Profile_ViewHolder> {
    Context  mContext;
    ArrayList<FAQModel> mArrayList;

    public FAQsAdapter(Context context) {
        mContext = context;
        mArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FAQsAdapter.Profile_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cell_faq, viewGroup, false);
        return new FAQsAdapter.Profile_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQsAdapter.Profile_ViewHolder vh, final int i) {
vh.mTitle.setText(mArrayList.get(i).getQuestion());

vh.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, FAQActivity.class);
        intent.putExtra("json", new Gson().toJson(mArrayList.get(i)));
        mContext.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void updateItems(ArrayList<FAQModel> faqs) {
        mArrayList = faqs;
        notifyDataSetChanged();
    }

    class Profile_ViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        public Profile_ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle= itemView.findViewById(R.id.txtQuestion);
        }
    }
}
