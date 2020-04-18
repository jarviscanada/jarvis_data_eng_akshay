package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

    @Autowired
    private QuoteDao quoteDao;

    @Autowired
    private QuoteService quoteService;

    private Quote savedQuote;

    @Before
    public void setUp() throws Exception {
        Quote quote = new Quote();
        savedQuote = quote;
        quote.setAskPrice(6d);
        quote.setAskSize((long) 6);
        quote.setBidPrice(6.2d);
        quote.setBidSize((long) 6);
        quote.setId("BA");
        quote.setLastPrice(6.1d);
        quoteDao.save(quote);
        savedQuote.setAskPrice(10d);
        savedQuote.setAskSize((long) 10);
        savedQuote.setBidPrice(10.2d);
        savedQuote.setBidSize((long) 10);
        savedQuote.setId("GOOGL");
        savedQuote.setLastPrice(10.1d);
    }

    @Test
    public void findIexQuoteByTicker() {
        IexQuote iexQuote = quoteService.findIexQuoteByTicker("AAPL");
        assertNotNull(iexQuote);
    }

    @Test
    public void updateMarketData() {
        quoteService.updateMarketData();
        assertNotEquals(0, quoteDao.count());
        assertTrue(quoteDao.existsById("BA"));
        assertFalse(quoteDao.existsById("ABCD"));
    }

    @Test
    public void saveQuotes() {
        List<String> tickers = new ArrayList<>();
        tickers.add("AAPL");
        tickers.add("AMZN");
        tickers.add("FB");
        quoteService.saveQuotes(tickers);
        assertEquals(4, quoteDao.count());
    }

    @Test
    public void saveQuote() {
        quoteService.saveQuote(savedQuote);
        assertEquals(2, quoteDao.count());
    }


    @After
    public void cleanup() throws Exception {
        quoteDao.deleteAll();
    }
}