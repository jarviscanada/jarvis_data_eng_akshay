package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TwitterDaoIntTest {

  private static TwitterDao dao;
  private Tweet tweet;
  private String tweetId;

  @BeforeClass
  public static void setup() {
    String CONSUMER_KEY = System.getenv("CONSUMER_KEY");
    String CONSUMER_SECRET = System.getenv("CONSUMER_SECRET");
    String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
    String TOKEN_SECRET = System.getenv("TOKEN_SECRET");
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);
    dao = new TwitterDao(httpHelper);
  }

  @Before
  public void create() {
    System.out.println("Create");
    String hashTag = "YOLO";
    String text =
        "@akshay please #YOLO to the full extent " + hashTag + "" + System.currentTimeMillis();
    double lat = 1d;
    double lon = 20d;
    Tweet postTweet = TweetUtil.buildTweet(text, lon, lat);
    try {
      System.out.println(JsonUtil.toJson(postTweet, true, true));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    tweet = dao.create(postTweet);
    assertEquals(text, tweet.getText());
    assertNotNull(tweet.getCoordinates());
    assertEquals(2, tweet.getCoordinates().getCoordinates().length);
    assertEquals(lon, tweet.getCoordinates().getCoordinates()[0], 0);
    assertEquals(lat, tweet.getCoordinates().getCoordinates()[1], 0);
    assertTrue(hashTag.contains(tweet.getEntities().getHashtags()[0].getText()));
    tweetId = tweet.getIdStr();
  }

  @Test
  public void find() throws Exception {
    System.out.println("find");
    Tweet foundTweet = dao.findById(tweetId);
    System.out.println(JsonUtil.toJson(foundTweet, true, false));

    assertEquals(tweet.getText(), foundTweet.getText());
    assertEquals(foundTweet.getCoordinates().getCoordinates()[0],
        tweet.getCoordinates().getCoordinates()[0], 0);
    assertEquals(foundTweet.getCoordinates().getCoordinates()[1],
        tweet.getCoordinates().getCoordinates()[1], 0);
    assertEquals(tweetId, foundTweet.getIdStr());

  }

  @After
  public void delete() throws Exception {
    System.out.println("delete");
    Tweet deletedTweet = dao.deleteById(tweetId);
    System.out.println(JsonUtil.toJson(deletedTweet, true, false));

    assertEquals(tweet.getText(), deletedTweet.getText());
    assertEquals(deletedTweet.getCoordinates().getCoordinates()[0],
        tweet.getCoordinates().getCoordinates()[0], 0);
    assertEquals(deletedTweet.getCoordinates().getCoordinates()[1],
        tweet.getCoordinates().getCoordinates()[1], 0);
    assertEquals(tweetId, deletedTweet.getIdStr());

  }
}