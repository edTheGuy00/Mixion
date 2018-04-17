package com.taskail.mixion.data.models.remote;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**Created by ed on 10/2/17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "voter",
        "weight",
        "rshares",
        "percent",
        "reputation",
        "time"
})
public class ActiveVote implements Serializable {

    @JsonProperty("voter")
    private String voter;
    @JsonProperty("weight")
    private Float weight;
    @JsonProperty("rshares")
    private Float rshares;
    @JsonProperty("percent")
    private Integer percent;
    @JsonProperty("reputation")
    private Float reputation;
    @JsonProperty("time")
    private String time;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("voter")
    public String getVoter() {
        return voter;
    }

    @JsonProperty("voter")
    public void setVoter(String voter) {
        this.voter = voter;
    }

    @JsonProperty("weight")
    public Float getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(Float weight) {
        this.weight = weight;
    }

    @JsonProperty("rshares")
    public Float getRshares() {
        return rshares;
    }

    @JsonProperty("rshares")
    public void setRshares(Float rshares) {
        this.rshares = rshares;
    }

    @JsonProperty("percent")
    public Integer getPercent() {
        return percent;
    }

    @JsonProperty("percent")
    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    @JsonProperty("reputation")
    public Float getReputation() {
        return reputation;
    }

    @JsonProperty("reputation")
    public void setReputation(Float reputation) {
        this.reputation = reputation;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
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
