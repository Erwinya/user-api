package com.RESTfull_API_CRUD_Example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RESTfull_API_CRUD_Example.dto.Serdar;

@RestController
//------lombok eklenecek, loglar lombok anatosyonu ile (@Slf4j) yapılacak
//veritabanı oluştur,pssql olacak. container olmalı(minikube yada docker)
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping("/haluk")
    public ResponseEntity<Serdar> getHaluk() {
        logger.info("Health enpoint çağırıldı.");
        logger.debug("Debug detayları: Sistem çalışıyor.");
        logger.warn("warn log çağırıldı");
        logger.error("error log çağırıldı");
        logger.trace("trace log çağırıldı");
        Serdar serdar = new Serdar("Merhaba serdar");
        return ResponseEntity.ok(serdar);
    }
}
