package com.bruno.projeto_facu.rest_resp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bruno.projeto_facu.model.NOTIFICACAO;
import com.bruno.projeto_facu.model.PARTE;
import com.bruno.projeto_facu.rest_resp.objetos.noti_put;
import com.bruno.projeto_facu.jpa.jpaNOTIFICACAO;
import com.bruno.projeto_facu.jpa.jpaPARTE;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cep") 
public class cep { 

    @Autowired
    private jpaNOTIFICACAO repoN; 

    @Autowired
    private jpaPARTE repoP; 

    @GetMapping
    public List<NOTIFICACAO> listNotifications() { 

        List<NOTIFICACAO> notificationsList = repoN.findcep();
        List<NOTIFICACAO> pendingNotifications = new ArrayList<>();

        for (NOTIFICACAO notification : notificationsList) {
            // Notification data
            int idNotification = notification.getId_notificacao();
            String processNumber = notification.getN_processo();
            String status = notification.getStatuss();
            String reason = notification.getMotivo_de_notificacao();

            NOTIFICACAO cepNotification = new NOTIFICACAO();
            cepNotification.setId_notificacao(idNotification);
            cepNotification.setN_processo(processNumber);
            cepNotification.setStatuss(status);
            cepNotification.setMotivo_de_notificacao(reason);

            pendingNotifications.add(cepNotification);
        }

        return pendingNotifications;
    }

    @PutMapping
    public ResponseEntity<String> editNotificationStatus(@RequestBody noti_put notificationPut) {
        String newStatus = notificationPut.getStatus();
        int notificationId = notificationPut.getId_notificacao(); 

        NOTIFICACAO editedNotification = repoN.findnoti(notificationId); 
        String processNumber = editedNotification.getN_processo();
        String currentStatus = editedNotification.getStatuss();

        PARTE part = repoP.findparte(processNumber); 
        String cep = part.getCep();
        String number = part.getNumero();

        if (editedNotification != null && cep != null && number != null && "pendente".equals(currentStatus)) {
            editedNotification.setStatuss(newStatus);
            repoN.save(editedNotification);

            return ResponseEntity.ok("Processo editado como enviado!");

        } else if (editedNotification != null && cep != null && number != null && "enviado".equals(currentStatus)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processo já foi enviado!");

        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Processo não possui CEP ou número!");
        }
    }
}
