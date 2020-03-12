package com.spectraparent.Fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spectraparent.Adapters.ChildImagesAdapter;
import com.spectraparent.Adapters.RidesChildImagesAdapter;
import com.spectraparent.Fragments.RidesFragment.OnListFragmentInteractionListener;
import com.spectraparent.Helpers.CircleTransform;
import com.spectraparent.Helpers.Tools;
import com.spectraparent.Models.RideModel;
import com.spectraparent.android.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MyRidesRecyclerViewAdapter extends RecyclerView.Adapter<MyRidesRecyclerViewAdapter.ViewHolder> {

    private List<RideModel> mValues;
    private final OnListFragmentInteractionListener mListener;
    int type = 0;

    public MyRidesRecyclerViewAdapter(List<RideModel> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_rides, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.bindData();
        if (type == 1) {
            holder.mBtnTrack.setVisibility(View.VISIBLE);
            holder.mBtnTrack.setText("Track on the map");
//            if (holder.mItem.getRideStatus() != 0) {
//                holder.mBtnTrack.setVisibility(View.VISIBLE);
//                holder.mBtnTrack.setText("Track on the map");
//            } else {
//                holder.mBtnTrack.setVisibility(View.GONE);
//            }

        } else if (type == 2) {
            holder.mBtnTrack.setVisibility(View.GONE);
        } else {
            holder.mBtnTrack.setVisibility(View.VISIBLE);
            holder.mBtnTrack.setText("Request cancellation");
        }
        holder.mBtnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem, type);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateItems(ArrayList<RideModel> rides, int type) {
        mValues = rides;
        this.type = type;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView, mSep1, mSep2;
        public LinearLayout mViewPickedUp, mViewOnTheWay, mViewDroppedOff;
        public TextView mRideId, mCreatedOn, txtPickup, txtDrop;
        public RecyclerView rvImages;
        public Button mBtnTrack;
        public RideModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSep1 = view.findViewById(R.id.vSep1);
            mSep2 = view.findViewById(R.id.vSep2);

            mViewPickedUp = view.findViewById(R.id.vPickedUp);
            mViewOnTheWay = view.findViewById(R.id.vOnTheWay);
            mViewDroppedOff = view.findViewById(R.id.vDroppedOff);

            mRideId = view.findViewById(R.id.txtRideId);
            mCreatedOn = view.findViewById(R.id.txtCreatedOn);
            txtPickup = view.findViewById(R.id.txtPickup);
            txtDrop = view.findViewById(R.id.txtDrop);

            rvImages = view.findViewById(R.id.rvImages);
            mBtnTrack = view.findViewById(R.id.btnTrack);

        }

        public void bindData() {
            mRideId.setText(mItem.getRideName());
            if (type == 1) {
                if (mItem.getRideStatus() == 3) {
                    mCreatedOn.setVisibility(View.VISIBLE);
                    mCreatedOn.setText("Picked up at "+Tools.getFormattedDate(itemView.getContext(), (mItem.getChild().get(0).getPickedupTime().getTime())));

                } else {
                    mCreatedOn.setVisibility(View.GONE);
                }
            } else {
                mCreatedOn.setVisibility(View.VISIBLE);
                mCreatedOn.setText(Tools.getFormattedDate(itemView.getContext(), (mItem.getSchendual()).getTime()));
            }
            try {
                txtPickup.setText(mItem.getChildModel().get(0).getPickup().getName());
                txtDrop.setText(mItem.getChildModel().get(0).getDrop().getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            rvImages.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayout.HORIZONTAL, false));
            if (mItem.getChildModel() != null && mItem.getChildModel().size() > 0
                    && mItem.getChildModel().get(0).getChild().getImages().size() > 0
                    && mItem.getChildModel().get(0).getChild().getImages().get(0).getSmallPhotoUrl() != null)
                rvImages.setAdapter(new RidesChildImagesAdapter(itemView.getContext(), mItem.getChildModel()));
            else
                rvImages.setVisibility(View.GONE);
            processEvents();
        }

        void processEvents() {
//            if (mItem.getChildModel().get(0).getEvents() == null) return;
//
//            markNotStarted();
//
//            if (mItem.getChildModel().get(0).getEvents().size() == 1) {
//                markPickedUp();
//            } else if (mItem.getChildModel().get(0).getEvents().size() == 2) {
//                markOnTheWay();
//            } else if (mItem.getChildModel().get(0).getEvents().size() == 3) {
//                markDroppedOff();
//            }
//            if (type == 1) {
//                markOnTheWay();
//            } else if (type == 2) {
//                markNotStarted();
//            } else {
//                markNotStarted();
//            }
            if (mItem.getRideStatus() == 0 || mItem.getRideStatus() == 1 || mItem.getRideStatus() == 2) {
                mViewOnTheWay.setAlpha((float) 0.6);
                mViewPickedUp.setAlpha((float) 0.6);
                mViewDroppedOff.setAlpha((float) 0.6);

                mSep1.setAlpha((float) 0.6);
                mSep2.setAlpha((float) 0.6);
            } else if (mItem.getRideStatus() == 3) {
                mViewOnTheWay.setAlpha(1);
                mSep1.setAlpha(1);
                mViewPickedUp.setAlpha(1);
                mSep2.setAlpha((float) 0.6);
                mViewDroppedOff.setAlpha((float) 0.6);
            } else if (mItem.getRideStatus() == 4) {
                mViewOnTheWay.setAlpha(1);
                mSep1.setAlpha(1);
                mViewPickedUp.setAlpha(1);
                mViewDroppedOff.setAlpha(1);
                mSep2.setAlpha(1);
            }

        }
    }
}
