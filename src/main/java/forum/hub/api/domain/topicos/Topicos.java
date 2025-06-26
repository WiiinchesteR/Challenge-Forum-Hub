package forum.hub.api.domain.topicos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Topicos(DadosEnviarTopico dados) {
        this.autor = dados.autor();
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.curso = dados.curso();
    }
}
