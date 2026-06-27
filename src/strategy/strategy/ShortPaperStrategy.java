package strategy;

// Esta classe concreta implementa a estratégia para o tipo de evento "Short Paper". Ela retorna o nome da categoria correspondente.

public class ShortPaperStrategy implements TipoEventoStrategy {
    @Override
    public String getNomeCategoria() { return "Short Paper"; }
}
