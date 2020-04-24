package ca.jrvs.apps.trading.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class OrderServiceUnitTest {

  @Captor
  ArgumentCaptor<SecurityOrder> captorSecurityOrder;

  @Mock
  private AccountDao accountDao;
  @Mock
  private SecurityOrderDao securityOrderDao;
  @Mock
  private QuoteDao quoteDao;
  @Mock
  private PositionDao positionDao;

  @InjectMocks
  private OrderService orderService;

  @Before
  public void setup(){
    Quote quote = new Quote();
    quote.setBidSize((long) 10);
    quote.setAskSize((long) 20);
    quote.setBidPrice(180d);
    quote.setAskPrice(190d);
    quote.setTicker("AAPL");
    quote.setLastPrice(189d);
    when(quoteDao.findById("AAPL")).thenReturn(Optional.of(quote));

    Account account = new Account();
    account.setAmount(1000d);
    account.setId(10);
    account.setTrader_id(10);
    when(accountDao.findById(10)).thenReturn(Optional.of(account));
    when(accountDao.save(any())).thenReturn(account);

    List<Position> positions = new ArrayList<>();
    Position position = new Position();
    position.setPosition(900);
    position.setTicker("AAPL");
    position.setAccountId(10);
    positions.add(position);
    when(positionDao.findWithSpecificColumn(any(),any())).thenReturn(positions);


    SecurityOrder securityOrder = new SecurityOrder();
    securityOrder.setStatus("FILLED");
    securityOrder.setId(58);
    securityOrder.setAccountId(10);
    securityOrder.setSize(900);
    securityOrder.setTicker("AAPL");
    securityOrder.setPrice(190d);
    when(securityOrderDao.save(any())).thenReturn(securityOrder);
  }

  @Test
  public void buy(){
    MarketOrderDto marketOrderDto = new MarketOrderDto();
    marketOrderDto.setAccountId(10);
    marketOrderDto.setSize(5);
    marketOrderDto.setTicker("AAPL");

    orderService.executeMarketOrder(marketOrderDto);
    verify(securityOrderDao).save(captorSecurityOrder.capture());
    assertEquals((Integer)58, captorSecurityOrder.getValue().getId());
  }

  @Test
  public void sell(){
    MarketOrderDto marketOrderDto = new MarketOrderDto();
    marketOrderDto.setAccountId(10);
    marketOrderDto.setSize(-5);
    marketOrderDto.setTicker("AAPL");

    orderService.executeMarketOrder(marketOrderDto);
    verify(securityOrderDao).save(captorSecurityOrder.capture());
    assertEquals((Integer) 58, captorSecurityOrder.getValue().getId());
  }


}