//package com.apricart.consumer.enity;
//
//import com.apricart.consumer.security.dto.request.SearchTagRequestDTO;
//import com.apricart.consumer.security.dto.response.SearchTagResponseDTO;
//import lombok.*;
//import org.hibernate.search.annotations.Indexed;
//import org.hibernate.search.annotations.IndexedEmbedded;
//import org.hibernate.search.annotations.SortableField;
//
//import javax.persistence.*;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Getter
//@Setter
//@Entity
//@Builder
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "search_tag")
//@Indexed
//public class SearchTag {
//
//    @SortableField
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column
//    private Boolean status;
//
//    @ElementCollection
//    @CollectionTable(name = "TAG_LIST", joinColumns = @JoinColumn(name = "search_tag_id"))
//    @Column(name = "tag")
//    private List<String> tags;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id")
//    @IndexedEmbedded
//    private Product product;
//
//    public static SearchTagResponseDTO toDTO(SearchTag searchTag) {
//        return SearchTagResponseDTO
//                .builder()
//                .id(searchTag.getId())
//                .productId(searchTag.getProduct().getId())
//                .tags(searchTag.getTags())
//                .status(searchTag.getStatus()).build();
//    }
//
//    public static List<SearchTagResponseDTO> toDTOList(List<SearchTag> searchTags) {
//        return searchTags.stream().map(SearchTag::toDTO).collect(Collectors.toList());
//    }
//
//    public static SearchTag fromDTO(SearchTagRequestDTO dto) {
//        return SearchTag.builder().tags(dto.getTags()).status(dto.getStatus()).build();
//    }
//
//}
