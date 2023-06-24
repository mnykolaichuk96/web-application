package pl.prz.mnykolaichuk.model.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private boolean emailVerified;

}
