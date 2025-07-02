package forum.hub.api.domain.respostas;

import forum.hub.api.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    List<Resposta> findAllByTopicoId(Long topicoId);

    int countByAutor(Usuario autor);

}
