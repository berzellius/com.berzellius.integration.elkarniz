package com.berzellius.integrations.elkarniz.repository;

import com.berzellius.integrations.elkarniz.dmodel.Site;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by berz on 15.06.2016.
 */
@Transactional
public interface SiteRepository extends CrudRepository<Site, Long> {
    public List<Site> findByUrlAndPassword(String url, String password);
}
