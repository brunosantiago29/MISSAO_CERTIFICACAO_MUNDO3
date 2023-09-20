package com.bruno.projeto_facu.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bruno.projeto_facu.model.NOTIFICACAO;


public interface jpaNOTIFICACAO extends JpaRepository<NOTIFICACAO,Integer>{
       
    @Query("SELECT n FROM NOTIFICACAO n JOIN PARTE p ON n.n_processo = p.n_processo " +
           "WHERE p.cep IS NULL AND p.numero IS NULL AND p.e_mail IS NULL AND n.statuss = 'pendente' AND p.parte = 'reu'")
    List<NOTIFICACAO> findje();

    @Query("SELECT n FROM NOTIFICACAO n JOIN PARTE p ON n.n_processo = p.n_processo " +
           "WHERE p.cep IS NOT NULL AND p.numero IS NOT NULL AND n.statuss = 'pendente' AND p.parte = 'reu'")
    List<NOTIFICACAO> findcep();

    @Query("SELECT n FROM NOTIFICACAO n WHERE n.id_notificacao = :id")
    NOTIFICACAO findnoti(@Param("id") int id);
}
