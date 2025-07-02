package forum.hub.api.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DadosAtualizarSenha(@NotBlank String novaSenha) {
}
