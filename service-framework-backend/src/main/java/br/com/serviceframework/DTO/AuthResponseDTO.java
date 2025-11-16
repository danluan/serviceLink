package br.com.serviceframework.DTO;

public record AuthResponseDTO(
        UserDTO user,
        String token
) {

}
