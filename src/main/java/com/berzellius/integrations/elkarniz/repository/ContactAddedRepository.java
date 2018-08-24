package com.berzellius.integrations.elkarniz.repository;

import com.berzellius.integrations.elkarniz.dmodel.ContactAdded;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by berz on 25.06.2017.
 */
@Transactional(readOnly = true)
public interface ContactAddedRepository extends CrudRepository<ContactAdded, Long> {
    public Iterable<ContactAdded> findByState(ContactAdded.State state);

    public Iterable<ContactAdded> findByStateAndDtmCreateLessThan(ContactAdded.State state, Date dtmCreateLowBound);
}
