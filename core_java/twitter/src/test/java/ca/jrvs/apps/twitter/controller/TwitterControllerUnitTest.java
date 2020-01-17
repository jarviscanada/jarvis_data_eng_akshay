package ca.jrvs.apps.twitter.controller;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  @Mock
  Service service;

  @InjectMocks
  TwitterController controller;

  @Test
  public void postTweet() {
    when(service.postTweet(any())).thenReturn(new Tweet());
    try {
      String[] arg = {"post", "test", "4:10", "another 1"};
      controller.postTweet(arg);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    String[] arg = {"post", "test", "10:7"};
    Tweet tweet = controller.postTweet(arg);
    assertNotNull(tweet);
  }

  @Test
  public void showTweet() {
    when(service.showTweet(any())).thenReturn(new Tweet());
    try {
      String[] arg = {"show", "test", "12345"};
      controller.showTweet(arg);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }
    String[] arg = {"show", "12345"};
    Tweet tweet = controller.showTweet(arg);

    assertNotNull(tweet);
  }

  @Test
  public void deleteTweets() {
    when(service.deleteTweets(any())).thenReturn(new ArrayList<Tweet>());
    try {
      String[] arg = {"delete", "invalid argument test", "123"};
      controller.deleteTweet(arg);
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    String[] arg = {"delete", "12345"};
    List<Tweet> list = controller.deleteTweet(arg);
    for (Tweet tweet : list) {
      assertNotNull(tweet);
    }
  }
}