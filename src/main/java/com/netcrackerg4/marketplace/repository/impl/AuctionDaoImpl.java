package com.netcrackerg4.marketplace.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netcrackerg4.marketplace.config.postgres_queries.AuctionQueries;
import com.netcrackerg4.marketplace.model.domain.CategoryEntity;
import com.netcrackerg4.marketplace.model.domain.auction.AuctionTypeEntity;
import com.netcrackerg4.marketplace.model.dto.auction.AuctionDto;
import com.netcrackerg4.marketplace.repository.interfaces.IAuctionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AuctionDaoImpl extends JdbcDaoSupport implements IAuctionDao {

    private final AuctionQueries queries;

    @Autowired
    public AuctionDaoImpl(DataSource ds, AuctionQueries queries) {
        this.queries = queries;
        super.setDataSource(ds);
    }

    @Override
    public void create(AuctionDto obj) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonDetails;
        try {
            jsonDetails = mapper.writeValueAsString(obj.getJsonDetails());
        }catch(JsonProcessingException e) {
            throw new IllegalStateException("Cannot parse jsonDetails at auctionDto");
        }

        getJdbcTemplate().update(queries.getCreate(), obj.getStartsAt(), obj.getStartPrice(),
                obj.getProductQuantity(), obj.getProductId(), jsonDetails, true, obj.getTypeId());
    }

    public Optional<String> getTypeNameById(int id){
        Optional<String> name;
        try {
            name = Optional.ofNullable(getJdbcTemplate()
                    .queryForObject(queries.getFetchTypeById(), String.class, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
        return name;
    }

    @Override
    public List<AuctionTypeEntity> getTypes() {
        List<AuctionTypeEntity> list;
        try{
            list = getJdbcTemplate().query(queries.getFetchTypes(),
                    (rs, row) -> AuctionTypeEntity.builder()
                            .typeId(rs.getInt("type_id"))
                            .name(rs.getString("auction_type_name"))
                            .build());
        } catch (EmptyResultDataAccessException e) {
            list = new ArrayList<>();
        }

        return list;
    }
}
