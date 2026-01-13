// Agents Functions
async function generateAgentNumber() {
    try {
        const response = await fetch(`${API_URL}/agents/next-numero`);
        const data = await response.json();
        document.getElementById('numAgent').value = data.nextNumero;
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la génération du numéro', 'error');
    }
}

async function chargerAgents() {
    try {
        const response = await fetch(`${API_URL}/agents`);
        const agents = await response.json();
        
        const tbody = document.getElementById('tableAgents').querySelector('tbody');
        tbody.innerHTML = '';
        
        agents.forEach(agent => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${agent.numAgent}</strong></td>
                <td>${agent.nomAgent}</td>
                <td>${agent.prenomAgent}</td>
                <td class="actions">
                    <button class="btn btn-warning btn-sm" onclick="openEditAgent('${agent.numAgent}')">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerAgent('${agent.numAgent}')">
                        <i class="fas fa-trash"></i> Supprimer
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors du chargement des agents', 'error');
    }
}

async function ajouterAgent() {
    const agent = {
        numAgent: document.getElementById('numAgent').value.trim(),
        nomAgent: document.getElementById('nomAgent').value.trim(),
        prenomAgent: document.getElementById('prenomAgent').value.trim()
    };
    
    if (!agent.numAgent || !agent.nomAgent || !agent.prenomAgent) {
        showAlert('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/agents`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(agent)
        });
        
        if (response.ok) {
            showAlert('Agent ajouté avec succès!', 'success');
            document.getElementById('formAgent').reset();
            generateAgentNumber(); // Générer un nouveau numéro pour le prochain
            chargerAgents();
        } else {
            showAlert('Erreur: Numéro d\'agent déjà existant', 'error');
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de l\'ajout de l\'agent', 'error');
    }
}

function openEditAgent(numAgent) {
    fetch(`${API_URL}/agents/${numAgent}`)
        .then(response => response.json())
        .then(agent => {
            document.getElementById('edit-numAgent').value = agent.numAgent;
            document.getElementById('edit-display-numAgent').value = agent.numAgent;
            document.getElementById('edit-nomAgent').value = agent.nomAgent;
            document.getElementById('edit-prenomAgent').value = agent.prenomAgent;
            openModal('modalEditAgent');
        })
        .catch(error => {
            console.error('Erreur:', error);
            showAlert('Erreur lors du chargement de l\'agent', 'error');
        });
}

async function updateAgent() {
    const updates = {
        numAgent: document.getElementById('edit-numAgent').value,
        nomAgent: document.getElementById('edit-nomAgent').value.trim(),
        prenomAgent: document.getElementById('edit-prenomAgent').value.trim()
    };
    
    if (!updates.nomAgent || !updates.prenomAgent) {
        showAlert('Veuillez remplir tous les champs', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/agents/${updates.numAgent}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updates)
        });
        
        if (response.ok) {
            showAlert('Agent mis à jour avec succès!', 'success');
            closeModal('modalEditAgent');
            chargerAgents();
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la mise à jour', 'error');
    }
}

async function supprimerAgent(numAgent) {
    if (!confirm(`Voulez-vous vraiment supprimer l'agent ${numAgent}?`)) return;
    
    try {
        const response = await fetch(`${API_URL}/agents/${numAgent}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            showAlert('Agent supprimé avec succès!', 'success');
            chargerAgents();
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la suppression', 'error');
    }
}

async function rechercherAgent() {
    const searchTerm = document.getElementById('searchAgent').value.toLowerCase();
    
    try {
        const response = await fetch(`${API_URL}/agents/search?keyword=${encodeURIComponent(searchTerm)}`);
        const agents = await response.json();
        
        const tbody = document.getElementById('tableAgents').querySelector('tbody');
        tbody.innerHTML = '';
        
        agents.forEach(agent => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${agent.numAgent}</strong></td>
                <td>${agent.nomAgent}</td>
                <td>${agent.prenomAgent}</td>
                <td class="actions">
                    <button class="btn btn-warning btn-sm" onclick="openEditAgent('${agent.numAgent}')">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerAgent('${agent.numAgent}')">
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
    generateAgentNumber();
    chargerAgents();
});