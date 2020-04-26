package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.SecurityOrder;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

  final String TABLE_NAME = "security_order";
  final String ID_COLUMN_NAME = "id";
  JdbcTemplate template;
  SimpleJdbcInsert simpleInsert;

  @Autowired
  public SecurityOrderDao(DataSource dataSource) {
    template = new JdbcTemplate(dataSource);
    simpleInsert = new SimpleJdbcInsert(template)
        .withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN_NAME);
  }

  @Override
  public JdbcTemplate getJdbcTemplate() {
    return template;
  }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() {
    return simpleInsert;
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public String getIdColumnName() {
    return ID_COLUMN_NAME;
  }

  @Override
  Class<SecurityOrder> getEntityClass() {
    return SecurityOrder.class;
  }

  @Override
  public int updateOne(SecurityOrder order) {
    String sql =
        "UPDATE " + getTableName() + " SET account_id=?, status=?, ticker=?, size=?, price=?, "
            + "notes=? WHERE id=?";
    return getJdbcTemplate().update(sql, order.getAccountId(), order.getStatus(),
        order.getTicker(), order.getSize(), order.getPrice(), order.getNotes(),
        order.getId());
  }

}