package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import ca.jrvs.apps.twitter.util.TweetUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {
  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService service;

  Tweet expectedTweet;
  @Before
  public void setup() throws Exception{
    String tweetJsonStr = "{\n" +
        "  \"created_at\" : \"Thu Jan 2 23:16:41 +0000 2020\",\n" +
        "  \"id\" : 7848741081207770622,\n" +
        "  \"id_str\" : \"7848741081207770622\",\n" +
        "  \"text\" : \"Testing this JSON\",\n" +
        "  \"entities\" : {\n" +
        "    \"hashtags\" : [ ],\n" +
        "    \"user_mentions\" : [ ]\n" +
        "  },\n" +
        "  \"coordinates\" : {\n" +
        "    \"coordinates\" : [ 15.0, 48.0 ],\n" +
        "    \"type\" : \"Point\"\n" +
        "  },\n" +
        "  \"retweet_count\" : 0,\n" +
        "  \"retweeted\" : false,\n" +
        "  \"entites\" : {\n" +
        "    \"hashtags\" : [ ],\n" +
        "    \"user_mentions\" : [ ]\n" +
        "  },\n" +
        "  \"favourite_count\" : 0\n" +
        "}";

    try {
      expectedTweet = JsonUtil.toObjectFromJson(tweetJsonStr, Tweet.class);
    }catch (IOException e){
      throw new IOException();
    }
  }

  @Test
  public void postTweet() {
    //test failed request
    when(dao.create(any())).thenReturn(new Tweet());
    try {
      //lon is exceeding the limit
      service.postTweet(TweetUtil.buildTweet("Mockito test", 263.0, 1d));
      fail();
    }catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    Tweet tweet = service.postTweet(expectedTweet);

    assertNotNull(tweet);
  }

  @Test
  public void showTweet() {
    when(dao.findById(any())).thenReturn(new Tweet());
    try {
      //is contains character
      service.showTweet("this is a wrong tweet id");
      fail();
    } catch (IllegalArgumentException e){
      assertTrue(true);
    }

    Tweet tweet = service.showTweet("12345");
    assertNotNull(tweet);
  }

  @Test
  public void deleteTweets() {
    when(dao.deleteById(any())).thenReturn(new Tweet());
    try {
      String[] ids = {"this is a wrong tweet id","7848741081207770621"};
      service.deleteTweets(ids);
      fail();
    }catch (IllegalArgumentException e) {
      assertTrue(true);
    }

    String[] ids = {"7848741081207770622","7848741081207770621"};
    List<Tweet> list = service.deleteTweets(ids);
    for(Tweet tweet : list) {
      assertNotNull(tweet);
    }
  }
}