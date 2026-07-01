package paperflow.dominio;

import java.util.HashSet;
import java.util.Set;

// Esta classe representa um usuário do sistema. Ela contém informações básicas como e-mail, senha, instituição e especialidades do usuário.

public class Usuario {
    private final String email;
    private final String senha;
    private final String instituicao;
    private final Set<String> especialidades;

    public Usuario(String email, String senha, String instituicao) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("E-mail é obrigatório.");
        this.email = email;
        this.senha = senha;
        this.instituicao = instituicao;
        this.especialidades = new HashSet<>();
    }

    public String getEmail() { return email; }
    public String getInstituicao() { return instituicao; }
    public Set<String> getEspecialidades() { return especialidades; }

    // Método para permitir que a Pessoa 2 cadastre áreas de expertise para este usuário virar revisor
    
    public void adicionarEspecialidade(String area) {
        this.especialidades.add(area.toLowerCase().trim());
    }
}
