// Contrats Functions
let agentsList = [];
let voituresList = [];
let clientsList = [];

async function generateContratNumber() {
    try {
        const response = await fetch(`${API_URL}/contrats/next-numero`);
        const data = await response.json();
        document.getElementById('numContrat').value = data.nextNumero;
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la génération du numéro', 'error');
    }
}

async function loadAgents() {
    try {
        const response = await fetch(`${API_URL}/agents`);
        agentsList = await response.json();
        
        const select = document.getElementById('numAgent');
        select.innerHTML = '<option value="">Sélectionner un agent...</option>';
        
        agentsList.forEach(agent => {
            const option = document.createElement('option');
            option.value = agent.numAgent;
            option.textContent = `${agent.numAgent} - ${agent.nomAgent} ${agent.prenomAgent}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erreur:', error);
    }
}

async function loadVoituresDisponibles() {
    try {
        const response = await fetch(`${API_URL}/voitures/disponibles`);
        voituresList = await response.json();
        
        const select = document.getElementById('matricule');
        select.innerHTML = '<option value="">Sélectionner une voiture...</option>';
        
        voituresList.forEach(voiture => {
            const option = document.createElement('option');
            option.value = voiture.matricule;
            option.textContent = `${voiture.matricule} - ${voiture.marque} ${voiture.modele} (${voiture.prixLoc} MAD/jour)`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erreur:', error);
    }
}

async function loadClients() {
    try {
        const response = await fetch(`${API_URL}/clients`);
        clientsList = await response.json();
        
        const select = document.getElementById('cinCli');
        select.innerHTML = '<option value="">Sélectionner un client...</option>';
        
        clientsList.forEach(client => {
            const option = document.createElement('option');
            option.value = client.cinCli;
            option.textContent = `${client.cinCli} - ${client.nomCli} ${client.prenomCli}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('Erreur:', error);
    }
}

function loadAgentInfo() {
    const numAgent = document.getElementById('numAgent').value;
    const agent = agentsList.find(a => a.numAgent === numAgent);
    
    const infoBox = document.getElementById('agent-info');
    if (agent) {
        document.getElementById('agent-nom').textContent = `${agent.nomAgent} ${agent.prenomAgent}`;
        infoBox.style.display = 'block';
    } else {
        infoBox.style.display = 'none';
    }
}

function loadVoitureInfo() {
    const matricule = document.getElementById('matricule').value;
    const voiture = voituresList.find(v => v.matricule === matricule);
    
    const infoBox = document.getElementById('voiture-info');
    if (voiture) {
        document.getElementById('voiture-details').textContent = 
            `${voiture.marque} ${voiture.modele} - ${voiture.prixLoc} MAD/jour`;
        infoBox.style.display = 'block';
    } else {
        infoBox.style.display = 'none';
    }
}

function loadClientInfo() {
    const cinCli = document.getElementById('cinCli').value;
    const client = clientsList.find(c => c.cinCli === cinCli);
    
    const infoBox = document.getElementById('client-info');
    if (client) {
        document.getElementById('client-nom').textContent = `${client.nomCli} ${client.prenomCli}`;
        infoBox.style.display = 'block';
    } else {
        infoBox.style.display = 'none';
    }
}

async function calculerMontant() {
    const dateDebut = document.getElementById('dateDebut').value;
    const dateFin = document.getElementById('dateFin').value;
    const matricule = document.getElementById('matricule').value;
    
    if (!dateDebut || !dateFin || !matricule) {
        showAlert('Veuillez sélectionner les dates et une voiture', 'error');
        return;
    }
    
    const voiture = voituresList.find(v => v.matricule === matricule);
    if (!voiture) {
        showAlert('Voiture non trouvée', 'error');
        return;
    }
    
    // Calculer le nombre de jours
    const debut = new Date(dateDebut);
    const fin = new Date(dateFin);
    const diffTime = Math.abs(fin - debut);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
    
    // Calculer le montant
    const montant = diffDays * voiture.prixLoc;
    document.getElementById('montantT').value = montant.toFixed(2);
    
    showAlert(`Montant calculé: ${diffDays} jours × ${voiture.prixLoc} MAD = ${montant.toFixed(2)} MAD`, 'success');
}

async function chargerContrats() {
    try {
        const response = await fetch(`${API_URL}/contrats`);
        const contrats = await response.json();
        
        const tbody = document.getElementById('tableContrats').querySelector('tbody');
        tbody.innerHTML = '';
        
        for (const contrat of contrats) {
            // Récupérer les informations détaillées
            let clientInfo = 'N/A';
            let voitureInfo = 'N/A';
            let status = 'Terminé';
            
            try {
                // Client
                const clientRes = await fetch(`${API_URL}/clients/${contrat.cinCliFk}`);
                if (clientRes.ok) {
                    const client = await clientRes.json();
                    clientInfo = `${client.nomCli} ${client.prenomCli}`;
                }
                
                // Voiture
                const voitureRes = await fetch(`${API_URL}/voitures/${contrat.matriculeFk}`);
                if (voitureRes.ok) {
                    const voiture = await voitureRes.json();
                    voitureInfo = `${voiture.matricule} (${voiture.marque})`;
                    
                    // Déterminer le statut
                    const aujourdhui = new Date();
                    const dateFin = new Date(contrat.dateFin);
                    const dateDebut = new Date(contrat.dateDebut);
                    
                    if (aujourdhui >= dateDebut && aujourdhui <= dateFin) {
                        status = 'En cours';
                    } else if (aujourdhui < dateDebut) {
                        status = 'À venir';
                    }
                }
            } catch (error) {
                console.error('Erreur lors du chargement des détails:', error);
            }
            
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${contrat.numContrat}</strong></td>
                <td>${clientInfo}</td>
                <td>${voitureInfo}</td>
                <td>${formatDate(contrat.dateDebut)} - ${formatDate(contrat.dateFin)}</td>
                <td>${contrat.montantT.toFixed(2)} MAD</td>
                <td>
                    <span class="status-badge ${status === 'En cours' ? 'status-available' : 
                                                status === 'À venir' ? 'status-maintenance' : 'status-rented'}">
                        ${status}
                    </span>
                </td>
                <td class="actions">
                    <button class="btn btn-info btn-sm" onclick="imprimerFacture('${contrat.numContrat}')">
                        <i class="fas fa-print"></i> Imprimer
                    </button>
                    <button class="btn btn-warning btn-sm" onclick="openEditContrat('${contrat.numContrat}')">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerContrat('${contrat.numContrat}')">
                        <i class="fas fa-trash"></i> Supprimer
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors du chargement des contrats', 'error');
    }
}

async function ajouterContrat() {
    const contrat = {
        numContrat: document.getElementById('numContrat').value.trim(),
        dateDebut: document.getElementById('dateDebut').value,
        dateFin: document.getElementById('dateFin').value,
        montantT: parseFloat(document.getElementById('montantT').value),
        modePaiement: document.getElementById('modePaiement').value,
        numAgentFk: document.getElementById('numAgent').value,
        matriculeFk: document.getElementById('matricule').value,
        cinCliFk: document.getElementById('cinCli').value
    };
    
    // Validation
    if (!contrat.numContrat || !contrat.dateDebut || !contrat.dateFin || 
        !contrat.montantT || !contrat.modePaiement || !contrat.numAgentFk || 
        !contrat.matriculeFk || !contrat.cinCliFk) {
        showAlert('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    if (new Date(contrat.dateFin) < new Date(contrat.dateDebut)) {
        showAlert('La date de fin doit être après la date de début', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/contrats`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(contrat)
        });
        
        if (response.ok) {
            showAlert('Contrat créé avec succès!', 'success');
            document.getElementById('formContrat').reset();
            generateContratNumber();
            chargerContrats();
            loadVoituresDisponibles(); // Recharger car état changé
        } else {
            const errorText = await response.text();
            showAlert('Erreur: ' + errorText, 'error');
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la création du contrat', 'error');
    }
}

function openEditContrat(numContrat) {
    fetch(`${API_URL}/contrats/${numContrat}`)
        .then(response => response.json())
        .then(contrat => {
            document.getElementById('edit-numContrat').value = contrat.numContrat;
            document.getElementById('edit-display-numContrat').value = contrat.numContrat;
            document.getElementById('edit-dateDebut').value = contrat.dateDebut;
            document.getElementById('edit-dateFin').value = contrat.dateFin;
            document.getElementById('edit-montantT').value = contrat.montantT;
            document.getElementById('edit-modePaiement').value = contrat.modePaiement;
            openModal('modalEditContrat');
        })
        .catch(error => {
            console.error('Erreur:', error);
            showAlert('Erreur lors du chargement du contrat', 'error');
        });
}

async function updateContrat() {
    const updates = {
        numContrat: document.getElementById('edit-numContrat').value,
        dateDebut: document.getElementById('edit-dateDebut').value,
        dateFin: document.getElementById('edit-dateFin').value,
        montantT: parseFloat(document.getElementById('edit-montantT').value),
        modePaiement: document.getElementById('edit-modePaiement').value
    };
    
    if (!updates.dateDebut || !updates.dateFin || !updates.montantT || !updates.modePaiement) {
        showAlert('Veuillez remplir tous les champs', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/contrats/${updates.numContrat}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updates)
        });
        
        if (response.ok) {
            showAlert('Contrat mis à jour avec succès!', 'success');
            closeModal('modalEditContrat');
            chargerContrats();
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la mise à jour', 'error');
    }
}

async function supprimerContrat(numContrat) {
    if (!confirm(`Voulez-vous vraiment supprimer le contrat ${numContrat}?`)) return;
    
    try {
        const response = await fetch(`${API_URL}/contrats/${numContrat}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showAlert('Contrat supprimé avec succès!', 'success');
            chargerContrats();
            loadVoituresDisponibles(); // Recharger car état changé
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la suppression', 'error');
    }
}

async function rechercherContrat() {
    const searchTerm = document.getElementById('searchContrat').value.toLowerCase();
    
    try {
        const response = await fetch(`${API_URL}/contrats/search?keyword=${encodeURIComponent(searchTerm)}`);
        const contrats = await response.json();
        
        const tbody = document.getElementById('tableContrats').querySelector('tbody');
        tbody.innerHTML = '';
        
        for (const contrat of contrats) {
            // Récupérer les informations détaillées (simplifié pour la recherche)
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${contrat.numContrat}</strong></td>
                <td>${contrat.cinCliFk}</td>
                <td>${contrat.matriculeFk}</td>
                <td>${formatDate(contrat.dateDebut)} - ${formatDate(contrat.dateFin)}</td>
                <td>${contrat.montantT.toFixed(2)} MAD</td>
                <td>
                    <span class="status-badge status-rented">
                        ${new Date(contrat.dateFin) >= new Date() ? 'Actif' : 'Terminé'}
                    </span>
                </td>
                <td class="actions">
                    <button class="btn btn-info btn-sm" onclick="imprimerFacture('${contrat.numContrat}')">
                        <i class="fas fa-print"></i> Imprimer
                    </button>
                    <button class="btn btn-warning btn-sm" onclick="openEditContrat('${contrat.numContrat}')">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerContrat('${contrat.numContrat}')">
                        <i class="fas fa-trash"></i> Supprimer
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        }
    } catch (error) {
        console.error('Erreur:', error);
    }
}

function imprimerFacture(numContrat) {
    // Ouvrir une nouvelle fenêtre/facture.html avec le numéro de contrat
    window.open(`facture.html?contrat=${numContrat}`, '_blank');
}

// Initialiser
document.addEventListener('DOMContentLoaded', function() {
    // Définir les dates par défaut (aujourd'hui et dans 7 jours)
    const aujourdhui = new Date().toISOString().split('T')[0];
    const dans7jours = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];
    
    document.getElementById('dateDebut').value = aujourdhui;
    document.getElementById('dateFin').value = dans7jours;
    document.getElementById('edit-dateDebut').value = aujourdhui;
    document.getElementById('edit-dateFin').value = dans7jours;
    
    // Initialiser les données
    generateContratNumber();
    loadAgents();
    loadVoituresDisponibles();
    loadClients();
    chargerContrats();
    
    // Actualiser la liste des voitures disponibles toutes les minutes
    setInterval(loadVoituresDisponibles, 60000);
});