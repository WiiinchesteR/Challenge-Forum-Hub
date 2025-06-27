package forum.hub.api.topicos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Topico")
@Table(name = "topicos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String autor;
    private String titulo;
    private String mensagem;
    private String curso;

    private LocalDateTime data;

    public Topicos(DadosEnviarTopicos dados) {
        this.autor = dados.autor();
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.curso = dados.curso();
        this.data = LocalDateTime.now();
    }

    public void atualizarInformacoes(DadosDetalhamentoTopicos dados) {

        if (dados.autor() != null) {
            this.autor = dados.autor();
        }
        if (dados.titulo() != null) {
            this.titulo = dados.titulo();
        }
        if (dados.mensagem() != null) {
            this.mensagem = (dados.mensagem());
        }
        if (dados.curso() != null) {
            this.curso = (dados.curso());
        }
    }
}
