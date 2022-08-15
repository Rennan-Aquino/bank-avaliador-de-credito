package io.github.fintech.application;

import io.github.fintech.application.exeption.DadosClienteNotFoundException;
import io.github.fintech.application.exeption.ErroComunicacaoMicroservicesException;
import io.github.fintech.application.exeption.ErroSolicitacaoCartaoException;
import io.github.fintech.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("avaliacoes-credito")
public class AvaliadorCreditoController {

    private final AvaliadorCreditoService avaliadorCreditoService;

    @GetMapping
    public String status() {
        return "OKAY";
    }

    @GetMapping(value = "situacao-cliente", params = "cpf")
    public ResponseEntity consultarSituacaoCliente(@RequestParam("cpf") String cpf) {

        try {
            SituacaoCliente situacaoCliente = avaliadorCreditoService.obterSituacaoCliente(cpf);
            return ResponseEntity.ok(situacaoCliente);
        }
        catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (ErroComunicacaoMicroservicesException e) {
           return ResponseEntity.status(resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("")
    public ResponseEntity realizarAvaliacao(@RequestBody DadosAvaliacao dados) {

        try {
            RetornoAvaliacaoCliente retornoAvaliacaoCliente = avaliadorCreditoService.realizarAvaliacao(dados.getCpf(), dados.getRenda());
            return ResponseEntity.ok(retornoAvaliacaoCliente);
        }
        catch (DadosClienteNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        catch (ErroComunicacaoMicroservicesException e) {
            return ResponseEntity.status(resolve(e.getStatus())).body(e.getMessage());
        }
    }

    @PostMapping("solicitacoes-cartao")
    public ResponseEntity solicitarCartao(@RequestBody DadosSolicitacaoEmissaoCartao dados) {
        try {
            ProtocoloSolicitacaoCartao protocoloSolicitacaoCartao = avaliadorCreditoService.solicitarEmissaoCartao(dados);
            return ResponseEntity.ok(protocoloSolicitacaoCartao);
        }
        catch (ErroSolicitacaoCartaoException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
