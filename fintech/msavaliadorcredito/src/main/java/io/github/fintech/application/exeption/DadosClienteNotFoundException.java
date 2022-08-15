package io.github.fintech.application.exeption;

public class DadosClienteNotFoundException extends Exception{

    public DadosClienteNotFoundException() {
        super("Dados do cliente não encontrados para o CPF informado!");
    }
}
