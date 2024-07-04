package com.back.takeeat.repository;

import com.back.takeeat.domain.option.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {

    List<Option> findByIdIn(List<Long> optionIds);

}
