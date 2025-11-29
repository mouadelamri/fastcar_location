// FastCar Location - Application JavaScript

const API_URL = 'http://localhost:8080';

// ============================================
// NAVIGATION
// ============================================

document.querySelectorAll('.nav-btn').forEach(btn => {
    btn.addEventListener('click', () => {
        const sectionId = btn.dataset.section;
        switchSection(sectionId, btn);
    });
});

function switchSection(sectionId, btn) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.nav-btn').forEach(b => b.classList.remove('active'));
    
    document.getElementById(sectionId).classList.add('active');
    btn.classList.add('active');

    if (sectionId === 'dashboard') {
        loadDashboard();
    } else if (sectionId === 'clients') {
        loadClients();
    } else if (sectionId === 'agents') {
        loadAgents();
    } else if (sectionId === 'voitures') {
        loadVoitures();
    } else if (sectionId === 'contrats') {
        loadContrats();
    }
}

// ============================================
// DASHBOARD
// ============================================

async function loadDashboard() {
    try {
        const [clientsRes, agentsRes, voituresRes, contratsRes] = await Promise.all([
            fetch(`${API_URL}/api/clients`),
            fetch(`${API_URL}/api/agents`),
            fetch(`${API_URL}/api/voitures`),
            fetch(`${API_URL}/api/contrats`)
        ]);

        const clients = await clientsRes.json();
        const agents = await agentsRes.json();
        const voitures = await voituresRes.json();
        const contrats = await contratsRes.json();

        document.getElementById('stat-clients').textContent = clients.length || 0;
        document.getElementById('stat-agents').textContent = agents.length || 0;
        document.getElementById('stat-voitures').textContent = voitures.length || 0;
        document.getElementById('stat-contrats').textContent = contrats.length || 0;

        showAlert('Statistiques mises à jour!', 'success');
    } catch (error) {
        showAlert('Erreur lors du chargement des statistiques', 'error');
        console.error(error);
    }
}

// ============================================
// CLIENTS CRUD
// ============================================

async function loadClients() {
    try {
        const response = await fetch(`${API_URL}/api/clients`);
        const clients = await response.json();
        let html = '';

        if (clients.length === 0) {
            html = '<div class="no-data">Aucun client enregistré</div>';
        } else {
            html = '<table><thead><tr><th>CIN</th><th>Nom</th><th>Prénom</th><th>Téléphone</th><th>Email</th><th>Adresse</th><th>Actions</th></tr></thead><tbody>';
            clients.forEach(client => {
                html += `<tr>
                    <td>${client.cin}</td>
                    <td>${client.nomCli}</td>
                    <td>${client.prenomCli}</td>
                    <td>${client.telephone || '-'}</td>
                    <td>${client.email || '-'}</td>
                    <td>${client.adresse || '-'}</td>
                    <td><button class="btn-danger" onclick="deleteClient('${client.cin}')">Supprimer</button></td>
                </tr>`;
            });
            html += '</tbody></table>';
        }
        document.getElementById('clients-list').innerHTML = html;
    } catch (error) {
        showAlert('Erreur lors du chargement des clients', 'error');
        console.error(error);
    }
}

async function addClient(e) {
    e.preventDefault();
    const client = {
        cin: document.getElementById('cin_cli').value.trim(),
        nomCli: document.getElementById('nom_cli').value.trim(),
        prenomCli: document.getElementById('prenom_cli').value.trim(),
        telephone: document.getElementById('telephone').value.trim() || null,
        email: document.getElementById('email').value.trim() || null,
        adresse: document.getElementById('adresse').value.trim() || null
    };

    // Validation côté client
    if (!client.cin || !client.nomCli || !client.prenomCli) {
        showAlert('Veuillez remplir les champs obligatoires (CIN, Nom, Prénom)', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/api/clients`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(client)
        });
        
        const responseData = await response.json();
        
        if (response.ok) {
            closeModal('addClientModal');
            document.querySelector('#addClientModal form').reset();
            loadClients();
            loadDashboard();
            showAlert('Client ajouté avec succès!', 'success');
        } else {
            let errorMessage = 'Erreur lors de l\'ajout du client';
            if (responseData.errors) {
                const errorList = Object.entries(responseData.errors)
                    .map(([field, message]) => `${field}: ${message}`)
                    .join('\n');
                errorMessage = 'Erreurs de validation:\n' + errorList;
            } else if (responseData.error) {
                errorMessage = responseData.error;
            }
            showAlert(errorMessage, 'error');
            console.error('Erreur création client:', responseData);
        }
    } catch (error) {
        showAlert('Erreur de connexion: ' + error.message, 'error');
        console.error('Erreur:', error);
    }
}

async function deleteClient(cin) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce client?')) {
        try {
            const response = await fetch(`${API_URL}/api/clients/${cin}`, { method: 'DELETE' });
            if (response.ok) {
                loadClients();
                showAlert('Client supprimé avec succès!', 'success');
            } else {
                showAlert('Erreur lors de la suppression', 'error');
            }
        } catch (error) {
            showAlert('Erreur: ' + error.message, 'error');
        }
    }
}

// ============================================
// AGENTS CRUD
// ============================================

async function loadAgents() {
    try {
        const response = await fetch(`${API_URL}/api/agents`);
        const agents = await response.json();
        let html = '';

        if (agents.length === 0) {
            html = '<div class="no-data">Aucun agent enregistré</div>';
        } else {
            html = '<table><thead><tr><th>ID</th><th>Nom</th><th>Prénom</th><th>Actions</th></tr></thead><tbody>';
            agents.forEach(agent => {
                html += `<tr>
                    <td>${agent.numAgent}</td>
                    <td>${agent.nomAgent}</td>
                    <td>${agent.prenomAgent}</td>
                    <td><button class="btn-danger" onclick="deleteAgent(${agent.numAgent})">Supprimer</button></td>
                </tr>`;
            });
            html += '</tbody></table>';
        }
        document.getElementById('agents-list').innerHTML = html;
    } catch (error) {
        showAlert('Erreur lors du chargement des agents', 'error');
        console.error(error);
    }
}

async function addAgent(e) {
    e.preventDefault();
    const agent = {
        nomAgent: document.getElementById('nom_agent').value.trim(),
        prenomAgent: document.getElementById('prenom_agent').value.trim()
    };

    // Validation côté client
    if (!agent.nomAgent || !agent.prenomAgent) {
        showAlert('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/api/agents`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(agent)
        });
        
        const responseData = await response.json();
        
        if (response.ok) {
            closeModal('addAgentModal');
            document.querySelector('#addAgentModal form').reset();
            loadAgents();
            loadDashboard();
            showAlert('Agent ajouté avec succès!', 'success');
        } else {
            let errorMessage = 'Erreur lors de l\'ajout de l\'agent';
            if (responseData.errors) {
                const errorList = Object.entries(responseData.errors)
                    .map(([field, message]) => `${field}: ${message}`)
                    .join('\n');
                errorMessage = 'Erreurs de validation:\n' + errorList;
            } else if (responseData.error) {
                errorMessage = responseData.error;
            }
            showAlert(errorMessage, 'error');
            console.error('Erreur création agent:', responseData);
        }
    } catch (error) {
        showAlert('Erreur de connexion: ' + error.message, 'error');
        console.error('Erreur:', error);
    }
}

async function deleteAgent(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet agent?')) {
        try {
            const response = await fetch(`${API_URL}/api/agents/${id}`, { method: 'DELETE' });
            if (response.ok) {
                loadAgents();
                showAlert('Agent supprimé avec succès!', 'success');
            } else {
                showAlert('Erreur lors de la suppression', 'error');
            }
        } catch (error) {
            showAlert('Erreur: ' + error.message, 'error');
        }
    }
}

// ============================================
// VOITURES CRUD
// ============================================

async function loadVoitures() {
    try {
        const response = await fetch(`${API_URL}/api/voitures`);
        const voitures = await response.json();
        let html = '';

        if (voitures.length === 0) {
            html = '<div class="no-data">Aucun véhicule enregistré</div>';
        } else {
            html = '<table><thead><tr><th>Matricule</th><th>Marque</th><th>Modèle</th><th>Prix/Jour</th><th>État</th><th>Km</th><th>Actions</th></tr></thead><tbody>';
            voitures.forEach(v => {
                const etatColor = v.etatVeh === 'DISPONIBLE' ? '#27ae60' : v.etatVeh === 'LOUEE' ? '#f39c12' : '#e74c3c';
                html += `<tr>
                    <td><strong>${v.matricule}</strong></td>
                    <td>${v.marque}</td>
                    <td>${v.modele}</td>
                    <td>${v.prixLoc}€</td>
                    <td><span style="background: ${etatColor}; color: white; padding: 5px 10px; border-radius: 3px; display: inline-block;">${v.etatVeh}</span></td>
                    <td>${v.kilometrage}</td>
                    <td><button class="btn-danger" onclick="deleteVoiture('${v.matricule}')">Supprimer</button></td>
                </tr>`;
            });
            html += '</tbody></table>';
        }
        document.getElementById('voitures-list').innerHTML = html;
    } catch (error) {
        showAlert('Erreur lors du chargement des véhicules', 'error');
        console.error(error);
    }
}

async function addVoiture(e) {
    e.preventDefault();
    const voiture = {
        matricule: document.getElementById('matricule').value.trim(),
        marque: document.getElementById('marque').value.trim(),
        modele: document.getElementById('modele').value.trim(),
        prixLoc: parseFloat(document.getElementById('prix_loc').value),
        kilometrage: parseInt(document.getElementById('kilometrage').value) || 0,
        etatVeh: document.getElementById('etat_veh').value
    };

    // Validation côté client
    if (!voiture.matricule || !voiture.marque || !voiture.modele || !voiture.prixLoc || !voiture.etatVeh) {
        showAlert('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    if (voiture.prixLoc <= 0) {
        showAlert('Le prix de location doit être supérieur à 0', 'error');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/api/voitures`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(voiture)
        });
        
        const responseData = await response.json();
        
        if (response.ok) {
            closeModal('addVoitureModal');
            document.querySelector('#addVoitureModal form').reset();
            loadVoitures();
            loadDashboard();
            showAlert('Véhicule ajouté avec succès!', 'success');
        } else {
            let errorMessage = 'Erreur lors de l\'ajout du véhicule';
            if (responseData.errors) {
                const errorList = Object.entries(responseData.errors)
                    .map(([field, message]) => `${field}: ${message}`)
                    .join('\n');
                errorMessage = 'Erreurs de validation:\n' + errorList;
            } else if (responseData.error) {
                errorMessage = responseData.error;
            }
            showAlert(errorMessage, 'error');
            console.error('Erreur création voiture:', responseData);
        }
    } catch (error) {
        showAlert('Erreur de connexion: ' + error.message, 'error');
        console.error('Erreur:', error);
    }
}

async function deleteVoiture(matricule) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce véhicule?')) {
        try {
            const response = await fetch(`${API_URL}/api/voitures/${matricule}`, { method: 'DELETE' });
            if (response.ok) {
                loadVoitures();
                showAlert('Véhicule supprimé avec succès!', 'success');
            } else {
                showAlert('Erreur lors de la suppression', 'error');
            }
        } catch (error) {
            showAlert('Erreur: ' + error.message, 'error');
        }
    }
}

// ============================================
// CONTRATS CRUD
// ============================================

async function loadContrats() {
    try {
        const response = await fetch(`${API_URL}/api/contrats`);
        const contrats = await response.json();
        let html = '';

        if (contrats.length === 0) {
            html = '<div class="no-data">Aucun contrat enregistré</div>';
        } else {
            html = '<table><thead><tr><th>ID</th><th>Client</th><th>Véhicule</th><th>Agent</th><th>Début</th><th>Fin</th><th>Montant</th><th>Paiement</th><th>Actions</th></tr></thead><tbody>';
            contrats.forEach(c => {
                html += `<tr>
                    <td>${c.numContract}</td>
                    <td>${c.nomCli || '-'} ${c.prenomCli || ''} (${c.cinCli || '-'})</td>
                    <td>${c.marque || '-'} ${c.modele || ''} (${c.matricule || '-'})</td>
                    <td>${c.nomAgent || '-'} ${c.prenomAgent || ''}</td>
                    <td>${c.dateDebut ? new Date(c.dateDebut).toLocaleDateString('fr-FR') : '-'}</td>
                    <td>${c.dateFin ? new Date(c.dateFin).toLocaleDateString('fr-FR') : '-'}</td>
                    <td><strong>${c.montantT ? (typeof c.montantT === 'number' ? c.montantT.toFixed(2) : parseFloat(c.montantT).toFixed(2)) : '0.00'} MAD</strong></td>
                    <td>${c.modePaiement}</td>
                    <td>
                        <button class="btn-primary" onclick="window.open('${API_URL}/api/factures/${c.numContract}/html', '_blank')" style="margin-right: 5px;">Facture HTML</button>
                        <button class="btn-primary" onclick="window.open('${API_URL}/api/factures/${c.numContract}/pdf', '_blank')" style="margin-right: 5px;">Facture PDF</button>
                        <button class="btn-danger" onclick="deleteContrat(${c.numContract})">Supprimer</button>
                    </td>
                </tr>`;
            });
            html += '</tbody></table>';
        }
        document.getElementById('contrats-list').innerHTML = html;
    } catch (error) {
        showAlert('Erreur lors du chargement des contrats', 'error');
        console.error(error);
    }
}

async function addContrat(e) {
    e.preventDefault();
    
    // Validation côté client
    const numAgent = document.getElementById('num_agent_contrat').value;
    const matricule = document.getElementById('matricule_contrat').value;
    const cinCli = document.getElementById('cin_cli_contrat').value;
    const dateDebut = document.getElementById('date_debut').value;
    const dateFin = document.getElementById('date_fin').value;
    const modePaiement = document.getElementById('mode_paiement').value;
    
    if (!numAgent || !matricule || !cinCli || !dateDebut || !dateFin || !modePaiement) {
        showAlert('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    // Vérifier que la date de fin est après la date de début
    if (new Date(dateFin) < new Date(dateDebut)) {
        showAlert('La date de fin doit être après la date de début', 'error');
        return;
    }
    
    const contrat = {
        dateDebut: dateDebut,
        dateFin: dateFin,
        modePaiement: modePaiement,
        numAgent: parseInt(numAgent),
        matricule: matricule.trim(),
        cinCli: cinCli.trim()
    };

    try {
        const response = await fetch(`${API_URL}/api/contrats`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(contrat)
        });
        
        let responseData;
        try {
            responseData = await response.json();
        } catch (e) {
            responseData = { error: await response.text() };
        }
        
        if (response.ok) {
            closeModal('addContratModal');
            document.querySelector('#addContratModal form').reset();
            loadContrats();
            loadDashboard();
            showAlert('Contrat créé avec succès!', 'success');
        } else {
            // Afficher les erreurs de validation détaillées
            let errorMessage = 'Erreur lors de la création du contrat';
            
            if (responseData.errors) {
                // Erreurs de validation
                const errorList = Object.entries(responseData.errors)
                    .map(([field, message]) => {
                        // Traduire les noms de champs
                        const fieldNames = {
                            'dateDebut': 'Date de début',
                            'dateFin': 'Date de fin',
                            'modePaiement': 'Mode de paiement',
                            'numAgent': 'Numéro agent',
                            'matricule': 'Matricule véhicule',
                            'cinCli': 'CIN client'
                        };
                        return `${fieldNames[field] || field}: ${message}`;
                    })
                    .join(' | ');
                errorMessage = 'Erreurs de validation: ' + errorList;
            } else if (responseData.error) {
                errorMessage = responseData.error;
            }
            
            showAlert(errorMessage, 'error');
            console.error('Erreur création contrat:', responseData);
        }
    } catch (error) {
        showAlert('Erreur de connexion: ' + error.message, 'error');
        console.error('Erreur:', error);
    }
}

async function deleteContrat(id) {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce contrat?')) {
        try {
            const response = await fetch(`${API_URL}/api/contrats/${id}`, { method: 'DELETE' });
            if (response.ok) {
                loadContrats();
                showAlert('Contrat supprimé avec succès!', 'success');
            } else {
                showAlert('Erreur lors de la suppression', 'error');
            }
        } catch (error) {
            showAlert('Erreur: ' + error.message, 'error');
        }
    }
}

// ============================================
// MODAL FUNCTIONS
// ============================================

function openAddClientModal() { 
    document.getElementById('addClientModal').classList.add('show'); 
}

function openAddAgentModal() { 
    document.getElementById('addAgentModal').classList.add('show'); 
}

function openAddVoitureModal() { 
    document.getElementById('addVoitureModal').classList.add('show'); 
}

function openAddContratModal() { 
    document.getElementById('addContratModal').classList.add('show'); 
}

function closeModal(modalId) {
    document.getElementById(modalId).classList.remove('show');
}

// ============================================
// UTILITY FUNCTIONS
// ============================================

function showAlert(message, type) {
    // Supprimer les alertes existantes
    const existingAlerts = document.querySelectorAll('.alert');
    existingAlerts.forEach(alert => alert.remove());
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    
    // Gérer les messages multi-lignes (remplacer \n par <br>)
    const formattedMessage = message.replace(/\n/g, '<br>');
    alertDiv.innerHTML = formattedMessage;
    
    // Style pour les erreurs longues
    if (type === 'error' && message.length > 100) {
        alertDiv.style.maxHeight = '200px';
        alertDiv.style.overflowY = 'auto';
        alertDiv.style.fontSize = '0.9em';
    }
    
    const activeSection = document.querySelector('.section.active');
    if (activeSection) {
        activeSection.insertBefore(alertDiv, activeSection.firstChild);
    } else {
        document.querySelector('main').insertBefore(alertDiv, document.querySelector('main').firstChild);
    }
    
    // Garder l'alerte plus longtemps pour les erreurs
    const timeout = type === 'error' ? 8000 : 4000;
    setTimeout(() => alertDiv.remove(), timeout);
}

// ============================================
// INITIALIZATION
// ============================================

document.addEventListener('DOMContentLoaded', () => {
    console.log('FastCar Location Frontend loaded successfully!');
    console.log('API URL:', API_URL);
    loadDashboard();
});
