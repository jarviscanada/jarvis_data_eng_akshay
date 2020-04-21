package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Account;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends JdbcCrudDao<Account>{


    private final String TABLE_NAME = "account";
    private final String ID_COLUMN = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public AccountDao(DataSource dataSource){
        this.jdbcTemplate=new JdbcTemplate(dataSource);
        this.simpleJdbcInsert= new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME).usingGeneratedKeyColumns(ID_COLUMN);
    }


    @Override
    public int updateOne(Account entity) {
        String updateSQL = "UPDATE " + getTABLE_NAME() + " SET amount=?";
        return getJdbcTemplate().update(updateSQL, entity.getAmount());
    }



    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getIdColumnName() {
        return ID_COLUMN;
    }

    @Override
    Class<Account> getEntityClass() {
        return Account.class;
    }


    public String getTABLE_NAME() {
        return TABLE_NAME;
    }


    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }


    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        return simpleJdbcInsert;
    }

}