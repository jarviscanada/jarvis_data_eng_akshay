package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class TwitterController implements Controller{

  public static final String COORD_SEP = ":";
  public static final String COMMA = ",";

  private Service service;

  @Autowired
  public TwitterController(Service service){
    this.service = service;
  }

  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3){
      throw new IllegalArgumentException("Please enter 3 arguments like: "
          + "USAGE: TwitterCLIApp post \"text to post\", \"longitude:latitude\"");
    }
    String text = args[1];
    String coordinates = args[2];
    String[] coordArray = coordinates.split(COORD_SEP);
    if (StringUtils.isEmpty(text)){
      throw new IllegalArgumentException("Please enter non whitespace text for your tweet");
    }
    if (coordArray.length != 2){
      throw new IllegalArgumentException("The format of Coordinates is not correct. \n"
          + "Please enter like this: longitude:latitude.");
    }
    Double lon;
    Double lat;
    try{
      lon = Double.parseDouble(coordArray[0]);
      lat = Double.parseDouble(coordArray[1]);
    }catch (Exception e){
      throw new IllegalArgumentException("The coordinates shouldn't contain any characters.");
    }
    Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
    return service.postTweet(postTweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    if (args.length != 2){
      throw new IllegalArgumentException("Please enter 2 arguments like: "
          + "USAGE: TwitterCLIApp show \"tweet_id\"");
    }
    String id = args[1];
    return service.showTweet(id);
  }


  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length != 2){
      throw new IllegalArgumentException("Please enter 2 arguments like: "
          + "USAGE: TwitterCLIApp delete \"tweet_id,tweet_id,...\"");
    }
    String[] ids = args[1].split(COMMA);
    return service.deleteTweets(ids);
  }
}