package com.morissoft.printing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.morissoft.printing.db.Items;

public interface ItemsRepository extends JpaRepository<Items, Long> {
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Items c WHERE c.name = :name")
	boolean existByName(@Param("name") String name);

	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Items c WHERE c.code = :code")
	boolean existByCode(@Param("code") String code);

	Optional<Items> findByCode(String code);
	
	List<Items> findByStatusOrderByName(String status);
}
