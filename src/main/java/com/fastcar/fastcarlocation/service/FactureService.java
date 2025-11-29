package com.fastcar.fastcarlocation.service;

import com.fastcar.fastcarlocation.entity.Contrat;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

/**
 * Service pour générer des factures au format PDF et HTML.
 */
@Service
@RequiredArgsConstructor
public class FactureService {

    private final ContratService contratService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Génère une facture PDF pour un contrat donné.
     * 
     * @param contratId L'ID du contrat
     * @return Le contenu du PDF sous forme de tableau d'octets
     * @throws IOException En cas d'erreur lors de la génération du PDF
     */
    public byte[] generateFacturePDF(Long contratId) throws IOException {
        Contrat contrat = contratService.findByIdOrThrow(contratId);
        
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = 750;
                float margin = 50;
                float lineHeight = 20;
                float titleFontSize = 16;
                float normalFontSize = 12;
                float smallFontSize = 10;

                // Titre
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), titleFontSize);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("FACTURE DE LOCATION");
                contentStream.endText();
                yPosition -= lineHeight * 2;

                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), normalFontSize);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("AGENCE AUTO-LOC MAROC");
                contentStream.endText();
                yPosition -= lineHeight * 2;

                // Séparateur
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(550, yPosition);
                contentStream.stroke();
                yPosition -= lineHeight * 1.5f;

                // Section CONTRAT
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), normalFontSize);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("CONTRAT");
                contentStream.endText();
                yPosition -= lineHeight * 1.5f;

                addTextLine(contentStream, margin, yPosition, "N° Contrat : LOC-" + contrat.getNumContract(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Date début : " + contrat.getDateDebut().format(DATE_FORMATTER), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Date fin : " + contrat.getDateFin().format(DATE_FORMATTER), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Montant total : " + formatMontant(contrat.getMontantT()) + " MAD", normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Mode de paiement : " + contrat.getModePaiement().name(), normalFontSize);
                yPosition -= lineHeight * 2;

                // Section CLIENT
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), normalFontSize);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("CLIENT");
                contentStream.endText();
                yPosition -= lineHeight * 1.5f;

                addTextLine(contentStream, margin, yPosition, "CIN : " + contrat.getClient().getCin(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Nom : " + contrat.getClient().getNomCli(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Prénom : " + contrat.getClient().getPrenomCli(), normalFontSize);
                yPosition -= lineHeight;
                if (contrat.getClient().getAdresse() != null) {
                    addTextLine(contentStream, margin, yPosition, "Adresse : " + contrat.getClient().getAdresse(), normalFontSize);
                    yPosition -= lineHeight;
                }
                if (contrat.getClient().getTelephone() != null) {
                    addTextLine(contentStream, margin, yPosition, "Téléphone : " + contrat.getClient().getTelephone(), normalFontSize);
                    yPosition -= lineHeight;
                }
                if (contrat.getClient().getEmail() != null) {
                    addTextLine(contentStream, margin, yPosition, "Email : " + contrat.getClient().getEmail(), normalFontSize);
                    yPosition -= lineHeight;
                }
                yPosition -= lineHeight;

                // Section VOITURE
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), normalFontSize);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("VOITURE");
                contentStream.endText();
                yPosition -= lineHeight * 1.5f;

                addTextLine(contentStream, margin, yPosition, "Matricule : " + contrat.getVoiture().getMatricule(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Marque : " + contrat.getVoiture().getMarque(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Modèle : " + contrat.getVoiture().getModele(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Prix journalier : " + formatMontant(contrat.getVoiture().getPrixLoc()) + " MAD", normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Kilométrage : " + formatKilometrage(contrat.getVoiture().getKilometrage()) + " km", normalFontSize);
                yPosition -= lineHeight * 2;

                // Section AGENT
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), normalFontSize);
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText("AGENT");
                contentStream.endText();
                yPosition -= lineHeight * 1.5f;

                addTextLine(contentStream, margin, yPosition, "N° Agent : AG-" + contrat.getAgent().getNumAgent(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Nom : " + contrat.getAgent().getNomAgent(), normalFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Prénom : " + contrat.getAgent().getPrenomAgent(), normalFontSize);
                yPosition -= lineHeight * 2;

                // Pied de page
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(550, yPosition);
                contentStream.stroke();
                yPosition -= lineHeight * 1.5f;

                addTextLine(contentStream, margin, yPosition, "Siège social : Bd Mohammed V, Casablanca", smallFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Tél : 05 22 33 44 55 | RC : 123456", smallFontSize);
                yPosition -= lineHeight;
                addTextLine(contentStream, margin, yPosition, "Patente : 78901234 | IF : 98765432", smallFontSize);
                yPosition -= lineHeight * 2;

                addTextLine(contentStream, margin, yPosition, "Merci de votre confiance !", normalFontSize);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    /**
     * Génère une facture HTML pour un contrat donné.
     * 
     * @param contratId L'ID du contrat
     * @return Le HTML de la facture
     */
    public String generateFactureHTML(Long contratId) {
        Contrat contrat = contratService.findByIdOrThrow(contratId);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"fr\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Facture de Location - LOC-").append(contrat.getNumContract()).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 40px; }\n");
        html.append("        .header { text-align: center; margin-bottom: 30px; }\n");
        html.append("        .section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; }\n");
        html.append("        .section h3 { margin-top: 0; color: #333; }\n");
        html.append("        .footer { margin-top: 30px; padding-top: 20px; border-top: 2px solid #333; text-align: center; font-size: 12px; }\n");
        html.append("        .separator { border-top: 2px solid #333; margin: 20px 0; }\n");
        html.append("        table { width: 100%; border-collapse: collapse; }\n");
        html.append("        td { padding: 5px; }\n");
        html.append("        .label { font-weight: bold; width: 150px; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        // En-tête
        html.append("    <div class=\"header\">\n");
        html.append("        <h1>FACTURE DE LOCATION</h1>\n");
        html.append("        <h2>AGENCE AUTO-LOC MAROC</h2>\n");
        html.append("    </div>\n");
        html.append("    <div class=\"separator\"></div>\n");

        // Section CONTRAT
        html.append("    <div class=\"section\">\n");
        html.append("        <h3>CONTRAT</h3>\n");
        html.append("        <table>\n");
        html.append("            <tr><td class=\"label\">N° Contrat :</td><td>LOC-").append(contrat.getNumContract()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Date début :</td><td>").append(contrat.getDateDebut().format(DATE_FORMATTER)).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Date fin :</td><td>").append(contrat.getDateFin().format(DATE_FORMATTER)).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Montant total :</td><td>").append(formatMontant(contrat.getMontantT())).append(" MAD</td></tr>\n");
        html.append("            <tr><td class=\"label\">Mode de paiement :</td><td>").append(contrat.getModePaiement().name()).append("</td></tr>\n");
        html.append("        </table>\n");
        html.append("    </div>\n");

        // Section CLIENT
        html.append("    <div class=\"section\">\n");
        html.append("        <h3>CLIENT</h3>\n");
        html.append("        <table>\n");
        html.append("            <tr><td class=\"label\">CIN :</td><td>").append(contrat.getClient().getCin()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Nom :</td><td>").append(contrat.getClient().getNomCli()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Prénom :</td><td>").append(contrat.getClient().getPrenomCli()).append("</td></tr>\n");
        if (contrat.getClient().getAdresse() != null) {
            html.append("            <tr><td class=\"label\">Adresse :</td><td>").append(contrat.getClient().getAdresse()).append("</td></tr>\n");
        }
        if (contrat.getClient().getTelephone() != null) {
            html.append("            <tr><td class=\"label\">Téléphone :</td><td>").append(contrat.getClient().getTelephone()).append("</td></tr>\n");
        }
        if (contrat.getClient().getEmail() != null) {
            html.append("            <tr><td class=\"label\">Email :</td><td>").append(contrat.getClient().getEmail()).append("</td></tr>\n");
        }
        html.append("        </table>\n");
        html.append("    </div>\n");

        // Section VOITURE
        html.append("    <div class=\"section\">\n");
        html.append("        <h3>VOITURE</h3>\n");
        html.append("        <table>\n");
        html.append("            <tr><td class=\"label\">Matricule :</td><td>").append(contrat.getVoiture().getMatricule()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Marque :</td><td>").append(contrat.getVoiture().getMarque()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Modèle :</td><td>").append(contrat.getVoiture().getModele()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Prix journalier :</td><td>").append(formatMontant(contrat.getVoiture().getPrixLoc())).append(" MAD</td></tr>\n");
        html.append("            <tr><td class=\"label\">Kilométrage :</td><td>").append(formatKilometrage(contrat.getVoiture().getKilometrage())).append(" km</td></tr>\n");
        html.append("        </table>\n");
        html.append("    </div>\n");

        // Section AGENT
        html.append("    <div class=\"section\">\n");
        html.append("        <h3>AGENT</h3>\n");
        html.append("        <table>\n");
        html.append("            <tr><td class=\"label\">N° Agent :</td><td>AG-").append(contrat.getAgent().getNumAgent()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Nom :</td><td>").append(contrat.getAgent().getNomAgent()).append("</td></tr>\n");
        html.append("            <tr><td class=\"label\">Prénom :</td><td>").append(contrat.getAgent().getPrenomAgent()).append("</td></tr>\n");
        html.append("        </table>\n");
        html.append("    </div>\n");

        // Pied de page
        html.append("    <div class=\"separator\"></div>\n");
        html.append("    <div class=\"footer\">\n");
        html.append("        <p>Siège social : Bd Mohammed V, Casablanca</p>\n");
        html.append("        <p>Tél : 05 22 33 44 55 | RC : 123456</p>\n");
        html.append("        <p>Patente : 78901234 | IF : 98765432</p>\n");
        html.append("        <p><strong>Merci de votre confiance !</strong></p>\n");
        html.append("    </div>\n");

        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    /**
     * Ajoute une ligne de texte dans le PDF.
     */
    private void addTextLine(PDPageContentStream contentStream, float x, float y, String text, float fontSize) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    /**
     * Formate un montant avec séparateurs de milliers.
     */
    private String formatMontant(BigDecimal montant) {
        if (montant == null) return "0,00";
        return String.format("%,.2f", montant).replace(",", " ").replace(".", ",");
    }

    /**
     * Formate un kilométrage avec séparateurs de milliers.
     */
    private String formatKilometrage(Long kilometrage) {
        if (kilometrage == null) return "0";
        return String.format("%,d", kilometrage).replace(",", " ");
    }
}

