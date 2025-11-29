package br.com.serviceframework.framework.domain.DTO;

public record AuthResponseDTO(
        UserDTO user,
        String token
) {

}
