package com.berzellius.integrations.elkarniz.repository;

import com.berzellius.integrations.elkarniz.dmodel.LeadFromSite;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by berz on 15.06.2016.
 */
@Transactional
public interface LeadFromSiteRepository extends CrudRepository<LeadFromSite, Long> {
    public Iterable<LeadFromSite> findByState(LeadFromSite.State state);
}
