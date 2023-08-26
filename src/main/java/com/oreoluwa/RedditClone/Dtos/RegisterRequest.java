package com.oreoluwa.RedditClone.Dtos;

import com.oreoluwa.RedditClone.CustomValidators.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    @ValidEmail
    private String email;
    private String password;
}
