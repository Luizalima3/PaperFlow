package br.edu.ifpb.paperflow.dominio.categoria;

// Esta classe concreta implementa a estratégia para o tipo de evento "Short Paper". Ela retorna o nome da categoria correspondente.

public class CategoriaShortPaper implements CategoriaEvento {
    @Override
    public String getNomeCategoria() { return "Short Paper"; }
}
