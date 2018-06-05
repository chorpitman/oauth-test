package com.lunapps.repository;

import com.lunapps.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT DISTINCT c FROM Category AS c LEFT JOIN FETCH c.subCategories")
    List<Category> getFetchedCategory();

    @Query("SELECT c FROM Category AS c LEFT JOIN FETCH c.subCategories WHERE c.id = :id")
    Category findCategoryById(@Param("id") final Long id);

}
