package com.jdutton.poc.elastic.index.client.service.impl;

import com.jdutton.poc.config.ElasticConfigData;
import com.jdutton.poc.elastic.index.client.repository.TwitterElasticsearchIndexRepository;
import com.jdutton.poc.elastic.index.client.service.ElasticIndexClient;
import com.jdutton.poc.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@Service
//@ConditionalOnProperty(name="elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);

    private final TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository;
    private final ElasticConfigData elasticConfigData;

    public TwitterElasticRepositoryIndexClient(TwitterElasticsearchIndexRepository twitterElasticsearchIndexRepository,
                                               ElasticConfigData elasticConfigData) {
        super();
        this.twitterElasticsearchIndexRepository = twitterElasticsearchIndexRepository;
        this.elasticConfigData = elasticConfigData;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repositoryResponse =
                (List<TwitterIndexModel>) twitterElasticsearchIndexRepository.saveAll(documents);
        List<String> documentIds = repositoryResponse.stream().map(TwitterIndexModel::getId).collect(Collectors.toList());
        LOG.info("Repository option: Documents indexed successfully with type: {} and ids: {}", TwitterIndexModel.class.getName(),
                documentIds);
        return documentIds;
    }
}
