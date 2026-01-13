// Facture Functions
let currentContrat = null;

// Récupérer le numéro de contrat depuis l'URL
function getContratNumberFromURL() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('contrat');
}

// Charger les données de la facture
async function chargerFacture() {
    const numContrat = getContratNumberFromURL();
    
    if (!numContrat) {
        showAlert('Aucun contrat spécifié', 'error');
        setTimeout(() => window.location.href = 'contrats.html', 2000);
        return;
    }
    
    try {
        // Charger le contrat
        const response = await fetch(`${API_URL}/contrats/${numContrat}`);
        if (!response.ok) {
            throw new Error('Contrat non trouvé');
        }
        
        currentContrat = await response.json();
        
        // Mettre à jour les informations
        updateFactureInfo();
        
        // Charger les informations complémentaires
        await loadAdditionalInfo();
        
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors du chargement de la facture', 'error');
        setTimeout(() => window.location.href = 'contrats.html', 2000);
    }
}

// Mettre à jour les informations de la facture
function updateFactureInfo() {
    if (!currentContrat) return;
    
    document.getElementById('facture-num').textContent = currentContrat.numContrat;
    document.getElementById('facture-debut').textContent = formatDate(currentContrat.dateDebut);
    document.getElementById('facture-fin').textContent = formatDate(currentContrat.dateFin);
    document.getElementById('facture-montant').textContent = currentContrat.montantT.toFixed(2);
    document.getElementById('facture-paiement').textContent = currentContrat.modePaiement;
}

// Charger les informations additionnelles
async function loadAdditionalInfo() {
    if (!currentContrat) return;
    
    try {
        // Charger le client
        const clientResponse = await fetch(`${API_URL}/clients/${currentContrat.cinCliFk}`);
        if (clientResponse.ok) {
            const client = await clientResponse.json();
            document.getElementById('facture-cin').textContent = client.cinCli;
            document.getElementById('facture-nom').textContent = client.nomCli;
            document.getElementById('facture-prenom').textContent = client.prenomCli;
            document.getElementById('facture-adresse').textContent = client.adresse || 'Non spécifiée';
            document.getElementById('facture-tel').textContent = client.telephone || 'Non spécifié';
            document.getElementById('facture-email').textContent = client.email || 'Non spécifié';
        }
        
        // Charger la voiture
        const voitureResponse = await fetch(`${API_URL}/voitures/${currentContrat.matriculeFk}`);
        if (voitureResponse.ok) {
            const voiture = await voitureResponse.json();
            document.getElementById('facture-matricule').textContent = voiture.matricule;
            document.getElementById('facture-marque').textContent = voiture.marque;
            document.getElementById('facture-modele').textContent = voiture.modele;
            document.getElementById('facture-prix').textContent = voiture.prixLoc.toFixed(2);
            document.getElementById('facture-km').textContent = voiture.kilometrage ? voiture.kilometrage.toLocaleString() : 'N/A';
            document.getElementById('facture-etat').textContent = voiture.etatVeh;
        }
        
        // Charger l'agent
        const agentResponse = await fetch(`${API_URL}/agents/${currentContrat.numAgentFk}`);
        if (agentResponse.ok) {
            const agent = await agentResponse.json();
            document.getElementById('facture-agent-num').textContent = agent.numAgent;
            document.getElementById('facture-agent-nom').textContent = agent.nomAgent;
            document.getElementById('facture-agent-prenom').textContent = agent.prenomAgent;
        }
        
    } catch (error) {
        console.error('Erreur lors du chargement des informations:', error);
    }
}

// Imprimer la facture
function imprimerFacture() {
    window.print();
}

// Générer un PDF
async function genererPDF() {
    const { jsPDF } = window.jspdf;
    
    try {
        // Créer un nouveau PDF
        const doc = new jsPDF('p', 'mm', 'a4');
        
        // Titre
        doc.setFontSize(20);
        doc.setTextColor(40, 40, 40);
        doc.text('FACTURE DE LOCATION', 105, 20, { align: 'center' });
        
        doc.setFontSize(16);
        doc.text('AGENCE FASTCAR LOCATION', 105, 30, { align: 'center' });
        
        // Informations de l'agence
        doc.setFontSize(10);
        doc.setTextColor(100, 100, 100);
        doc.text('Bd Mohammed V, Casablanca | Tél: 05 22 33 44 55', 105, 38, { align: 'center' });
        doc.text('RC: 123456 | Patente: 78901234 | IF: 98765432', 105, 43, { align: 'center' });
        
        // Ligne de séparation
        doc.setDrawColor(200, 200, 200);
        doc.setLineWidth(0.5);
        doc.line(20, 50, 190, 50);
        
        let yPosition = 60;
        
        // Section CONTRAT
        doc.setFontSize(14);
        doc.setTextColor(40, 40, 40);
        doc.text('CONTRAT', 20, yPosition);
        
        doc.setFontSize(10);
        doc.setTextColor(80, 80, 80);
        yPosition += 10;
        doc.text(`N° Contrat : ${document.getElementById('facture-num').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Date début : ${document.getElementById('facture-debut').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Date fin : ${document.getElementById('facture-fin').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Montant total : ${document.getElementById('facture-montant').textContent} MAD`, 20, yPosition);
        yPosition += 7;
        doc.text(`Mode de paiement : ${document.getElementById('facture-paiement').textContent}`, 20, yPosition);
        
        // Section CLIENT
        yPosition += 15;
        doc.setFontSize(14);
        doc.setTextColor(40, 40, 40);
        doc.text('CLIENT', 20, yPosition);
        
        doc.setFontSize(10);
        doc.setTextColor(80, 80, 80);
        yPosition += 10;
        doc.text(`CIN : ${document.getElementById('facture-cin').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Nom : ${document.getElementById('facture-nom').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Prénom : ${document.getElementById('facture-prenom').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Adresse : ${document.getElementById('facture-adresse').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Téléphone : ${document.getElementById('facture-tel').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Email : ${document.getElementById('facture-email').textContent}`, 20, yPosition);
        
        // Nouvelle page si nécessaire
        if (yPosition > 250) {
            doc.addPage();
            yPosition = 20;
        }
        
        // Section VOITURE
        yPosition += 15;
        doc.setFontSize(14);
        doc.setTextColor(40, 40, 40);
        doc.text('VOITURE', 20, yPosition);
        
        doc.setFontSize(10);
        doc.setTextColor(80, 80, 80);
        yPosition += 10;
        doc.text(`Matricule : ${document.getElementById('facture-matricule').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Marque : ${document.getElementById('facture-marque').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Modèle : ${document.getElementById('facture-modele').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Prix journalier : ${document.getElementById('facture-prix').textContent} MAD`, 20, yPosition);
        yPosition += 7;
        doc.text(`Kilométrage : ${document.getElementById('facture-km').textContent} km`, 20, yPosition);
        yPosition += 7;
        doc.text(`État : ${document.getElementById('facture-etat').textContent}`, 20, yPosition);
        
        // Section AGENT
        yPosition += 15;
        doc.setFontSize(14);
        doc.setTextColor(40, 40, 40);
        doc.text('AGENT', 20, yPosition);
        
        doc.setFontSize(10);
        doc.setTextColor(80, 80, 80);
        yPosition += 10;
        doc.text(`N° Agent : ${document.getElementById('facture-agent-num').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Nom : ${document.getElementById('facture-agent-nom').textContent}`, 20, yPosition);
        yPosition += 7;
        doc.text(`Prénom : ${document.getElementById('facture-agent-prenom').textContent}`, 20, yPosition);
        
        // Cachet et signatures
        yPosition += 20;
        doc.setFontSize(12);
        doc.setTextColor(40, 40, 40);
        doc.text('Cachet et signature de l\'agence :', 20, yPosition);
        
        yPosition += 20;
        doc.setFontSize(10);
        doc.text('_________________________', 20, yPosition);
        doc.text('Signature du Client', 20, yPosition + 5);
        
        doc.text('_________________________', 120, yPosition);
        doc.text('Signature de l\'Agent', 120, yPosition + 5);
        
        // Pied de page
        const pageHeight = doc.internal.pageSize.height;
        doc.setFontSize(10);
        doc.setTextColor(100, 100, 100);
        doc.text('Merci de votre confiance !', 105, pageHeight - 30, { align: 'center' });
        doc.text('Pour toute réclamation, contactez-nous au 05 22 33 44 55', 105, pageHeight - 25, { align: 'center' });
        doc.text('Email: contact@fastcar-location.ma | Site: www.fastcar-location.ma', 105, pageHeight - 20, { align: 'center' });
        
        // Sauvegarder le PDF
        const fileName = `Facture_${currentContrat.numContrat}_${new Date().toISOString().split('T')[0]}.pdf`;
        doc.save(fileName);
        
        showAlert('PDF téléchargé avec succès!', 'success');
        
    } catch (error) {
        console.error('Erreur lors de la génération du PDF:', error);
        showAlert('Erreur lors de la génération du PDF', 'error');
    }
}

// Formater la date
function formatDate(dateString) {
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    } catch (error) {
        return dateString;
    }
}

// Initialiser la facture
document.addEventListener('DOMContentLoaded', chargerFacture);