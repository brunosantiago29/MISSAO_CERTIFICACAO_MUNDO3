package com.bruno.projeto_facu.rest_resp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.projeto_facu.model.NOTIFICACAO;
import com.bruno.projeto_facu.model.PARTE;
import com.bruno.projeto_facu.rest_resp.objetos.notificacao;
import com.bruno.projeto_facu.jpa.jpaNOTIFICACAO;
import com.bruno.projeto_facu.jpa.jpaPARTE;

@RestController
@RequestMapping("/notificacao")
public class post_notificacao {

    @Autowired
    private jpaPARTE repop;

    @Autowired
    private jpaNOTIFICACAO repon;

    @PostMapping
    public ResponseEntity<String> enviar(@RequestBody notificacao e_notificacao) {
        String n_processo = e_notificacao.getN_processo();
        String motivo = e_notificacao.getMotivo_notificacao();
        String status = "enviado";

        // parte
        PARTE pegar = repop.findparte(n_processo);
        String cep = pegar.getCep();
        String numero = pegar.getNumero();
        String email = pegar.getE_mail();

        try {
            if (email != null && cep == null && numero == null) {
                NOTIFICACAO notificacao = new NOTIFICACAO();
                notificacao.setN_processo(n_processo);
                notificacao.setStatuss(status);
                notificacao.setMotivo_de_notificacao(motivo);

                repon.save(notificacao);
                return ResponseEntity.ok("E-mail enviado");
            } else if (cep != null && numero != null) {
                NOTIFICACAO notificacao = new NOTIFICACAO();
                notificacao.setN_processo(n_processo);
                notificacao.setStatuss("pendente");
                notificacao.setMotivo_de_notificacao(motivo);

                repon.save(notificacao);
                return ResponseEntity.ok("Processo cadastrado e será enviado pelo e-cartas!");
            } else {
                NOTIFICACAO notificacao = new NOTIFICACAO();
                notificacao.setN_processo(n_processo);
                notificacao.setStatuss("pendente");
                notificacao.setMotivo_de_notificacao(motivo);

                repon.save(notificacao);
                return ResponseEntity.ok("Processo cadastrado e será enviado por DJe!");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar os dados");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro inesperado");
        }
    }
}
