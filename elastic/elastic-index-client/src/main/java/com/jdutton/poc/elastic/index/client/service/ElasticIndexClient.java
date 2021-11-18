package com.jdutton.poc.elastic.index.client.service;

import com.jdutton.poc.elastic.model.index.IndexModel;

import java.util.List;

public interface ElasticIndexClient <T extends IndexModel>{
    List<String> save(List<T> documents);
}
