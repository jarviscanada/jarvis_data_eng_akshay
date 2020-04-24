package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.MarketOrderDto;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.Quote;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

  private AccountDao accountDao;
  private SecurityOrderDao securityOrderDao;
  private QuoteDao quoteDao;
  private PositionDao positionDao;

  @Autowired
  public OrderService(AccountDao accountDao, SecurityOrderDao securityOrderDao,
      QuoteDao quoteDao, PositionDao positionDao){
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.quoteDao = quoteDao;
    this.positionDao = positionDao;
  }


  public SecurityOrder executeMarketOrder(MarketOrderDto orderDto){
    String ticker = orderDto.getTicker().toUpperCase();
    Integer accountId = orderDto.getAccountId();

    Quote quote = quoteDao.findById(ticker).get();
    Account account = accountDao.findById(accountId).get();

    SecurityOrder securityOrder = new SecurityOrder();
    securityOrder.setSize(orderDto.getSize());
    securityOrder.setStatus("CREATED");
    securityOrder.setTicker(orderDto.getTicker());
    securityOrder.setAccountId(accountId);

    if (orderDto.getSize() > 0){
      securityOrder.setPrice(quote.getAskPrice());
      handleBuyMarketOrder(orderDto, securityOrder, account);
    } else if (orderDto.getSize() < 0){
      securityOrder.setPrice(quote.getBidPrice());
      handleSellMarketOrder(orderDto, securityOrder, account);
    } else {
      throw new IllegalArgumentException("Size of the order cannot be zero");
    }
    securityOrder.setStatus("FILLED");
    return securityOrder;
  }


  protected void handleBuyMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
      Account account){
    Double cost = securityOrder.getPrice() * securityOrder.getSize();
    if (cost > account.getAmount()){
      throw new IllegalArgumentException("Insufficient funds to cover this purchase");
    }
    account.setAmount(account.getAmount() - cost);
    securityOrder.setStatus("FILLED");
    accountDao.save(account);
    securityOrder.setId(securityOrderDao.save(securityOrder).getId());
  }


  protected void handleSellMarketOrder(MarketOrderDto marketOrderDto, SecurityOrder securityOrder,
      Account account){
    List<Position> positions = positionDao.findWithSpecificColumn(account.getId(), "account_id");
    if (positions == null){
      throw new IllegalArgumentException("There are no open positions in this account");
    }
    for (Position p : positions){
      if (p.getTicker() == marketOrderDto.getTicker().toUpperCase()){
        if (p.getPosition() > -marketOrderDto.getSize()){
          account.setAmount(account.getAmount() + securityOrder.getPrice() * securityOrder.getSize());
          securityOrder.setStatus("FILLED");
          accountDao.save(account);
          securityOrder.setId(securityOrderDao.save(securityOrder).getId());
        }
        else {
          throw new IllegalArgumentException("The order size is larger than the current position.");
        }
      }
    else{
      throw new IllegalArgumentException("You do not currently own any: " + marketOrderDto.getTicker());
    }

  }
}
}