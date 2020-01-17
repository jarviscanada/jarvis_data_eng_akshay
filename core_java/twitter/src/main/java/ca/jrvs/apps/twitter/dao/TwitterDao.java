package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtil;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
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
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  @Override
  public Tweet create(Tweet tweet) {
    URI uri;
    try {
      uri = getPostURI(tweet);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid tweet syntax ", e);
    }
    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);
  }


  @Override
  public Tweet findById(String id) {
    URI uri;
    try {
      uri = getFindURI(id);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid URI syntax for tweet id", e);
    }
    HttpResponse response = httpHelper.httpGet(uri);
    return parseResponseBody(response, HTTP_OK);
  }

  @Override
  public Tweet deleteById(String id) {
    URI uri;
    try {
      uri = getDeleteUri(id);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid URI syntax for tweet id", e);
    }
    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponseBody(response, HTTP_OK);

  }

  private URI getPostURI(Tweet tweet) throws URISyntaxException {
    String text = tweet.getText();
    double[] coordinates = tweet.getCoordinates().getCoordinates();
    double longitude = coordinates[0];
    double latitude = coordinates[1];

    PercentEscaper percentEscaper = new PercentEscaper("", false);
    return new URI(API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL +
        percentEscaper.escape(text) + AMPERSAND + "long" + EQUAL + longitude +
        AMPERSAND + "lat" + EQUAL + latitude);
  }

  private URI getFindURI(String id) throws URISyntaxException {
    return new URI(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + id);
  }

  private URI getDeleteUri(String id) throws URISyntaxException {
    return new URI(API_BASE_URI + DELETE_PATH + "/" + id + ".json");
  }

  protected Tweet parseResponseBody(HttpResponse response, int expectedStatusCode) {
    Tweet tweet = null;

    //Verify response status
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatusCode) {
      try {
        System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        System.out.println("Response has no entity");
      }
      throw new RuntimeException("Unexpected HTTP status:" + status);
    }
    if (response.getEntity() == null) {
      throw new RuntimeException("Empty response body");
    }

    //Convert response entity to string
    String jsonStr;
    try {
      jsonStr = EntityUtils.toString(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert entity to String");
    }
    //Deserialize jsonStr into Tweet object
    try {
      tweet = JsonUtil.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to convert JSON string to tweet Object");
    }
    return tweet;
  }
}
