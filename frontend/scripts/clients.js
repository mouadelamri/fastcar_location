// Clients Functions
async function chargerClients() {
    try {
        const response = await fetch(`${API_URL}/clients`);
        const clients = await response.json();
        
        const tbody = document.getElementById('tableClients').querySelector('tbody');
        tbody.innerHTML = '';
        
        clients.forEach(client => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${client.cinCli}</strong></td>
                <td>${client.nomCli}</td>
                <td>${client.prenomCli}</td>
                <td>${client.telephone || '-'}</td>
                <td>${client.email || '-'}</td>
                <td class="actions">
                    <button class="btn btn-warning btn-sm" onclick="openEditClient('${client.cinCli}')">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerClient('${client.cinCli}')">
                        <i class="fas fa-trash"></i> Supprimer
                    </button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors du chargement des clients', 'error');
    }
}

async function ajouterClient() {
    const client = {
        cinCli: document.getElementById('cin').value.trim(),
        nomCli: document.getElementById('nom').value.trim(),
        prenomCli: document.getElementById('prenom').value.trim(),
        telephone: document.getElementById('telephone').value.trim(),
        email: document.getElementById('email').value.trim() || null,
        adresse: document.getElementById('adresse').value.trim() || null
    };
    
    if (!client.cinCli || !client.nomCli || !client.prenomCli || !client.telephone) {
        showAlert('Veuillez remplir tous les champs obligatoires', 'error');
        return;
    }
    
    try {
        const response = await fetch(`${API_URL}/clients`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(client)
        });
        
        if (response.ok) {
            showAlert('Client ajouté avec succès!', 'success');
            document.getElementById('formClient').reset();
            chargerClients();
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de l\'ajout du client', 'error');
    }
}

function openEditClient(cin) {
    fetch(`${API_URL}/clients/${cin}`)
        .then(response => response.json())
        .then(client => {
            document.getElementById('edit-cin').value = client.cinCli;
            document.getElementById('edit-nom').value = client.nomCli;
            document.getElementById('edit-prenom').value = client.prenomCli;
            document.getElementById('edit-telephone').value = client.telephone || '';
            document.getElementById('edit-email').value = client.email || '';
            openModal('modalEditClient');
        })
        .catch(error => {
            console.error('Erreur:', error);
            showAlert('Erreur lors du chargement du client', 'error');
        });
}

async function updateClient() {
    const updates = {
        telephone: document.getElementById('edit-telephone').value.trim(),
        email: document.getElementById('edit-email').value.trim() || null
    };
    
    if (!updates.telephone) {
        showAlert('Le téléphone est obligatoire', 'error');
        return;
    }
    
    const cin = document.getElementById('edit-cin').value;
    
    try {
        const response = await fetch(`${API_URL}/clients/${cin}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updates)
        });
        
        if (response.ok) {
            showAlert('Client mis à jour avec succès!', 'success');
            closeModal('modalEditClient');
            chargerClients();
        }
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la mise à jour', 'error');
    }
}

async function supprimerClient(cin) {
    if (!confirm(`Voulez-vous vraiment supprimer le client ${cin}?`)) return;
    
    try {
        await fetch(`${API_URL}/clients/${cin}`, { method: 'DELETE' });
        showAlert('Client supprimé avec succès!', 'success');
        chargerClients();
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors de la suppression', 'error');
    }
}

async function rechercherClient() {
    const searchTerm = document.getElementById('searchClient').value.toLowerCase();
    
    try {
        const response = await fetch(`${API_URL}/clients`);
        const clients = await response.json();
        
        const tbody = document.getElementById('tableClients').querySelector('tbody');
        tbody.innerHTML = '';
        
        const filteredClients = clients.filter(client =>
            client.nomCli.toLowerCase().includes(searchTerm) ||
            client.prenomCli.toLowerCase().includes(searchTerm) ||
            client.cinCli.toLowerCase().includes(searchTerm) ||
            (client.telephone && client.telephone.includes(searchTerm))
        );
        
        filteredClients.forEach(client => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td><strong>${client.cinCli}</strong></td>
                <td>${client.nomCli}</td>
                <td>${client.prenomCli}</td>
                <td>${client.telephone || '-'}</td>
                <td>${client.email || '-'}</td>
                <td class="actions">
                    <button class="btn btn-warning btn-sm" onclick="openEditClient('${client.cinCli}')">
                        <i class="fas fa-edit"></i> Modifier
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="supprimerClient('${client.cinCli}')">
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

document.addEventListener('DOMContentLoaded', chargerClients);