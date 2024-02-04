package com.walker.parkingmanagement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class JasperService {
    private final ResourceLoader resourceLoader;
    private final DataSource dataSource;

    private Map<String, Object> params = new HashMap<>();

    private static final String JASPER_DIRETORIO = "classpath:reports/";

    public void addParams(String key, Object value) {
        this.params.put("IMAGEM_DIRETORIO",JASPER_DIRETORIO);
        this.params.put("REPORT_LOCALE", new Locale("pt","BR"));
        this.params.put(key,value);
    }

    public byte[] generatePdf(){
        byte[] bytes = null;
        try {
            Resource resource = resourceLoader.getResource(JASPER_DIRETORIO.concat("parkinglots.jasper"));
            InputStream inputStream = resource.getInputStream();
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream,params, dataSource.getConnection());
            bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        }catch (IOException | JRException | SQLException exception){
            log.error("Jasper Reports : : : ", exception.getCause());
            throw new RuntimeException(exception);
        }
        return bytes;
    }
}
