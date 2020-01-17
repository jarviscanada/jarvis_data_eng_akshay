package ca.jrvs.apps.twitter.dao.helper;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class TwitterHttpHelperTest {

  @Test
  public void httpPost() throws Exception {
    String CONSUMER_KEY = System.getenv("CONSUMER_KEY");
    String CONSUMER_SECRET = System.getenv("CONSUMER_SECRET");
    String ACCESS_TOKEN = System.getenv("ACCESS_TOKEN");
    String TOKEN_SECRET = System.getenv("TOKEN_SECRET");
    HttpHelper httpHelper = new TwitterHttpHelper(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN,
        TOKEN_SECRET);
    HttpResponse response = httpHelper
        .httpPost(new URI("https://api.twitter.com/1.1/statuses/update.json?status=first_tweet"));
    System.out.println(EntityUtils.toString(response.getEntity()));

  }
}