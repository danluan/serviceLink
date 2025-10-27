package br.com.servicelink.DTO;

import br.com.servicelink.entity.User;

public record AuthResponseDTO(
        UserDTO user,
        String token
) {

}
