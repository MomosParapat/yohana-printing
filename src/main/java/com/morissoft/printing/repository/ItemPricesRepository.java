package com.morissoft.printing.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.morissoft.printing.db.ItemPrices;

public interface ItemPricesRepository extends JpaRepository<ItemPrices, Long> {
	List<ItemPrices> findByItemId(Long itemId);

	@Query("SELECT u FROM ItemPrices u WHERE u.itemId=:itemId AND u.qtyFrom<=:qty AND u.qtyTo>=:qty AND type=:type")
	Optional<ItemPrices> findPriceByQtyInBetween(@Param("itemId") Long itemId, @Param("qty") Integer qty,
			@Param("type") String type);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ItemPrices c WHERE c.itemId=:itemId AND c.qtyFrom<=:qty AND c.qtyTo>=:qty AND type=:type")
	boolean isQuantityOverLapping(@Param("itemId") Long itemId, @Param("qty") Integer qty, @Param("type") String type);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ItemPrices c WHERE c.itemId=:itemId AND c.price=:price AND type=:type")
	boolean isPriceOverLapping(@Param("itemId") Long itemId, @Param("price") BigDecimal price,
			@Param("type") String type);
}
