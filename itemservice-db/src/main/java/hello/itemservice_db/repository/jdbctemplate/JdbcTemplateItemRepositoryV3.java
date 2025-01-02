package hello.itemservice_db.repository.jdbctemplate;

import hello.itemservice_db.domain.Item;
import hello.itemservice_db.repository.ItemRepository;
import hello.itemservice_db.repository.ItemSearchCond;
import hello.itemservice_db.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SimpleJdbcInsert
 */
@Slf4j
@Repository
public class JdbcTemplateItemRepositoryV3 implements ItemRepository {
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateItemRepositoryV3(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("item")
                .usingGeneratedKeyColumns("id");
//                .usingColumns("item_name", "price", "quantity"); // 생략 가능
    }

    @Override
    public Item save(Item item) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);

        Number key = jdbcInsert.executeAndReturnKey(param);

        item.setId(key.longValue());

        return item;
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "UPDATE item " +
                "SET item_name = :itemName, price = :price, quantity = :quantity " +
                "WHERE id = :id;";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", itemId)
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity());

        template.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "SELECT * FROM item WHERE id = :id;";

        try {
            Map<String, Object> param = Map.of("id", id);
            Item item = template.queryForObject(sql, param, itemRowMapper());

            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        // 동적 쿼리 생성 시작
        String sql = "SELECT * FROM item";

        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " WHERE";
        }

        boolean andFlag = false;
        if (StringUtils.hasText(itemName)) {
            sql += " item_name LIKE CONCAT('%', :itemName, '%')";
            andFlag = true;
        }

        if (maxPrice != null) {
            if (andFlag) {
                sql += " AND";
            }

            sql += " price <= :maxPrice";
        }

        sql += ";";
        // 동적 쿼리 생성 끝

        log.info("sql={}", sql);

        return template.query(sql, param, itemRowMapper());
    }

    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); // camel 변환 지원
    }
}
