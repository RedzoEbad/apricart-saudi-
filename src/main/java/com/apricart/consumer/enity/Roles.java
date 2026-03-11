package com.apricart.consumer.enity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Created on October, 2022
 *
 * @author Kashaf Arshad
 */

@Entity
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "ROLES")
public class Roles extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String arabicName;

    @Column(nullable = false)
    private String active = "Y";

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private List<UserPortal> users;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private List<RolePermissionMapper> rolePermissionMapper;

//	@Override
//	public String toString() {
//		return new com.google.gson.Gson().toJson(this);
//	}
}
