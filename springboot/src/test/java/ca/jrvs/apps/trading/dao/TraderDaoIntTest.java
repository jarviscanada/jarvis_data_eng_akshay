package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.domain.Trader;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.assertj.core.util.Lists;
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
public class TraderDaoIntTest {


  @Autowired
  private TraderDao traderDao;

  private Trader savedTrader;

  @Before
  public void insertOne() {
    savedTrader = new Trader();
    savedTrader.setCountry("Canada");
    savedTrader.setDob(new Date(1969, 12, 04));
    savedTrader.setEmail("Fake2@gmail.com");
    savedTrader.setFirstName("Fake3");
    savedTrader.setLastName("Person");
    System.out.println(savedTrader.getId());
    //savedTrader.setId(traderDao.save(savedTrader).getId());
    traderDao.save(savedTrader);
    System.out.println(savedTrader.getId());
  }

  @Test
  public void findAllById() {
    List<Trader> traders = Lists
        .newArrayList(traderDao.findAllById(Arrays.asList(savedTrader.getId())));
    assertEquals(1, traders.size());
    assertEquals(savedTrader.getCountry(), traders.get(0).getCountry());
    assertEquals(savedTrader.getEmail(), traders.get(0).getEmail());
    assertEquals(savedTrader.getFirstName(), traders.get(0).getFirstName());
    assertEquals(savedTrader.getLastName(), traders.get(0).getLastName());
    assertEquals(savedTrader.getDob(), traders.get(0).getDob());
    assertEquals(savedTrader.getId(), traders.get(0).getId());
  }

//    @After
//    //public void deleteOne(){
//        traderDao.deleteById(savedTrader.getId());
//    }
}