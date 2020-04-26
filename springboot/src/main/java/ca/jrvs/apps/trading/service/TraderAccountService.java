package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.domain.Account;
import ca.jrvs.apps.trading.model.domain.Position;
import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import ca.jrvs.apps.trading.model.domain.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraderAccountService {

  private TraderDao traderDao;
  private AccountDao accountDao;
  private SecurityOrderDao securityOrderDao;
  private PositionDao positionDao;

  @Autowired
  public TraderAccountService(TraderDao traderDao, AccountDao accountDao,
      SecurityOrderDao securityOrderDao, PositionDao positionDao) {
    this.traderDao = traderDao;
    this.accountDao = accountDao;
    this.securityOrderDao = securityOrderDao;
    this.positionDao = positionDao;
  }


  public TraderAccountView createTraderAndAccount(Trader trader) throws IllegalArgumentException {
    if (trader == null) {
      throw new IllegalArgumentException("The trader cannot be null.");
    }
    if (trader.getCountry() == null) {
      throw new IllegalArgumentException("The trader's Country cannot be null.");
    }
    if (trader.getDob() == null) {
      throw new IllegalArgumentException("The trader's Date of birth cannot be null.");
    }
    if (trader.getEmail() == null) {
      throw new IllegalArgumentException("The trader's Email cannot be null.");
    }
    if (trader.getFirstName() == null) {
      throw new IllegalArgumentException("The trader's First Name cannot be null.");
    }
    if (trader.getLastName() == null) {
      throw new IllegalArgumentException("The trader's Last Name cannot be null.");
    }
    if (trader.getId() != null) {
      throw new IllegalArgumentException("The trader's ID should be null.");
    }
    int id = traderDao.save(trader).getId();
    trader.setId(id);
    Account account = new Account();
    account.setTrader_id(id);
    account.setAmount(0d);
    account.setId(accountDao.save(account).getId());
    TraderAccountView traderAccountView = new TraderAccountView();
    traderAccountView.setTrader(trader);
    traderAccountView.setAccount(account);
    return traderAccountView;
  }

  public void deleteTraderById(int id) {
    Trader trader = traderDao.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Trader with that ID does not exist")
    );
    Account account = accountDao.findById(id).orElseThrow(
        () -> new EntityNotFoundException("Account with that ID does not exist")
    );
    List<SecurityOrder> securityOrders = securityOrderDao
        .findWithSpecificColumn(account.getId(), "account_id");
    List<Position> accountPositions = getAccountPositions(id);
    if (account.getAmount() != 0 || hasOpenPositions(accountPositions)) {
      throw new IllegalStateException("Account either has a balance or open position(s)");
    } else {
      if (securityOrders.size() != 0) {
        for (SecurityOrder securityOrder : securityOrders) {
          securityOrderDao.deleteById(securityOrder.getId());
        }
      }
      accountDao.deleteById(account.getId());
      traderDao.deleteById(trader.getId());
    }
  }

  private List<Position> getAccountPositions(int id) {
    List<Position> positions = new ArrayList<>();
    for (Position position : positionDao.findAll()) {
      if (id == position.getId()) {
        positions.add(position);
      }
    }
    return positions;
  }

  private boolean hasOpenPositions(List<Position> positionList) {
    for (Position position : positionList) {
      if (position.getPosition() != 0) {
        return true;
      }
    }
    return false;
  }


  public Account deposit(int accountId, double depositAmount) {
    if (depositAmount <= 0) {
      throw new IllegalArgumentException("The deposit amount cannot be empty");
    }
    Optional<Account> accountOpt = accountDao.findById(accountId);
    Account account;
    if (accountOpt.isPresent()) {
      account = accountOpt.get();
      account.setAmount(account.getAmount() + depositAmount);
      accountDao.updateOne(account);
      return account;
    } else {
      throw new EntityNotFoundException("No account with that ID exists");
    }
  }


  public Account withdraw(int accountId, double withdrawAmount) {
    if (withdrawAmount <= 0) {
      throw new IllegalArgumentException("Funds to withdraw must be greater than 0");
    }
    Optional<Account> accountOpt = accountDao.findById(accountId);
    Account account;
    if (accountOpt.isPresent()) {
      account = accountOpt.get();
      if (withdrawAmount > account.getAmount()) {
        throw new IllegalArgumentException("Withdrawal amount cannot exceed balance");
      }
      account.setAmount(account.getAmount() - withdrawAmount);
      accountDao.updateOne(account);
      return account;
    } else {
      throw new EntityNotFoundException("No account with that ID exists");
    }

  }


  public TraderDao getTraderDao() {
    return traderDao;
  }

  public void setTraderDao(TraderDao traderDao) {
    this.traderDao = traderDao;
  }

  public AccountDao getAccountDao() {
    return accountDao;
  }

  public void setAccountDao(AccountDao accountDao) {
    this.accountDao = accountDao;
  }

  public SecurityOrderDao getSecurityOrderDao() {
    return securityOrderDao;
  }

  public void setSecurityOrderDao(SecurityOrderDao securityOrderDao) {
    this.securityOrderDao = securityOrderDao;
  }

  public PositionDao getPositionDao() {
    return positionDao;
  }

  public void setPositionDao(PositionDao positionDao) {
    this.positionDao = positionDao;
  }
}