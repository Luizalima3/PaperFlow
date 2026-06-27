package strategy;

// Esta classe concreta implementa a estratégia para o tipo de evento "Demo". Ela retorna o nome da categoria correspondente.

public class DemoStrategy implements TipoEventoStrategy {
    @Override
    public String getNomeCategoria() { return "Demo"; }
}
