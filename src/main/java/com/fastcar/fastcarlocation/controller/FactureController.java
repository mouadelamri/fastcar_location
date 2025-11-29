package com.fastcar.fastcarlocation.controller;

import com.fastcar.fastcarlocation.service.FactureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Contrôleur REST pour la génération de factures.
 */
@RestController
@RequestMapping("/api/factures")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService factureService;

    /**
     * Génère une facture PDF pour un contrat.
     * 
     * @param contratId L'ID du contrat
     * @return Le PDF en tant que réponse HTTP
     */
    @GetMapping("/{contratId}/pdf")
    public ResponseEntity<byte[]> generateFacturePDF(@PathVariable Long contratId) {
        try {
            byte[] pdfContent = factureService.generateFacturePDF(contratId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "facture-LOC-" + contratId + ".pdf");
            headers.setContentLength(pdfContent.length);
            
            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Génère une facture HTML pour un contrat.
     * 
     * @param contratId L'ID du contrat
     * @return Le HTML en tant que réponse HTTP
     */
    @GetMapping(value = "/{contratId}/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> generateFactureHTML(@PathVariable Long contratId) {
        String htmlContent = factureService.generateFactureHTML(contratId);
        return ResponseEntity.ok(htmlContent);
    }
}

