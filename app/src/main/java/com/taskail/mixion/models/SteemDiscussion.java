package com.taskail.mixion.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
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
        "id",
        "author",
        "permlink",
        "category",
        "parent_author",
        "parent_permlink",
        "title",
        "body",
        "json_metadata",
        "last_update",
        "created",
        "active",
        "last_payout",
        "depth",
        "children",
        "net_rshares",
        "abs_rshares",
        "vote_rshares",
        "children_abs_rshares",
        "cashout_time",
        "max_cashout_time",
        "total_vote_weight",
        "reward_weight",
        "total_payout_value",
        "curator_payout_value",
        "author_rewards",
        "net_votes",
        "root_comment",
        "max_accepted_payout",
        "percent_steem_dollars",
        "allow_replies",
        "allow_votes",
        "allow_curation_rewards",
        "beneficiaries",
        "url",
        "root_title",
        "pending_payout_value",
        "total_pending_payout_value",
        "active_votes",
        "replies",
        "author_reputation",
        "promoted",
        "body_length",
        "reblogged_by"
})
public class SteemDiscussion implements Serializable{

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("author")
    private String author;
    @JsonProperty("permlink")
    private String permlink;
    @JsonProperty("category")
    private String category;
    @JsonProperty("parent_author")
    private String parentAuthor;
    @JsonProperty("parent_permlink")
    private String parentPermlink;
    @JsonProperty("title")
    private String title;
    @JsonProperty("body")
    private String body;
    @JsonProperty("json_metadata")
    private String jsonMetadata;
    @JsonProperty("last_update")
    private String lastUpdate;
    @JsonProperty("created")
    private String created;
    @JsonProperty("active")
    private String active;
    @JsonProperty("last_payout")
    private String lastPayout;
    @JsonProperty("depth")
    private Integer depth;
    @JsonProperty("children")
    private Integer children;
    @JsonProperty("net_rshares")
    private String netRshares;
    @JsonProperty("abs_rshares")
    private String absRshares;
    @JsonProperty("vote_rshares")
    private String voteRshares;
    @JsonProperty("children_abs_rshares")
    private String childrenAbsRshares;
    @JsonProperty("cashout_time")
    private String cashoutTime;
    @JsonProperty("max_cashout_time")
    private String maxCashoutTime;
    @JsonProperty("total_vote_weight")
    private Integer totalVoteWeight;
    @JsonProperty("reward_weight")
    private Integer rewardWeight;
    @JsonProperty("total_payout_value")
    private String totalPayoutValue;
    @JsonProperty("curator_payout_value")
    private String curatorPayoutValue;
    @JsonProperty("author_rewards")
    private Integer authorRewards;
    @JsonProperty("net_votes")
    private Integer netVotes;
    @JsonProperty("root_comment")
    private Integer rootComment;
    @JsonProperty("max_accepted_payout")
    private String maxAcceptedPayout;
    @JsonProperty("percent_steem_dollars")
    private Integer percentSteemDollars;
    @JsonProperty("allow_replies")
    private Boolean allowReplies;
    @JsonProperty("allow_votes")
    private Boolean allowVotes;
    @JsonProperty("allow_curation_rewards")
    private Boolean allowCurationRewards;
    @JsonProperty("beneficiaries")
    private List<Object> beneficiaries = null;
    @JsonProperty("url")
    private String url;
    @JsonProperty("root_title")
    private String rootTitle;
    @JsonProperty("pending_payout_value")
    private String pendingPayoutValue;
    @JsonProperty("total_pending_payout_value")
    private String totalPendingPayoutValue;
    @JsonProperty("active_votes")
    private List<ActiveVote> activeVotes = null;
    @JsonProperty("replies")
    private List<Object> replies;
    @JsonProperty("author_reputation")
    private String authorReputation;
    @JsonProperty("promoted")
    private String promoted;
    @JsonProperty("body_length")
    private Integer bodyLength;
    @JsonProperty("reblogged_by")
    private List<Object> rebloggedBy = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty("permlink")
    public String getPermlink() {
        return permlink;
    }

    @JsonProperty("permlink")
    public void setPermlink(String permlink) {
        this.permlink = permlink;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("parent_author")
    public String getParentAuthor() {
        return parentAuthor;
    }

    @JsonProperty("parent_author")
    public void setParentAuthor(String parentAuthor) {
        this.parentAuthor = parentAuthor;
    }

    @JsonProperty("parent_permlink")
    public String getParentPermlink() {
        return parentPermlink;
    }

    @JsonProperty("parent_permlink")
    public void setParentPermlink(String parentPermlink) {
        this.parentPermlink = parentPermlink;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("body")
    public String getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(String body) {
        this.body = body;
    }

    @JsonProperty("json_metadata")
    public String getJsonMetadata() {
        return jsonMetadata;
    }

    @JsonProperty("json_metadata")
    public void setJsonMetadata(String jsonMetadata) {
        this.jsonMetadata = jsonMetadata;
    }

    @JsonProperty("last_update")
    public String getLastUpdate() {
        return lastUpdate;
    }

    @JsonProperty("last_update")
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("active")
    public String getActive() {
        return active;
    }

    @JsonProperty("active")
    public void setActive(String active) {
        this.active = active;
    }

    @JsonProperty("last_payout")
    public String getLastPayout() {
        return lastPayout;
    }

    @JsonProperty("last_payout")
    public void setLastPayout(String lastPayout) {
        this.lastPayout = lastPayout;
    }

    @JsonProperty("depth")
    public Integer getDepth() {
        return depth;
    }

    @JsonProperty("depth")
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    @JsonProperty("children")
    public Integer getChildren() {
        return children;
    }

    @JsonProperty("children")
    public void setChildren(Integer children) {
        this.children = children;
    }

    @JsonProperty("net_rshares")
    public String getNetRshares() {
        return netRshares;
    }

    @JsonProperty("net_rshares")
    public void setNetRshares(String netRshares) {
        this.netRshares = netRshares;
    }

    @JsonProperty("abs_rshares")
    public String getAbsRshares() {
        return absRshares;
    }

    @JsonProperty("abs_rshares")
    public void setAbsRshares(String absRshares) {
        this.absRshares = absRshares;
    }

    @JsonProperty("vote_rshares")
    public String getVoteRshares() {
        return voteRshares;
    }

    @JsonProperty("vote_rshares")
    public void setVoteRshares(String voteRshares) {
        this.voteRshares = voteRshares;
    }

    @JsonProperty("children_abs_rshares")
    public String getChildrenAbsRshares() {
        return childrenAbsRshares;
    }

    @JsonProperty("children_abs_rshares")
    public void setChildrenAbsRshares(String childrenAbsRshares) {
        this.childrenAbsRshares = childrenAbsRshares;
    }

    @JsonProperty("cashout_time")
    public String getCashoutTime() {
        return cashoutTime;
    }

    @JsonProperty("cashout_time")
    public void setCashoutTime(String cashoutTime) {
        this.cashoutTime = cashoutTime;
    }

    @JsonProperty("max_cashout_time")
    public String getMaxCashoutTime() {
        return maxCashoutTime;
    }

    @JsonProperty("max_cashout_time")
    public void setMaxCashoutTime(String maxCashoutTime) {
        this.maxCashoutTime = maxCashoutTime;
    }

    @JsonProperty("total_vote_weight")
    public Integer getTotalVoteWeight() {
        return totalVoteWeight;
    }

    @JsonProperty("total_vote_weight")
    public void setTotalVoteWeight(Integer totalVoteWeight) {
        this.totalVoteWeight = totalVoteWeight;
    }

    @JsonProperty("reward_weight")
    public Integer getRewardWeight() {
        return rewardWeight;
    }

    @JsonProperty("reward_weight")
    public void setRewardWeight(Integer rewardWeight) {
        this.rewardWeight = rewardWeight;
    }

    @JsonProperty("total_payout_value")
    public String getTotalPayoutValue() {
        return totalPayoutValue;
    }

    @JsonProperty("total_payout_value")
    public void setTotalPayoutValue(String totalPayoutValue) {
        this.totalPayoutValue = totalPayoutValue;
    }

    @JsonProperty("curator_payout_value")
    public String getCuratorPayoutValue() {
        return curatorPayoutValue;
    }

    @JsonProperty("curator_payout_value")
    public void setCuratorPayoutValue(String curatorPayoutValue) {
        this.curatorPayoutValue = curatorPayoutValue;
    }

    @JsonProperty("author_rewards")
    public Integer getAuthorRewards() {
        return authorRewards;
    }

    @JsonProperty("author_rewards")
    public void setAuthorRewards(Integer authorRewards) {
        this.authorRewards = authorRewards;
    }

    @JsonProperty("net_votes")
    public Integer getNetVotes() {
        return netVotes;
    }

    @JsonProperty("net_votes")
    public void setNetVotes(Integer netVotes) {
        this.netVotes = netVotes;
    }

    @JsonProperty("root_comment")
    public Integer getRootComment() {
        return rootComment;
    }

    @JsonProperty("root_comment")
    public void setRootComment(Integer rootComment) {
        this.rootComment = rootComment;
    }

    @JsonProperty("max_accepted_payout")
    public String getMaxAcceptedPayout() {
        return maxAcceptedPayout;
    }

    @JsonProperty("max_accepted_payout")
    public void setMaxAcceptedPayout(String maxAcceptedPayout) {
        this.maxAcceptedPayout = maxAcceptedPayout;
    }

    @JsonProperty("percent_steem_dollars")
    public Integer getPercentSteemDollars() {
        return percentSteemDollars;
    }

    @JsonProperty("percent_steem_dollars")
    public void setPercentSteemDollars(Integer percentSteemDollars) {
        this.percentSteemDollars = percentSteemDollars;
    }

    @JsonProperty("allow_replies")
    public Boolean getAllowReplies() {
        return allowReplies;
    }

    @JsonProperty("allow_replies")
    public void setAllowReplies(Boolean allowReplies) {
        this.allowReplies = allowReplies;
    }

    @JsonProperty("allow_votes")
    public Boolean getAllowVotes() {
        return allowVotes;
    }

    @JsonProperty("allow_votes")
    public void setAllowVotes(Boolean allowVotes) {
        this.allowVotes = allowVotes;
    }

    @JsonProperty("allow_curation_rewards")
    public Boolean getAllowCurationRewards() {
        return allowCurationRewards;
    }

    @JsonProperty("allow_curation_rewards")
    public void setAllowCurationRewards(Boolean allowCurationRewards) {
        this.allowCurationRewards = allowCurationRewards;
    }

    @JsonProperty("beneficiaries")
    public List<Object> getBeneficiaries() {
        return beneficiaries;
    }

    @JsonProperty("beneficiaries")
    public void setBeneficiaries(List<Object> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("root_title")
    public String getRootTitle() {
        return rootTitle;
    }

    @JsonProperty("root_title")
    public void setRootTitle(String rootTitle) {
        this.rootTitle = rootTitle;
    }

    @JsonProperty("pending_payout_value")
    public String getPendingPayoutValue() {
        return pendingPayoutValue;
    }

    @JsonProperty("pending_payout_value")
    public void setPendingPayoutValue(String pendingPayoutValue) {
        this.pendingPayoutValue = pendingPayoutValue;
    }

    @JsonProperty("total_pending_payout_value")
    public String getTotalPendingPayoutValue() {
        return totalPendingPayoutValue;
    }

    @JsonProperty("total_pending_payout_value")
    public void setTotalPendingPayoutValue(String totalPendingPayoutValue) {
        this.totalPendingPayoutValue = totalPendingPayoutValue;
    }

    @JsonProperty("active_votes")
    public List<ActiveVote> getActiveVotes() {
        return activeVotes;
    }

    @JsonProperty("active_votes")
    public void setActiveVotes(List<ActiveVote> activeVotes) {
        this.activeVotes = activeVotes;
    }

    @JsonProperty("replies")
    public List<Object> getReplies() {
        return replies;
    }

    @JsonProperty("replies")
    public void setReplies(List<Object> replies) {
        this.replies = replies;
    }

    @JsonProperty("author_reputation")
    public String getAuthorReputation() {
        return authorReputation;
    }

    @JsonProperty("author_reputation")
    public void setAuthorReputation(String authorReputation) {
        this.authorReputation = authorReputation;
    }

    @JsonProperty("promoted")
    public String getPromoted() {
        return promoted;
    }

    @JsonProperty("promoted")
    public void setPromoted(String promoted) {
        this.promoted = promoted;
    }

    @JsonProperty("body_length")
    public Integer getBodyLength() {
        return bodyLength;
    }

    @JsonProperty("body_length")
    public void setBodyLength(Integer bodyLength) {
        this.bodyLength = bodyLength;
    }

    @JsonProperty("reblogged_by")
    public List<Object> getRebloggedBy() {
        return rebloggedBy;
    }

    @JsonProperty("reblogged_by")
    public void setRebloggedBy(List<Object> rebloggedBy) {
        this.rebloggedBy = rebloggedBy;
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
