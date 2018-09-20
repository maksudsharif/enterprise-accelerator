package com.maksudsharif.repository.model;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface RecordRepository extends JpaRepository<Record, Long>
{
    @Override
    @CacheEvict(cacheNames = { "Records" })
    <S extends Record> S save(S s);

    @Override
    @Cacheable(value = "Records")
    Optional<Record> findById(Long id);

    @Override
    @Cacheable(value = "Records", key = "#pageable.pageNumber + '.' + #pageable.pageSize + '.records'")
    Page<Record> findAll(Pageable pageable);

    @Override
    @Cacheable(value = "Records")
    Record getOne(Long aLong);
}
