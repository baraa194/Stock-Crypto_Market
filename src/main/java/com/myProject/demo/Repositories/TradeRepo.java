package com.myProject.demo.Repositories;

import com.myProject.demo.DTO.BuyTradeResponse;
import com.myProject.demo.Enums.TradeType;
import com.myProject.demo.Models.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TradeRepo extends JpaRepository<Trade, Long> {

    @Query("select new com.myProject.demo.DTO.BuyTradeResponse(t.quantity,t.asset.name,t.user.username" +
            ", t.portfolio.id,t.PNL) " +
            "from Trade t where t.portfolio.id =:portfolioid and t.type = :type")
    List<BuyTradeResponse> findBuytradesByPortfolioId(@Param("portfolioid") Long id, @Param("type") TradeType type);

    @Query("select t from Trade t where t.portfolio.id = :portfolioid and t.type = :type")
    List<Trade> findTradesByPortfolioAndType(@Param("portfolioid") Long portfolioid,
                                             @Param("type") TradeType type);

}