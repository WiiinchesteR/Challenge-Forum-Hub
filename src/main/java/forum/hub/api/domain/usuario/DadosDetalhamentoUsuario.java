package forum.hub.api.domain.usuario;

import java.time.LocalDateTime;

public record DadosDetalhamentoUsuario(
        Long id, String nome, String email, LocalDateTime data
) {

    public DadosDetalhamentoUsuario(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getData()
        );
    }
}
