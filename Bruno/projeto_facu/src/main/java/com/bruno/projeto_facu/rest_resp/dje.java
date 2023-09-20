package com.bruno.projeto_facu.rest_resp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.projeto_facu.model.NOTIFICACAO;
import com.bruno.projeto_facu.model.PARTE;
import com.bruno.projeto_facu.rest_resp.objetos.noti_put;
import com.bruno.projeto_facu.jpa.jpaNOTIFICACAO;
import com.bruno.projeto_facu.jpa.jpaPARTE;

@RestController
@RequestMapping("/dje") 
public class dje { 

    @Autowired
    private jpaNOTIFICACAO repoN; 
    @Autowired
    private jpaPARTE repoP; 
    @GetMapping
    public List<NOTIFICACAO> listNotifications() { 
        return repoN.findje();
    }

    @PutMapping
    public ResponseEntity<String> editNotificationStatus(@RequestBody noti_put notificationPut) { 

        String newStatus = notificationPut.getStatus();
        int notificationId = notificationPut.getId_notificacao(); 
        NOTIFICACAO editedNotification = repoN.findnoti(notificationId);         String processNumber = editedNotification.getN_processo();
        String currentStatus = editedNotification.getStatuss();

        PARTE part = repoP.findparte(processNumber);         String cep = part.getCep();
        String number = part.getNumero();
        String email = part.getE_mail(); 
        if (editedNotification != null && cep == null && number == null && email == null && "pendente".equals(currentStatus)) {
            editedNotification.setStatuss(newStatus);
            repoN.save(editedNotification);

            return ResponseEntity.ok("Processo editado como enviado!");

        } else if (editedNotification != null && cep == null && number == null && email == null && "enviado".equals(currentStatus)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processo já foi enviado!");

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processo possui CEP, número ou email!");
        }
    }
}
