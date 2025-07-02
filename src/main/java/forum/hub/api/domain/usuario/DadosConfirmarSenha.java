package forum.hub.api.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosConfirmarSenha(@NotBlank String senha) {
}
