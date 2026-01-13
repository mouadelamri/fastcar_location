// Variables globales
let currentEditingMatricule = '';

// Gestion du select de marque
function handleMarqueSelect() {
    const select = document.getElementById('marque-select');
    const input = document.getElementById('marque-input');
    
    if (select.value === 'other') {
        input.style.display = 'block';
        input.required = true;
        input.value = '';
        input.focus();
    } else {
        input.style.display = 'none';
        input.required = false;
        input.value = select.value;
    }
}

// Charger les voitures
async function chargerVoitures() {
    try {
        const response = await fetch(`${API_URL}/voitures`);
        const voitures = await response.json();
        
        const tbody = document.getElementById('tableVoitures').querySelector('tbody');
        tbody.innerHTML = '';
        
        voitures.forEach(voiture => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${voiture.matricule}</strong></td>
                <td>${voiture.marque}</td>
                <td>${voiture.modele}</td>
                <td>${voiture.prixLoc.toFixed(2)} MAD</td>
                <td>
                    <span class="status-badge status-${voiture.etatVeh === 'Disponible' ? 'available' : 
                                                        voiture.etatVeh === 'Louée' ? 'rented' : 'maintenance'}">
                        ${voiture.etatVeh}
                    </span>
                </td>
                <td>${voiture.kilometrage ? voiture.kilometrage.toLocaleString() + ' km' : 'N/A'}</td>
                <td class="actions">
                    <button class="btn btn-warning btn-sm" onclick="openEditKilometrage('${voiture.matricule}', ${voiture.kilometrage || 0})">
                        <i class="fas fa-edit"></i> Modifier KM
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerVoiture('${voiture.matricule}')">
                        <i class="fas fa-trash"></i> Supprimer
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors du chargement des voitures', 'error');
    }
}

// Ajouter une voiture
async function ajouterVoiture() {
    const matricule = document.getElementById('matricule').value.trim();
    const marqueSelect = document.getElementById('marque-select').value;
    const marqueInput = document.getElementById('marque-input').value.trim();
    const marque = marqueSelect === 'other' ? marqueInput : marqueSelect;
    
    const voiture = {
        matricule: matricule,
        marque: marque,
        modele: document.getElementById('modele').value.trim(),
        prixLoc: parseFloat(document.getElementById('prix').value),
        etatVeh: document.getElementById('etat').value,
        kilometrage: parseInt(document.getElementById('kilometrage').value) || 0
    };
    
    // Validation
    if (!voiture.matricule || !voiture.marque || !voiture.modele || !voiture.prixLoc || !voiture.etatVeh) {
        showAlert('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/voitures`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(voiture)
        });
        
        if (response.ok) {
            showAlert('Voiture ajoutée avec succès!', 'success');
            document.getElementById('formVoiture').reset();
            document.getElementById('marque-input').style.display = 'none';
            chargerVoitures();
        } else {
            const error = await response.text();
            showAlert('Erreur: ' + error, 'error');
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de l\'ajout de la voiture', 'error');
    }
}

// Ouvrir modal pour modifier kilométrage
function openEditKilometrage(matricule, kilometrage) {
    currentEditingMatricule = matricule;
    document.getElementById('edit-matricule').value = matricule;
    document.getElementById('edit-kilometrage').value = kilometrage;
    openModal('modalKilometrage');
}

// Mettre à jour le kilométrage
async function updateKilometrage() {
    const kilometrage = parseInt(document.getElementById('edit-kilometrage').value);
    
    if (!kilometrage || kilometrage < 0) {
        showAlert('Veuillez entrer un kilométrage valide', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/voitures/${currentEditingMatricule}/kilometrage`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ kilometrage: kilometrage })
        });
        
        if (response.ok) {
            showAlert('Kilométrage mis à jour avec succès!', 'success');
            closeModal('modalKilometrage');
            chargerVoitures();
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la mise à jour', 'error');
    }
}

// Supprimer une voiture
async function supprimerVoiture(matricule) {
    if (!confirm(`Voulez-vous vraiment supprimer la voiture ${matricule}?`)) {
        return;
    }
    
    try {
        await fetch(`${API_URL}/voitures/${matricule}`, {
            method: 'DELETE'
        });
        showAlert('Voiture supprimée avec succès!', 'success');
        chargerVoitures();
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la suppression', 'error');
    }
}

// Rechercher des voitures
async function rechercherVoiture() {
    const searchTerm = document.getElementById('searchVoiture').value.toLowerCase();
    
    try {
        const response = await fetch(`${API_URL}/voitures`);
        const voitures = await response.json();
        
        const tbody = document.getElementById('tableVoitures').querySelector('tbody');
        tbody.innerHTML = '';
        
        const filteredVoitures = voitures.filter(voiture =>
            voiture.marque.toLowerCase().includes(searchTerm) ||
            voiture.modele.toLowerCase().includes(searchTerm) ||
            voiture.matricule.toLowerCase().includes(searchTerm)
        );
        
        filteredVoitures.forEach(voiture => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${voiture.matricule}</strong></td>
                <td>${voiture.marque}</td>
                <td>${voiture.modele}</td>
                <td>${voiture.prixLoc.toFixed(2)} MAD</td>
                <td>
                    <span class="status-badge status-${voiture.etatVeh === 'Disponible' ? 'available' : 
                                                        voiture.etatVeh === 'Louée' ? 'rented' : 'maintenance'}">
                        ${voiture.etatVeh}
                    </span>
                </td>
                <td>${voiture.kilometrage ? voiture.kilometrage.toLocaleString() + ' km' : 'N/A'}</td>
                <td class="actions">
                    <button class="btn btn-warning btn-sm" onclick="openEditKilometrage('${voiture.matricule}', ${voiture.kilometrage || 0})">
                        <i class="fas fa-edit"></i> Modifier KM
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerVoiture('${voiture.matricule}')">
                        <i class="fas fa-trash"></i> Supprimer
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Erreur:', error);
    }
}

// Initialiser
document.addEventListener('DOMContentLoaded', function() {
    chargerVoitures();
    
    // Validation du matricule
    document.getElementById('matricule').addEventListener('input', function(e) {
        const value = e.target.value.toUpperCase();
        e.target.value = value.replace(/[^A-Z0-9-]/g, '');
    });
});