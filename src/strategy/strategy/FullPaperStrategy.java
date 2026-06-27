package strategy;

// Esta classe concreta implementa a estratégia para o tipo de evento "Full Paper". Ela retorna o nome da categoria correspondente.

public class FullPaperStrategy implements TipoEventoStrategy {
    @Override
    public String getNomeCategoria() { return "Full Paper"; }
}
