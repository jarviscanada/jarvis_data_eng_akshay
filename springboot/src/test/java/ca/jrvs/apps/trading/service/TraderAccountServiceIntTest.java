package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.sql.Date;
import java.time.LocalDate;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class TraderAccountServiceIntTest {
    private TraderAccountView traderAccountView;

    @Autowired
    private TraderAccountService traderAccountService;

    @Autowired
    private TraderDao traderDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private SecurityOrderDao securityOrderDao;

    @Autowired
    private QuoteDao quoteDao;

    @Before
    public void setup(){
        securityOrderDao.deleteAll();
        accountDao.deleteAll();
        traderDao.deleteAll();

    }

    @Test
    public void createTraderAccount(){
        Trader trader = new Trader();
        trader.setFirstName("Akshay");
        trader.setLastName("Sapra");
        trader.setCountry("Canada");
        trader.setEmail("fakee@gmail.com");
        trader.setDob(Date.valueOf(LocalDate.now()));

        traderAccountView = traderAccountService.createTraderAndAccount(trader);

        assertNotNull(traderAccountView.getTrader().getId());
        assertNotNull(traderAccountView.getAccount().getId());
        assertEquals(Double.valueOf(0.0), traderAccountView.getAccount().getAmount());
        assertNotEquals(0,accountDao.findAll().size());
        assertNotEquals(0,traderDao.findAll().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTraderAccountWithInvalidTrader(){
        Trader trader = new Trader();
        trader.setFirstName(null);
        trader.setLastName("sap");
        trader.setCountry("canada");
        trader.setEmail("ha@gmail.com");
        trader.setDob(Date.valueOf(LocalDate.now()));

        traderAccountService.createTraderAndAccount(trader);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTraderWithId(){
        Trader trader = new Trader();
        trader.setId(10);
        trader.setFirstName("John");
        trader.setLastName("Smith");
        trader.setCountry("india");
        trader.setEmail("john@gmail.com");
        trader.setDob(Date.valueOf(LocalDate.now()));

        traderAccountService.createTraderAndAccount(trader);
    }

    @Test
    public void depositToAccount(){
        Trader trader = new Trader();
        trader.setFirstName("John");
        trader.setLastName("Smith");
        trader.setCountry("india");
        trader.setEmail("john@gmail.com");
        trader.setDob(Date.valueOf(LocalDate.now()));

        traderAccountService.createTraderAndAccount(trader);

        Integer id = accountDao.findAll().get(0).getId();
        Double amount = 500000.0;
        Account account = traderAccountService.deposit(id, amount);

        assertEquals(Double.valueOf(500000.0), account.getAmount());
    }

    @Test(expected = EntityNotFoundException.class)
    public void depositToNonExistentAccount(){
        traderAccountService.deposit(100, 5000.0);
    }


    @Test(expected = IllegalArgumentException.class)
    public void depositZeroFunds(){
        traderAccountService.deposit(1, 0.0);
    }

    @Test
    public void withdrawFundsFromAccount(){
        Trader trader = new Trader();
        trader.setFirstName("John");
        trader.setLastName("Smith");
        trader.setCountry("india");
        trader.setEmail("john@gmail.com");
        trader.setDob(Date.valueOf(LocalDate.now()));
        traderAccountService.createTraderAndAccount(trader);

        Integer id = accountDao.findAll().get(0).getId();
        Account account = traderAccountService.deposit(id, 3000.0);

        Account account2 = traderAccountService.withdraw(id, 3000.0);

        assertEquals(Double.valueOf(0.0), account2.getAmount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void withdrawFundsExceedingAccountBalance(){
        Trader trader = new Trader();
        trader.setFirstName("John");
        trader.setLastName("Smith");
        trader.setCountry("india");
        trader.setEmail("john@gmail.com");
        trader.setDob(Date.valueOf(LocalDate.now()));
        traderAccountService.createTraderAndAccount(trader);

        Integer id = accountDao.findAll().get(0).getId();
        Account account = traderAccountService.deposit(id, 5000.0);

        account = traderAccountService.withdraw(id, 6000.0);
    }

    @Test
    public void deleteTraderAccountById(){
        Trader trader = new Trader();
        trader.setFirstName("John");
        trader.setLastName("Smith");
        trader.setCountry("india");
        trader.setEmail("john@gmail.com");
        trader.setDob(Date.valueOf(LocalDate.now()));
        TraderAccountView traderAccountView = traderAccountService.createTraderAndAccount(trader);

        Quote quote = new Quote();
        quote.setAskPrice(10d);
        quote.setAskSize((long) 11);
        quote.setBidPrice(10.2d);
        quote.setBidSize((long) 10);
        quote.setTicker("FB");
        quote.setLastPrice(10.36d);
        quoteDao.save(quote);

        SecurityOrder securityOrder = new SecurityOrder();
        securityOrder.setAccountId(traderAccountView.getAccount().getId());
        securityOrder.setNotes("");
        securityOrder.setPrice(150.0);
        securityOrder.setSize(1000);
        securityOrder.setStatus("FILLED");
        securityOrder.setTicker(quote.getTicker());
        securityOrderDao.save(securityOrder);

        traderAccountService.deleteTraderById(traderAccountView.getTrader().getId());

        assertEquals(0,securityOrderDao.findAll().size());
        assertEquals(0,traderDao.findAll().size());
        assertEquals(0,accountDao.findAll().size());
    }

}