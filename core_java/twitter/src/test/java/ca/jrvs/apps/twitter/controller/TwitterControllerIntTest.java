package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.service.TwitterService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TwitterControllerIntTest {

  private static TwitterController controller;
  private List<String> ids = new ArrayList<String>();

  @BeforeClass
  public static void setUp() throws Exception {

    String consumerKey = System.getenv("CONSUMER_KEY");
    String consumerSecret = System.getenv("CONSUMER_SECRET");
    String accessToken = System.getenv("ACCESS_TOKEN");
    String tokenSecret = System.getenv("TOKEN_SECRET");
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    TwitterDao dao = new TwitterDao(httpHelper);
    Service service = new TwitterService(dao);
    controller = new TwitterController(service);

  }

  @Before
  public void postTweets() throws Exception {
    String[] args;
    for (int i = 0; i < 3; i++) {
      args = new String[]{"post", "Remember to #YOLO Sample tweet" + System.currentTimeMillis(), "-2.0:8.0"};
      Tweet createdTweet = controller.postTweet(args);
      assertNotNull(createdTweet);
      ids.add(createdTweet.getIdStr());
    }
  }

  @Test
  public void showTweets() throws Exception {
    String[] arguments;
    for (String id : ids) {
      arguments = new String[]{"show", id};
      Tweet returnedTweet = controller.showTweet(arguments);
      assertNotNull(returnedTweet);
      assertEquals(id, returnedTweet.getIdStr());
    }
  }

  @Test
  public void deleteTweets() throws Exception {
    StringBuilder stringBuilder = new StringBuilder();
    for (String id : ids) {
      stringBuilder.append(id + controller.COMMA);
    }
    String[] arguments = new String[]{"delete", stringBuilder.toString()};
    List<Tweet> deletedTweets = controller.deleteTweet(arguments);
    for (Tweet deletedTweet : deletedTweets) {
      assertNotNull(deletedTweet);
    }
  }

}