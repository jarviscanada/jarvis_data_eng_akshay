package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Sql({"classpath:schema.sql"})
@SpringBootTest(classes = {TestConfig.class})
public class SecurityOrderDaoIntTest {

    @Autowired
    SecurityOrderDao securityOrderTestDao;
    @Autowired
    QuoteDao quoteDao;
    @Autowired
    TraderDao traderDao;
    @Autowired
    AccountDao accountDao;

    private SecurityOrder testOrder;
    private SecurityOrder testOrder2;
    private Quote testQuote;
    private Trader testTrader;
    private Account testAccount;


    @Before
    public void setUp() throws Exception {
        testQuote = new Quote();
        testTrader = new Trader();
        testAccount = new Account();
        testOrder = new SecurityOrder();

        testQuote.setId("AAPL");
        testQuote.setLastPrice(224.65);
        testQuote.setAskPrice(225.2);
        testQuote.setBidPrice(224.4);
        testQuote.setAskSize((long) 585);
        testQuote.setBidSize((long) 974);
        quoteDao.save(testQuote);

        testTrader.setId(6);
        testTrader.setFirstName("Akshay");
        testTrader.setLastName("Sapra");
        testTrader.setCountry("Canada");
        testTrader.setEmail("fake@gmail.com");
        testTrader.setDob(new Date(1990,12,10));
        testTrader = traderDao.save(testTrader);

        testAccount.setId(6);
        testAccount.setTrader_id(testTrader.getId());
        testAccount.setAmount(90000.5);
        testAccount = accountDao.save(testAccount);

        testOrder.setId(874);
        testOrder.setTicker("AAPL");
        testOrder.setPrice(225.1);
        testOrder.setSize(100);
        testOrder.setStatus("PENDING");
        testOrder.setAccountId(testAccount.getId());
        testOrder.setNotes("");

        testOrder2 = new SecurityOrder();
        testOrder2.setId(15);
        testOrder2.setTicker("AAPL");
        testOrder2.setPrice(199.99);
        testOrder2.setSize(20);
        testOrder2.setStatus("FILLED");
        testOrder2.setAccountId(testAccount.getId());
        testOrder2.setNotes("");
    }


    @Test
    public void save() {
        securityOrderTestDao.save(testOrder);
        assertTrue(testOrder.getId() >= 1);
    }

    @Test
    public void saveAll() {
        securityOrderTestDao.saveAll(Arrays.asList(testOrder, testOrder2));
        assertEquals(2, securityOrderTestDao.count());
    }

    @Test
    public void findById() {
        securityOrderTestDao.save(testOrder);
        SecurityOrder order = securityOrderTestDao.findById(testOrder.getId()).orElse(null);
        assertNotNull(order);
        assertEquals(testOrder.getNotes(), order.getNotes());
        assertEquals(testOrder.getTicker(), order.getTicker());
        assertEquals(testOrder.getStatus(), order.getStatus());
        assertEquals(testOrder.getAccountId(), order.getAccountId());
    }

    @Test
    public void existsById() {
        securityOrderTestDao.save(testOrder);
        assertTrue(securityOrderTestDao.existsById(testOrder.getId()));
    }

    @Test
    public void findAll() {
        securityOrderTestDao.saveAll(Arrays.asList(testOrder, testOrder2));
        assertEquals(2, securityOrderTestDao.findAll().size());
    }

    @Test
    public void findAllById() {
        securityOrderTestDao.saveAll(Arrays.asList(testOrder, testOrder2));
        assertEquals(2, securityOrderTestDao.findAllById(Arrays.asList(1, 2)).size());
    }

    @Test
    public void count() {
        securityOrderTestDao.saveAll(Arrays.asList(testOrder, testOrder2));
        assertEquals(2, securityOrderTestDao.count());
    }

    @Test
    public void deleteById() {
        securityOrderTestDao.saveAll(Arrays.asList(testOrder, testOrder2));
        securityOrderTestDao.deleteById(1);
        assertEquals(1, securityOrderTestDao.count());
    }

    @Test
    public void deleteAll() {
        securityOrderTestDao.saveAll(Arrays.asList(testOrder, testOrder2));
        securityOrderTestDao.deleteAll();
        assertEquals(0, securityOrderTestDao.count());
    }

    @Test
    public void getJdbcTemplate() {
        assertEquals(securityOrderTestDao.template, securityOrderTestDao.getJdbcTemplate());
    }

    @Test
    public void getSimpleJdbcInsert() {
        assertEquals(securityOrderTestDao.simpleInsert, securityOrderTestDao.getSimpleJdbcInsert());
    }

    @Test
    public void getTableName() {
        assertEquals(securityOrderTestDao.TABLE_NAME, securityOrderTestDao.getTableName());
    }

    @Test
    public void getIdColumnName() {
        assertEquals(securityOrderTestDao.ID_COLUMN_NAME, securityOrderTestDao.getIdColumnName());
    }

    @Test
    public void getEntityClass() {
        assertEquals(SecurityOrder.class, securityOrderTestDao.getEntityClass());
    }

    @Test
    public void updateEntity() {
        testOrder.setStatus("FILLED");
        testOrder.setNotes("FILLED ON " + Instant.now().toString());
        securityOrderTestDao.updateOne(testOrder);
    }

    @After
    public void tearDown() throws Exception {
        securityOrderTestDao.deleteAll();
    }

}