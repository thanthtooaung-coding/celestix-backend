package com.movie.celestix.common.models;

import com.movie.celestix.common.converters.RoleConverter;
import com.movie.celestix.common.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User extends MasterData {

    private String name;

    private String password;

    @Column(unique = true)
    private String email;

    @Convert(converter = RoleConverter.class)
    private Role role;

    private String profileUrl;
}
