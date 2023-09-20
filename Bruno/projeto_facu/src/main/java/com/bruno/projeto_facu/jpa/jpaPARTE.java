package com.bruno.projeto_facu.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bruno.projeto_facu.model.PARTE;

public interface jpaPARTE extends JpaRepository<PARTE,Integer>{
    @Query("SELECT p FROM PARTE p WHERE p.n_processo = :processo AND p.parte = 'reu'")
    PARTE findparte(@Param("processo") String processo);
}
