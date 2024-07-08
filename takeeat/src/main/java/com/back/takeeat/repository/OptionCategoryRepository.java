package com.back.takeeat.repository;

import com.back.takeeat.domain.option.OptionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long> {
}
