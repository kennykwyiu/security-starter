package com.kenny.uaa.domain.dto;

import com.kenny.uaa.annotation.PasswordMatch;
import com.kenny.uaa.annotation.ValidEmail;
import com.kenny.uaa.annotation.ValidPassword;
import com.kenny.uaa.config.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@PasswordMatch
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "Username length must be between 4 and 50 characters")
    private String username;

    @NotNull
    @ValidPassword
    private String password;

    @NotNull
    private String matchingPassword;

    @NotNull
    @ValidEmail
    private String email;

    @NotNull
    @NotBlank
    @Size(min = 4, max = 50, message = "Name length must be between 4 and 50 characters")
    private String name;

    @Pattern(regexp = Constants.PATTERN_MOBILE)
    @NotNull
    private String mobile;
}
