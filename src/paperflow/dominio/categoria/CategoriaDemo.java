package paperflow.dominio.categoria;

// Esta classe concreta implementa a estratégia para o tipo de evento "Demo". Ela retorna o nome da categoria correspondente.

public class CategoriaDemo implements CategoriaEvento {
    @Override
    public String getNomeCategoria() { return "Demo"; }
}
