package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class QuoteService {


    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    /**
     * Helper method: Map a IexQuote to a Quote entity
     * Note: `iexQuote.getLatestPrice() == null` if the stock market is closed.
     * Make sure set a default value for number fields.
     */
    protected static Quote IexQuoteToQuote(IexQuote iexQuote) {
        Quote quote = new Quote();
        quote.setTicker(iexQuote.getSymbol());
        quote.setLastPrice(iexQuote.getLatestPrice());
        quote.setAskPrice(iexQuote.getIexAskPrice());
        quote.setAskSize(iexQuote.getIexAskSize());
        quote.setBidPrice(iexQuote.getIexBidPrice());
        quote.setBidSize(iexQuote.getIexBidSize());
        return quote;
    }

    /**
     * Update quote table against IEX source
     * - get all quotes from the db
     * - foreach ticker get IexQuote
     * - convert iexQuote to quote entity
     * - persist quote to db
     *
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public void updateMarketData() {
        List<Quote> quotes = quoteDao.findAll();
        IexQuote iexQuote;
        Quote updatedQuote;
        for (Quote quote : quotes) {
            String ticker = quote.getTicker();
            iexQuote = marketDataDao.findById(ticker).get();
            updatedQuote = IexQuoteToQuote(iexQuote);
            quoteDao.save(updatedQuote);
        }
    }

    public List<Quote> saveQuotes(List<String> tickers) {
        List<Quote> quotes = new ArrayList<>();
        for (String ticker : tickers) {
            quotes.add(saveQuote(ticker));
        }
        return quotes;
    }

    public Quote saveQuote(Quote quote) {
        return quoteDao.save(quote);
    }

    public Quote saveQuote(String ticker) {
        IexQuote iexQuote = marketDataDao.findById(ticker).get();
        Quote quote = IexQuoteToQuote(iexQuote);
        return quoteDao.save(quote);
    }

    public IexQuote findIexQuoteByTicker(String ticker) {
        return marketDataDao.findById(ticker)
                .orElseThrow(() -> new IllegalArgumentException("IEX Data could not be retrieved for " + ticker));
    }

    public List<Quote> findAllQuotes() {
        return quoteDao.findAll();
    }
}