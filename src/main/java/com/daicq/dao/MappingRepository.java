package com.daicq.dao;

import com.daicq.dao.doc.MappingDoc;
import org.springframework.data.repository.CrudRepository;

public interface MappingRepository extends CrudRepository<MappingDoc, String> {

}
