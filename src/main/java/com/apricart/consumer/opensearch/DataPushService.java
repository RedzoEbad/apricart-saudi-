//package com.apricart.consumer.opensearch;
//
//import com.apricart.consumer.enity.Product;
//import com.apricart.consumer.enity.ProductWarehouse;
//import com.apricart.consumer.mapper.ProductMapper;
//import com.apricart.consumer.security.dto.dto.ProductDetailDTO;
//import com.apricart.consumer.security.enums.LanguageType;
//import com.apricart.consumer.service.ProductService;
//import com.apricart.consumer.service.ProductWarehouseService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import org.opensearch.action.get.GetRequest;
//import org.opensearch.action.get.GetResponse;
//import org.opensearch.action.update.UpdateRequest;
//import org.opensearch.client.RequestOptions;
//import org.opensearch.client.RestHighLevelClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//import static com.apricart.consumer.security.constants.ArabicResponseMessages.PRODUCT_NOT_FOUND_SEARCH_ARABIC;
//import static com.apricart.consumer.security.constants.ArabicResponseMessages.PRODUCT_WAREHOUSE_NULL_SEARCH_ARABIC;
//import static com.apricart.consumer.security.constants.ResponseMessage.PRODUCT_NOT_FOUND_SEARCH;
//import static com.apricart.consumer.security.constants.ResponseMessage.PRODUCT_WAREHOUSE_NULL_SEARCH;
//
//@Service
//public class DataPushService {
//
//    protected static final Logger LOGGER = LoggerFactory.getLogger(DataPushService.class);
//
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ProductWarehouseService productWarehouseService;
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    @Autowired
//    private RestHighLevelClient client;
//
//    @Autowired
//    private Gson gson;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    public static final String PRODUCT_INDEX = "product-index";
//
//
//    public void pushDataToOpenSearch(int pageSize, LanguageType languageType) {
//
//        Page<Product> productPage;
//        int pageNumber = 0;
//        do {
//            productPage = productService.findAll(PageRequest.of(pageNumber, pageSize));
//            for (Product product : productPage.getContent()) {
//                ProductWarehouse productWarehouse = productWarehouseService.findProductWarehouseByProductId(product.getId(), languageType);
//                ProductDetailDTO productDetailDTO = productMapper.mapToProductDetailDTO(productWarehouse, languageType);
//                indexProductData(productDetailDTO);
//            }
//            pageNumber++;
//        } while (pageNumber < productPage.getTotalPages());
//    }
//
//    private void indexProductData(ProductDetailDTO dto) {
//        Map<String, Object> sourceMap = objectMapper.convertValue(dto, Map.class);
//        UpdateRequest request = new UpdateRequest(PRODUCT_INDEX, dto.getId().toString())
//                .doc(sourceMap)
//                .upsert(sourceMap);
//        try {
//            client.update(request, RequestOptions.DEFAULT);
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//    }
//
//    public void syncDataById(Long productId, LanguageType languageType) {
//        ProductWarehouse productWarehouse = productWarehouseService.findProductWarehouseByProductId(productId, languageType);
//        if (productWarehouse != null) {
//            ProductDetailDTO dto = productMapper.mapToProductDetailDTO(productWarehouse, languageType);
//            indexProductData(dto);
//        } else {
//            throw new RuntimeException(LanguageType.ARB.equals(languageType) ? PRODUCT_WAREHOUSE_NULL_SEARCH_ARABIC : PRODUCT_WAREHOUSE_NULL_SEARCH);
//        }
//    }
//
//    public ProductDetailDTO getDataByProductId(Long productId, LanguageType languageType) {
//        try {
//            GetRequest getRequest = new GetRequest(PRODUCT_INDEX, productId.toString());
//            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
//
//            if (response.isExists()) {
//                String sourceAsString = response.getSourceAsString();
//                System.out.println("Document source: " + sourceAsString);
//                return convertJsonToProductDetailDTO(sourceAsString);
//            } else {
//                throw new RuntimeException(LanguageType.ARB.equals(languageType) ? PRODUCT_NOT_FOUND_SEARCH_ARABIC : PRODUCT_NOT_FOUND_SEARCH);
//            }
//
//        } catch (Exception e) {
//            LOGGER.error(e.getMessage());
//        }
//        return null;
//    }
//
//    public ProductDetailDTO convertJsonToProductDetailDTO(String json) {
//        return gson.fromJson(json, ProductDetailDTO.class);
//    }
//}