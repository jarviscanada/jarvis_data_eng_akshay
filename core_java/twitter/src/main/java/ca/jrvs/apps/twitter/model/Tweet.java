package ca.jrvs.apps.twitter.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({
    "created_at",
    "id",
    "id_str",
    "text",
    "entities",
    "coordinates",
    "retweet_count",
    "favorite_count",
    "favorited",
    "retweeted"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {

  @JsonProperty("created_at")
  private String createdAt;

  @JsonProperty("id")
  private Long id;

  @JsonProperty("id_str")
  private String idStr;

  @JsonProperty("text")
  private String text;

  @JsonProperty("entities")
  private Entities entities;

  @JsonProperty("coordinates")
  private Coordinates coordinates;

  @JsonProperty("retweet_count")
  private Long retweetCount;

  @JsonProperty("favorite_count")
  private Long favoriteCount;

  @JsonProperty("favorited")
  private Boolean favorited;

  @JsonProperty("retweeted")
  private Boolean retweeted;

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIdStr() {
    return idStr;
  }

  public void setIdStr(String idStr) {
    this.idStr = idStr;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Entities getEntities() {
    return entities;
  }

  public void setEntities(Entities entities) {
    this.entities = entities;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public Long getRetweetCount() {
    return retweetCount;
  }

  public void setRetweetCount(Long retweetCount) {
    this.retweetCount = retweetCount;
  }

  public Long getFavoriteCount() {
    return favoriteCount;
  }

  public void setFavoriteCount(Long favoriteCount) {
    this.favoriteCount = favoriteCount;
  }

  public Boolean getFavorited() {
    return favorited;
  }

  public void setFavorited(Boolean favorited) {
    this.favorited = favorited;
  }

  public Boolean getRetweeted() {
    return retweeted;
  }

  public void setRetweeted(Boolean retweeted) {
    this.retweeted = retweeted;
  }

}
