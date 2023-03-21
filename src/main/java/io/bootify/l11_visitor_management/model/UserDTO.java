package io.bootify.l11_visitor_management.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @Size(max = 255)
    private String email;

    @NotNull
    @Size(max = 255)
    private String phone;

    private Long flat;

    private Long address;

    @NotNull
    private Long role;

}
