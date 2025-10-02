package com.example.myclinic.db.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "doctors")
public class Doctor {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String category;
    private String bio;
    private String imageUri;
    private float rating;

    public Doctor(String name, String category, String bio, String imageUri, float rating) {
        this.name = name;
        this.category = category;
        this.bio = bio;
        this.imageUri = imageUri;
        this.rating = rating;
    }

    public Doctor() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getImageUri() { return imageUri; }
    public void setImageUri(String imageUri) { this.imageUri = imageUri; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", bio='" + bio + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", rating=" + rating +
                '}';
    }
}
