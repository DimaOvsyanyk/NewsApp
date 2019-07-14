package com.dimaoprog.newsapiapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SourcesResponse extends BaseNewsApiResponse {

    @SerializedName("sources")
    @Expose
    private List<Source> sources = null;

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
