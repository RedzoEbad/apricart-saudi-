package com.apricart.consumer.repository.elastic;


//import com.apricart.consumer.enity.Product;
//import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
//import org.springframework.data.elasticsearch.annotations.Query;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import java.util.List;

//public interface ProductSearchRepository extends ElasticsearchRepository<Product, Long> {
//
//
//    @Query("{\"bool\": {\"must\": [{\"match_phrase_prefix\": {\"title\": \"?0\"}}, {\"match\": {\"warehouseId\": \"?1\"}}]}}")
//    List<ProductDetailDTO> findByTitleAndWarehouseId(String titlePrefix, String warehouseId);
//
//}
