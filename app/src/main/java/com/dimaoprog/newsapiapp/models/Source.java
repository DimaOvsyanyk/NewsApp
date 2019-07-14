package com.dimaoprog.newsapiapp.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "sources")
public class Source {

    public static final int SELECTED = 1;
    public static final int UNSELECTED = 0;

    @PrimaryKey
    @SerializedName("id")
    @Expose
    @NonNull
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("country")
    @Expose
    private String country;

    @ColumnInfo(name = "is_selected", index = true)
    private int isSelectedSource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getIsSelectedSource() {
        return isSelectedSource;
    }

    public void setIsSelectedSource(int isSelectedSource) {
        this.isSelectedSource = isSelectedSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Source source = (Source) o;
        return getIsSelectedSource() == source.getIsSelectedSource() &&
                getId().equals(source.getId()) &&
                Objects.equals(getName(), source.getName()) &&
                Objects.equals(getDescription(), source.getDescription()) &&
                Objects.equals(getUrl(), source.getUrl()) &&
                Objects.equals(getCategory(), source.getCategory()) &&
                Objects.equals(getLanguage(), source.getLanguage()) &&
                Objects.equals(getCountry(), source.getCountry());
    }
}
