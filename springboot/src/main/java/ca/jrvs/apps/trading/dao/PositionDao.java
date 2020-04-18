package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Position;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao extends JdbcCrudDao<Position> {

    JdbcTemplate template;
    final String TABLE_NAME = "position";
    final String ID_COLUMN = "account_id";

    @Autowired
    public PositionDao(DataSource ds) {
        template = new JdbcTemplate(ds);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return template;
    }

    @Override
    public SimpleJdbcInsert getSimpleJdbcInsert() {
        throw new UnsupportedOperationException("Position data is read-only");
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
    Class<Position> getEntityClass() {
        return Position.class;
    }


    @Override
    public <S extends Position> S save(S entity) {
        throw new UnsupportedOperationException("Position data is read-only");
    }

    @Override
    public <S extends Position> Iterable<S> saveAll(Iterable<S> entities) {
        throw new UnsupportedOperationException("Position data is read-only");
    }

    @Override
    public int updateOne(Position entity) {
        throw new UnsupportedOperationException("Position data is read-only");
    }


    @Override
    public void deleteById(Integer id) {
        throw new UnsupportedOperationException("Position data is read-only");
    }

    @Override
    public void delete(Position entity) {
        throw new UnsupportedOperationException("Position data is read-only");
    }

    @Override
    public void deleteAll(Iterable<? extends Position> entities) {
        throw new UnsupportedOperationException("Position data is read-only");
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Position data is read-only");
    }
}