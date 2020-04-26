package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Quote;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;


  @Before
  public void insertOne() {
    savedQuote = new Quote();
    savedQuote.setAskPrice(6d);
    savedQuote.setAskSize((long) 6);
    savedQuote.setBidPrice(6.2d);
    savedQuote.setBidSize((long) 6);
    savedQuote.setId("ABQ");
    savedQuote.setLastPrice(6.1d);
    quoteDao.save(savedQuote);
  }

  @Before
  public void insertTwo() {
    List<Quote> quotes = new ArrayList<>();
    Quote quote1 = new Quote();
    quote1.setAskPrice(4d);
    quote1.setAskSize((long) 4);
    quote1.setBidPrice(4.2d);
    quote1.setBidSize((long) 4);
    quote1.setId("YTB");
    quote1.setLastPrice(9.1d);
    quotes.add(quote1);
    Quote quote2 = new Quote();
    quote2.setAskPrice(1d);
    quote2.setAskSize((long) 1);
    quote2.setBidPrice(1.2d);
    quote2.setBidSize((long) 1);
    quote2.setId("ALP");
    quote2.setLastPrice(1.1d);
    quotes.add(quote2);
    quoteDao.saveAll(quotes);
  }


  @Test
  public void findById() {
    String ticker = savedQuote.getId();
    Optional<Quote> quote = quoteDao.findById(ticker);
    assertEquals(quote.get().getAskPrice(), savedQuote.getAskPrice());
    assertEquals(quote.get().getAskSize(), savedQuote.getAskSize());
    assertEquals(quote.get().getBidPrice(), savedQuote.getBidPrice());
    assertEquals(quote.get().getBidSize(), savedQuote.getBidSize());
    assertEquals(quote.get().getId(), savedQuote.getId());
    assertEquals(quote.get().getLastPrice(), savedQuote.getLastPrice());
  }

  @Test
  public void existsById() {
    assertTrue(quoteDao.existsById(savedQuote.getId()));
    assertFalse(quoteDao.existsById("FAKE"));
  }

  @After
  public void deleteById() {
    quoteDao.deleteById(savedQuote.getId());
    try {
      quoteDao.existsById("SDG");
    } catch (NullPointerException e) {
      assertTrue(true);
    } catch (Exception e) {
      Assert.fail();
    }
  }

}