package com.RESTfull_API_CRUD_Example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.RESTfull_API_CRUD_Example.dto.Serdar;
import lombok.extern.slf4j.Slf4j;
//------lombok eklenecek, loglar lombok anatosyonu ile (@Slf4j) yapılacak
//veritabanı oluştur,pssql olacak. container olmalı(minikube yada docker)

@Slf4j
@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping("/haluk")
    public ResponseEntity<Serdar> getHaluk() {
        log.info("Health endpoint çağırıldı.");
        log.debug("Debug detayları: Sistem çalışıyor.");
        log.warn("Warn log çağırıldı.");
        log.error("Error log çağırıldı.");
        log.trace("Trace log çağırıldı.");
        Serdar serdar = new Serdar("Merhaba serdar");
        return ResponseEntity.ok(serdar);
    }
}
