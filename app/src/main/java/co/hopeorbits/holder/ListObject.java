package co.hopeorbits.holder;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by ADMIN on 12-Sep-17.
 */

public class ListObject extends Application implements Parcelable {

    String Id, Name, Phone, Email, City, State, Country, Address, Zipcode, Distance, Lat, Longi, Image;
    Bitmap bitmap;

    public ListObject() {

    }

    public ListObject(JSONObject jsonData) {

        try {


            this.setId(jsonData.getString("id"));
            this.setName(jsonData.getString("name"));
            this.setPhone(jsonData.getString("phone"));
            this.setEmail(jsonData.getString("email"));
            this.setCity(jsonData.getString("city"));
            this.setState(jsonData.getString("state"));
            this.setCountry(jsonData.getString("country"));
            this.setAddress(jsonData.getString("address"));
            this.setZipcode(jsonData.getString("zipcode"));
            this.setDistance(jsonData.getString("distance"));
            this.setLat(jsonData.getString("latitude"));
            this.setLongi(jsonData.getString("longitude"));
            this.setImage(jsonData.getString("photo"));

//                                Distance = c.getString("distance");
//                                Longi = c.getString("longitude");
//                                Lat = c.getString("latitude");
//                                Image = c.getString("photo");
//                                Holder h = new Holder();



        } catch (Throwable t) {

            Log.e("Question", "Could not parse malformed JSON: \"" + jsonData.toString() + "\"");

        } finally {

            Log.d("Question", jsonData.toString());
        }
    }


    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getZipcode() {
        return Zipcode;
    }

    public void setZipcode(String zipcode) {
        Zipcode = zipcode;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLongi() {
        return Longi;
    }

    public void setLongi(String longi) {
        Longi = longi;
    }

    @Override
    public int describeContents() {

        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator CREATOR = new Creator() {

        public ListObject createFromParcel(Parcel in) {

            return new ListObject();
        }

        public ListObject[] newArray(int size) {
            return new ListObject[size];
        }
    };
}
