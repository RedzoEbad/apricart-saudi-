package com.apricart.consumer.configuration;

//import com.apricart.consumer.service.Impl.SearchServiceImpl;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.opensearch.client.RestClient;
//import org.opensearch.client.RestClientBuilder;
//import org.opensearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * Created on August, 2024
 *
 * @author Kashaf Arshad
 */
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.apricart.consumer.repository.elastic")
//public class ElasticsearchConfig {
//	private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
//
//
//	@Value("${spring.elasticsearch.rest.uris}")
//	private String elasticsearchUris;
//
//	@Value("${spring.elasticsearch.rest.username}")
//	private String elasticsearchUsername;
//
//	@Value("${spring.elasticsearch.rest.password}")
//	private String elasticsearchPassword;
//
//	@Bean
//	public RestClientBuilder getRestClient() {
//		final CredentialsProvider credentialsProvider =
//				new BasicCredentialsProvider();
//		credentialsProvider.setCredentials(AuthScope.ANY,
//				new UsernamePasswordCredentials(elasticsearchUsername, elasticsearchPassword));
//
//        return RestClient.builder(
//						HttpHost.create(elasticsearchUris))
//				.setHttpClientConfigCallback(httpClientBuilder ->
//						httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//	}
//
//	@Bean
//	public RestHighLevelClient getRestHighLevelClient() {
//		return new RestHighLevelClient(getRestClient());
//	}
//
//}