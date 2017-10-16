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

/**Created by ed on 10/3/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pages",
        "hits",
        "results",
        "time",
        "error"
})

public class AskSteem {

    @JsonProperty("pages")
    private Pages pages;
    @JsonProperty("hits")
    private Integer hits;
    @JsonProperty("results")
    private List<Result> results = null;
    @JsonProperty("time")
    private Double time;
    @JsonProperty("error")
    private Boolean error;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pages")
    public Pages getPages() {
        return pages;
    }

    @JsonProperty("pages")
    public void setPages(Pages pages) {
        this.pages = pages;
    }

    @JsonProperty("hits")
    public Integer getHits() {
        return hits;
    }

    @JsonProperty("hits")
    public void setHits(Integer hits) {
        this.hits = hits;
    }

    @JsonProperty("results")
    public List<Result> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonProperty("time")
    public Double getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(Double time) {
        this.time = time;
    }

    @JsonProperty("error")
    public Boolean getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(Boolean error) {
        this.error = error;
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