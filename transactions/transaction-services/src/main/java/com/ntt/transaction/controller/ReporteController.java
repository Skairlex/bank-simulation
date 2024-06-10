package com.ntt.transaction.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import com.ntt.transaction.service.IReportService;
import com.ntt.transaction.utils.BaseResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
@Slf4j
public class ReporteController {


    @Autowired
    private IReportService reportService;

    @GetMapping("/estado-cuenta")
    public ResponseEntity<BaseResponseVo> getAccountStatementReport(
        @RequestParam Long cuentaId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(reportService.generateAccountStatementReport(cuentaId, startDate, endDate));
    }

    @GetMapping("/estado-cuenta/pdf")
    public ResponseEntity<byte[]> getAccountStatementPdf(
        @RequestParam Long cuentaId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            ByteArrayInputStream pdf = reportService.generateAccountStatementPdf(cuentaId, startDate, endDate);
            byte[] pdfBytes = pdf.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "estado_cuenta.pdf");

            return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);

        } catch (IOException e) {
            log.error("Error getting PDF : ",e);
            return ResponseEntity.status(500).build();
        }
    }


}
