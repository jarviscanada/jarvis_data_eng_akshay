package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.IexQuote;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;

import java.util.*;

import org.assertj.core.util.Lists;
import org.junit.After;
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
public class PositionDaoIntTest {

    @Autowired
    private PositionDao positionDao;

    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private QuoteDao quoteDao;

    private Account account;
    private Trader savedTrader;
    private SecurityOrder securityOrder;
    private Quote savedQuote;
    private Position position;

    @Before
    public void setUp() throws Exception {
        savedQuote = new Quote();
        savedTrader = new Trader();
        account = new Account();
        securityOrder = new SecurityOrder();
        position = new Position();

        savedQuote.setId("AAPL");
        savedQuote.setLastPrice(224.65);
        savedQuote.setAskPrice(225.2);
        savedQuote.setBidPrice(224.4);
        savedQuote.setAskSize((long) 585);
        savedQuote.setBidSize((long) 974);
        quoteDao.save(savedQuote);

        savedTrader.setId(6);
        savedTrader.setFirstName("Akshay");
        savedTrader.setLastName("Sapra");
        savedTrader.setCountry("Canada");
        savedTrader.setEmail("fake@gmail.com");
        savedTrader.setDob(new java.sql.Date(1990,12,10));
        savedTrader = traderDao.save(savedTrader);

        account.setId(6);
        account.setTrader_id(savedTrader.getId());
        account.setAmount(90000.5);
        account = accountDao.save(account);

        securityOrder.setId(874);
        securityOrder.setTicker("AAPL");
        securityOrder.setPrice(225.1);
        securityOrder.setStatus("PENDING");
        securityOrder.setAccountId(account.getId());
        securityOrder.setNotes("");
        securityOrderDao.save(securityOrder);

        position.setAccountId(account.getId());
        position.setTicker(savedQuote.getTicker());
        position.setPosition(securityOrder.getSize());

    }


    @Test
    public void getJdbcTemplate() {
        assertNotNull(positionDao.getJdbcTemplate());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getSimpleJdbcInsert() {
        positionDao.getSimpleJdbcInsert();
    }

    @Test
    public void getTableName() {
        assertEquals("position", positionDao.getTableName().toLowerCase());
    }

    @Test
    public void getIdColumnName() {
        assertEquals("account_id", positionDao.getIdColumnName().toLowerCase());
    }

    @Test
    public void getEntityClass() {
        assertEquals(Position.class, positionDao.getEntityClass());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void updateOne() {
        positionDao.updateOne(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void save() {
        positionDao.save(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void saveAll() {
        positionDao.saveAll(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void deleteById() {
        positionDao.deleteById(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete() {
        positionDao.delete(new Position());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void deleteAll() {
        positionDao.deleteAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeleteAll() {
        positionDao.deleteAll(null);
    }

}