package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;

public class TwitterDao implements CrdDao<Tweet, String> {

  //URI contraints
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";
  //response code
  private static final int HTTP_OK = 200;
   //dependency
  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao (HttpHelper httpHelperIn){
    this.httpHelper = httpHelperIn;
  }

  @Override
  public Tweet create(Tweet tweet) {
    //Put the tweet in a URI
    URI uri;
    uri=getPostURI(tweet);
    return null;
  }

  private URI getPostURI(Tweet tweet) {

  }

  @Override
  public Tweet findById(String s) {
    return null;
  }

  @Override
  public Tweet deleteById(String s) {
    return null;
  }
}
