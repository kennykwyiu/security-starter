package com.kenny.uaa.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto implements Serializable {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
