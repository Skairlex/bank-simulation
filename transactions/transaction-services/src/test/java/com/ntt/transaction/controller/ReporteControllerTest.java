package com.ntt.transaction.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import com.ntt.transaction.service.IReportService;
import com.ntt.transaction.utils.BaseResponseVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
class ReporteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IReportService reportService;

    @InjectMocks
    private ReporteController reportController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    public void testGetAccountStatementReport() throws Exception {
        Long cuentaId = 1L;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        BaseResponseVo expectedResponse = BaseResponseVo.builder().message("Report generated successfully").build();

        Mockito.when(reportService.generateAccountStatementReport(cuentaId, startDate, endDate)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/reportes/estado-cuenta")
                .param("cuentaId", cuentaId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"message\":\"Report generated successfully\"}"));
    }

    @Test
    public void testGetAccountStatementPdf() throws Exception {
        Long cuentaId = 1L;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        ByteArrayInputStream pdfStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "estado_cuenta.pdf");

        Mockito.when(reportService.generateAccountStatementPdf(cuentaId, startDate, endDate)).thenReturn(pdfStream);

        mockMvc.perform(get("/api/reportes/estado-cuenta/pdf")
                .param("cuentaId", cuentaId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_PDF))
            .andExpect(result -> {
                byte[] content = result.getResponse().getContentAsByteArray();
                assertEquals(3, content.length);
            });
    }
}
