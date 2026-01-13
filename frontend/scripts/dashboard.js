// Dashboard Functions
async function loadDashboard() {
    try {
        const response = await fetch(`${API_URL}/statistiques/dashboard`);
        const stats = await response.json();
        
        // Mettre à jour les statistiques
        document.getElementById('total-voitures').textContent = stats.totalVoitures || 0;
        document.getElementById('voitures-disponibles').textContent = stats.voituresDisponibles || 0;
        document.getElementById('voitures-louees').textContent = stats.voituresLouees || 0;
        document.getElementById('voitures-maintenance').textContent = stats.voituresMaintenance || 0;
        document.getElementById('total-clients').textContent = stats.totalClients || 0;
        document.getElementById('total-agents').textContent = stats.totalAgents || 0;
        document.getElementById('total-contrats').textContent = stats.totalContrats || 0;
        
        // Calculer le taux d'occupation
        const tauxOccupation = stats.totalVoitures > 0 
            ? Math.round((stats.voituresLouees / stats.totalVoitures) * 100) 
            : 0;
        document.getElementById('taux-occupation').textContent = `${tauxOccupation}%`;
        
        showAlert('Tableau de bord actualisé avec succès', 'success');
    } catch (error) {
        console.error('Erreur:', error);
        showAlert('Erreur lors du chargement des statistiques', 'error');
    }
}

function generateReport() {
    showAlert('Génération du rapport mensuel en cours...', 'info');
    // Simuler la génération du rapport
    setTimeout(() => {
        const link = document.createElement('a');
        link.href = '#';
        link.download = 'rapport_mensuel_fastcar.pdf';
        link.click();
        showAlert('Rapport généré avec succès', 'success');
    }, 2000);
}

// Initialiser le dashboard
document.addEventListener('DOMContentLoaded', function() {
    loadDashboard();
    
    // Actualiser toutes les 30 secondes
    setInterval(loadDashboard, 30000);
});