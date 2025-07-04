package com.kenny.uaa.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "Username length must be between 4 and 50 characters")
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String matchingPassword;
    @NotNull
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "Name length must be between 4 and 50 characters")
    private String name;
}
