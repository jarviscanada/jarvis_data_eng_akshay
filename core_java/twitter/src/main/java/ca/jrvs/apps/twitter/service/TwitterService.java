package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class TwitterService implements Service {

  private CrdDao dao;

  @Autowired
  public TwitterService(CrdDao dao) {
    this.dao=dao;
  }

  @Override
  public Tweet postTweet(Tweet tweet) {
    validatePostTweet(tweet);

    return (Tweet) dao.create(tweet);
  }

  @Override
  public Tweet showTweet(String id) {
    validateId(id);
    return (Tweet) dao.findById(id);
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> deleted = new ArrayList<>();

    for(String id : ids) {
      validateId(id);
      deleted.add((Tweet) dao.deleteById(id));
    }

    return deleted;
  }

  private void validatePostTweet(Tweet tweet) {
    if (tweet.getText().length() > 140) {
      throw new RuntimeException("Tweet is too long, it should be 140 characters or less");
    }

    Double lon = tweet.getCoordinates().getCoordinates()[0];
    Double lat = tweet.getCoordinates().getCoordinates()[1];
    if (lon > 180 || lon < -180 || lat > 90 || lat <-90) {
      throw new RuntimeException("Coordinates out of range, please enter valid coordinates");
    }
  }

  private void validateId(String id) {
    if (!id.matches("^\\d+$")) {
      throw new RuntimeException("Invalid Tweet Id");
    }
  }
}