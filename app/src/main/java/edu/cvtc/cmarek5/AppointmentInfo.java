package edu.cvtc.cmarek5;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentInfo implements Parcelable {

    // Member Attributes
    private String mName;
    private String mDate;
    private int mId;

    protected AppointmentInfo(Parcel parcel) {
        mName = parcel.readString();
        mDate = parcel.readString();
    }

    public static final Creator<AppointmentInfo> CREATOR = new Creator<AppointmentInfo>() {
        @Override
        public AppointmentInfo createFromParcel(Parcel parcel) {
            return new AppointmentInfo(parcel);
        }

        @Override
        public AppointmentInfo[] newArray(int size) {
            return new AppointmentInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(mName);
        parcel.writeString(mDate);
    }

    // Overloaded Constructors
    public AppointmentInfo(String name, String date) {
        mName = name;
        mDate = date;
    }

    public AppointmentInfo(int id, String name, String date) {
        mId = id;
        mName = name;
        mDate = date;
    }

    // Getters and Setters
    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    private String getCompareKey() {
        return mName + "|" + mDate;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentInfo that = (AppointmentInfo) o;
        return getCompareKey().equals(that.getCompareKey());
    }

    public int hashCode() {
        return getCompareKey().hashCode();
    }

    public String toString() {
        return getCompareKey();
    }

}
