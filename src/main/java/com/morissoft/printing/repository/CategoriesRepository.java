package com.morissoft.printing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.morissoft.printing.db.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Long> {
}
