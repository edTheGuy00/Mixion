package com.taskail.mixion.data.models;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "total_payouts",
        "net_votes",
        "top_posts",
        "comments",
        "trending"
})
public class Tags {

    @JsonProperty("name")
    private String name;
    @JsonProperty("total_payouts")
    private String totalPayouts;
    @JsonProperty("net_votes")
    private Integer netVotes;
    @JsonProperty("top_posts")
    private Integer topPosts;
    @JsonProperty("comments")
    private Integer comments;
    @JsonProperty("trending")
    private String trending;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("total_payouts")
    public String getTotalPayouts() {
        return totalPayouts;
    }

    @JsonProperty("total_payouts")
    public void setTotalPayouts(String totalPayouts) {
        this.totalPayouts = totalPayouts;
    }

    @JsonProperty("net_votes")
    public Integer getNetVotes() {
        return netVotes;
    }

    @JsonProperty("net_votes")
    public void setNetVotes(Integer netVotes) {
        this.netVotes = netVotes;
    }

    @JsonProperty("top_posts")
    public Integer getTopPosts() {
        return topPosts;
    }

    @JsonProperty("top_posts")
    public void setTopPosts(Integer topPosts) {
        this.topPosts = topPosts;
    }

    @JsonProperty("comments")
    public Integer getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @JsonProperty("trending")
    public String getTrending() {
        return trending;
    }

    @JsonProperty("trending")
    public void setTrending(String trending) {
        this.trending = trending;
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
