package com.taskail.mixion.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by ed on 10/4/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tags",
        "image",
        "links",
        "app",
        "format"
})



public class JsonMetaData {

    @JsonProperty("tags")
    private List<String> tags = null;
    @JsonProperty("image")
    private List<String> image = null;
    @JsonProperty("links")
    private List<String> links = null;
    @JsonProperty("app")
    private String app;
    @JsonProperty("format")
    private String format;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("tags")
    public List<String> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @JsonProperty("image")
    public List<String> getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(List<String> image) {
        this.image = image;
    }

    @JsonProperty("links")
    public List<String> getLinks() {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(List<String> links) {
        this.links = links;
    }

    @JsonProperty("app")
    public String getApp() {
        return app;
    }

    @JsonProperty("app")
    public void setApp(String app) {
        this.app = app;
    }

    @JsonProperty("format")
    public String getFormat() {
        return format;
    }

    @JsonProperty("format")
    public void setFormat(String format) {
        this.format = format;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}