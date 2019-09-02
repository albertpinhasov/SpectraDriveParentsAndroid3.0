package com.spectraparent.Fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        holder.mBtnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateItems(ArrayList<RideModel> rides) {
        mValues = rides;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView, mSep1, mSep2;
        public LinearLayout mViewPickedUp, mViewOnTheWay, mViewDroppedOff;
        public TextView mRideId, mCreatedOn, mAddress;
        public ImageView mImgChild1;
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
            mAddress = view.findViewById(R.id.txtAddress);

            mImgChild1 = view.findViewById(R.id.imgChild1);
            mBtnTrack = view.findViewById(R.id.btnTrack);

        }

        public void bindData() {
            mRideId.setText(mItem.getRideName());
            mCreatedOn.setText(Tools.getFormattedDate(itemView.getContext(),(mItem.getCreatedOn()).getTime()));
            try {
                mAddress.setText(mItem.getChildModel().get(0).getPickup() + "->" + mItem.getChildModel().get(0).getDrop());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mItem.getChildModel() != null && mItem.getChildModel().size() > 0 && mItem.getChildModel().get(0).getChild().getImages() != null &&
                    mItem.getChildModel().get(0).getChild().getImages().size() > 0 && mItem.getChildModel().get(0).getChild().getImages().get(0).getSmallPhotoUrl() != null) {
                Picasso.get().load(mItem.getChildModel().get(0).getChild().getImages().get(0).getSmallPhotoUrl().replace("https://snappill.app", "https://spectradrivewebapi.azurewebsites.net"))
                        .transform(new CircleTransform()).fit().centerCrop().into(mImgChild1);
            }

            processEvents();
        }

        void processEvents() {
            if (mItem.getChildModel().get(0).getEvents() == null) return;

            markNotStarted();

            if (mItem.getChildModel().get(0).getEvents().size() == 1) {
                markPickedUp();
            } else if (mItem.getChildModel().get(0).getEvents().size() == 2) {
                markOnTheWay();
            } else if (mItem.getChildModel().get(0).getEvents().size() == 3) {
                markDroppedOff();
            }
        }

        void markNotStarted() {
            mViewOnTheWay.setAlpha((float) 0.6);
            mViewPickedUp.setAlpha((float) 0.6);
            mViewDroppedOff.setAlpha((float) 0.6);

            mSep1.setAlpha((float) 0.6);
            mSep2.setAlpha((float) 0.6);
        }

        void markPickedUp() {
            mViewPickedUp.setAlpha(1);
        }

        void markOnTheWay() {
            markPickedUp();
            mViewOnTheWay.setAlpha(1);
            mSep1.setAlpha(1);
        }

        void markDroppedOff() {
            markPickedUp();
            markOnTheWay();
            mViewDroppedOff.setAlpha(1);
            mSep2.setAlpha(1);
        }
    }
}
