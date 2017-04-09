package com.springer.patryk.geo_photo.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.google.maps.android.clustering.ClusterItem;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Patryk on 2017-03-26.
 */


public class Picture implements ClusterItem {

    private String pictureId;
    private String uid;
    private Double longtitude;
    private Double latitude;
    private String image;
    private String description;
    private String downloadUrl;

    private byte[] imageStorage;

    public Picture() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Picture(String uid, Double longtitude, Double latitude, Bitmap image) {
        this.uid = uid;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.image = convertToBase64(image);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("longtitude", longtitude);
        result.put("latitude", latitude);
        result.put("description", description);
        result.put("downloadUrl", downloadUrl);
        return result;
    }

    public String convertToBase64(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        imageStorage = stream.toByteArray();
        return Base64.encodeToString(imageStorage, Base64.NO_WRAP);
    }

    public static Bitmap convertBase64ToBitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @SuppressWarnings("VisibleForTests")
    public void saveToFirebase(boolean isPublic) {
        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child(uid).child(UUID.randomUUID().toString() + ".jpg").putBytes(imageStorage);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            Uri downloadUri = taskSnapshot.getDownloadUrl();
            downloadUrl = downloadUri.toString();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            String key = database.child("picture").push().getKey();

            Map<String, Object> pictureValues = this.toMap();
            Map<String, Object> childUpdates = new HashMap<>();

            if (isPublic) {
                childUpdates.put("/pictures/" + key, pictureValues);
                childUpdates.put("/user-picture/" + this.uid + "/" + key,
                        pictureValues);
            } else {
                childUpdates.put("/user-picture/" + this.uid + "/" + key,
                        pictureValues);
            }

            database.updateChildren(childUpdates);
            Log.d("URI", "Download uri: " + downloadUri.toString());
        }).addOnFailureListener(e -> Log.d("URI", "Upload failed"));

    }

    public LatLng getPosition() {
        return new LatLng(this.getLatitude(), this.longtitude);
    }

    @Override
    public String getTitle() {
        return "title";
    }

    @Override
    public String getSnippet() {
        return "snippet";
    }

    @Override
    public String toString() {
        return "Picture{" +
                "uid='" + uid + '\'' +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                '}';
    }

    public String getPictureId() {
        return pictureId;
    }

    public void setPictureId(String pictureId) {
        this.pictureId = pictureId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}