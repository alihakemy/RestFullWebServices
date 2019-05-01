
package com.hakemy.linkedin_webservices.MyJsonClass;

import android.os.Parcel;
import android.os.Parcelable;

public class JsonFromPojo implements Parcelable {

    // parse json to Gson this google library add from -project structure->
    private String itemName;
    private String category;
    private String description;
    private int sort;
    private double price; // refactor typeMigration
    private String image;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeInt(this.sort);
        dest.writeDouble(this.price);
        dest.writeString(this.image);
    }

    public JsonFromPojo() {
    }

    protected JsonFromPojo(Parcel in) {
        this.itemName = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.sort = in.readInt();
        this.price = in.readDouble();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<JsonFromPojo> CREATOR = new Parcelable.Creator<JsonFromPojo>() {
        @Override
        public JsonFromPojo createFromParcel(Parcel source) {
            return new JsonFromPojo(source);
        }

        @Override
        public JsonFromPojo[] newArray(int size) {
            return new JsonFromPojo[size];
        }
    };
}