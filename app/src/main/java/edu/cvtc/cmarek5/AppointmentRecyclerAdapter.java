package edu.cvtc.cmarek5;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AppointmentRecyclerAdapter extends RecyclerView.Adapter<AppointmentRecyclerAdapter.ViewHolder> {

    // Member variables
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private Cursor mCursor;
    private int mAppNamePosition;
    private int mAppDatePosition;
    private int mIdPosition;

    public AppointmentRecyclerAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);

        // Used to get the positions of the columns we are interested in
        populateColumnPositions();

    }

    private void populateColumnPositions() {
        if (mCursor != null) {
            // Get column indexes from mCursor
            mAppNamePosition = mCursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_FNAME);
            mAppDatePosition = mCursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry.COLUMN_APPOINTMENT_DATE);
            mIdPosition = mCursor.getColumnIndex(AppointmentsDatabaseContract.AppointmentInfoEntry._ID);
        }
    }

    public void changeCursor(Cursor cursor) {
        // If cursor is open, close it
        if (mCursor != null) {
            mCursor.close();
        }

        // Create a new cursor based upon the object passed in
        mCursor = cursor;

        // Get the positions of the columns in your cursor
        populateColumnPositions();

        // Tell the activity that the data set has changed
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = mLayoutInflater.inflate(R.layout.item_list, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Move the cursor to the correct row
        mCursor.moveToPosition(position);

        // Get the actual values
        String appFirstName = mCursor.getString(mAppNamePosition);
        String appDate = mCursor.getString(mAppDatePosition);
        int id = mCursor.getInt(mIdPosition);

        // Pass the information into the holder
        holder.mAppFirstName.setText(appFirstName);
        holder.mAppDate.setText(appDate);
        holder.mId = id;

    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Member Variables
        public final TextView mAppFirstName;
        public final TextView mAppDate;
        public int mId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the Constructors by Setting their TextView
            mAppFirstName = (TextView)itemView.findViewById(R.id.patient_name);
            mAppDate = (TextView)itemView.findViewById(R.id.patient_appointment);

            itemView.setOnClickListener(new View.OnClickListener() {

                public void onClick (View view) {
                    Intent intent = new Intent(mContext, AppointmentActivity.class);
                    intent.putExtra(AppointmentActivity.APPOINTMENT_ID, mId);
                    mContext.startActivity(intent);
                }
            });
        }

    }

}
