//Author: Okemdi Udeh
//Student ID: S1903344


package org.me.gcu.equakestartercode;

//Import the necessary libraries
import android.os.Parcel;
import android.os.Parcelable;

//Definition of Item class
public class Item implements Parcelable {
    //Declare variables
    int id;
    String title, description, location, link, pubDate, category;
    double depth, magnitude, latitude, longitude;

    public Item() {
    }

    protected Item(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        location = in.readString();
        link = in.readString();
        pubDate = in.readString();
        category = in.readString();
        depth = in.readDouble();
        magnitude = in.readDouble();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(location);
        dest.writeString(link);
        dest.writeString(pubDate);
        dest.writeString(category);
        dest.writeDouble(depth);
        dest.writeDouble(magnitude);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    //Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", link='" + link + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", category='" + category + '\'' +
                ", depth=" + depth +
                ", magnitude=" + magnitude +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
