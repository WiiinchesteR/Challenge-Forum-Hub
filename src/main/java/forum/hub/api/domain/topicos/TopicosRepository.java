package forum.hub.api.domain.topicos;

import forum.hub.api.domain.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicosRepository extends JpaRepository<Topico, Long> {

    int countByAutor(Usuario enviados);

}
