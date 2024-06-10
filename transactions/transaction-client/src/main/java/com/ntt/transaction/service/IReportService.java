package com.ntt.transaction.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import com.ntt.transaction.utils.BaseResponseVo;

public interface IReportService {

    BaseResponseVo generateAccountStatementReport(Long cuentaId, LocalDate startDate, LocalDate endDate);

    ByteArrayInputStream generateAccountStatementPdf(Long cuentaId, LocalDate startDate, LocalDate endDate) throws IOException;
}
