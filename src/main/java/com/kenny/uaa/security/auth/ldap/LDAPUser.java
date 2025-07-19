package com.kenny.uaa.security.auth.ldap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.security.core.userdetails.UserDetails;

import javax.naming.Name;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entry(objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"})
public class LDAPUser implements UserDetails {
    @Id
    @JsonIgnore
    private Name id;
    @Attribute(name = "uid")
    private Name username;
    @Attribute(name = "userPassword")
    private String password;
}
