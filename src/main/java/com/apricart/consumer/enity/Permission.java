package com.apricart.consumer.enity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created on October, 2022
 *
 * @author Kashaf Arshad
 */

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PERMISSION")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_name", unique = true)
    private String apiName;

    @Column(name = "api_url", unique = true)
    private String apiURL;

    @Column(name = "Active", nullable = false)
    private String active = "Y";

    @Column(name = "category", nullable = false)
    private String category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "permission")
    private List<RolePermissionMapper> rolePermissionMapper;

//	@Override
//	public String toString() {
//		return new com.google.gson.Gson().toJson(this);
//	}
}
