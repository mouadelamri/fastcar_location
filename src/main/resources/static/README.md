# FastCar Location - Frontend

Frontend HTML/CSS/JS pour la gestion de location de voitures.

## 📁 Fichiers

- **index.html** - Application web complète (single-page app)
  - Tableau de bord avec statistiques
  - Gestion des clients
  - Gestion des agents
  - Gestion des véhicules
  - Gestion des contrats

## 🚀 Comment Utiliser

### Option 1: Servir via Spring Boot (Recommandé)

1. Copiez `index.html` dans le dossier des ressources statiques du backend:
   ```
   backend/src/main/resources/static/index.html
   ```

2. Lancez le backend:
   ```powershell
   cd C:\fastcar_location\backend
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

3. Ouvrez dans le navigateur:
   ```
   http://localhost:8080
   ```

### Option 2: Servir avec Docker

1. Le frontend est automatiquement inclus si vous copiez `index.html` dans `backend/src/main/resources/static/`

2. Lancez le stack complet:
   ```powershell
   cd C:\fastcar_location
   docker compose up --build
   ```

3. Accédez à:
   ```
   http://localhost:8080
   ```

### Option 3: Ouvrir directement (Développement Rapide)

Double-cliquez sur `index.html` pour l'ouvrir dans le navigateur, MAIS vous devez d'abord:
1. Lancer le backend (voir Options 1 ou 2)
2. Ensuite ouvrir le fichier HTML

## 🎨 Fonctionnalités

### Tableau de Bord
- Affiche les statistiques en temps réel
- Nombre de clients, agents, véhicules et contrats
- Bouton pour rafraîchir manuellement

### Gestion des Clients
- 📝 Ajouter un client (CIN, Nom, Prénom, Téléphone, Email, Adresse)
- 📋 Lister tous les clients
- 🗑️ Supprimer un client

### Gestion des Agents
- ➕ Ajouter un agent (Nom, Prénom)
- 📋 Lister tous les agents
- 🗑️ Supprimer un agent

### Gestion des Véhicules
- 🚙 Ajouter un véhicule (Matricule, Marque, Modèle, Prix/Jour, Kilométrage, État)
- 📋 Lister tous les véhicules avec état (Disponible/Louée/En Maintenance)
- 🗑️ Supprimer un véhicule

### Gestion des Contrats
- 📋 Créer un contrat (Client, Véhicule, Agent, Dates, Montant, Mode Paiement)
- 📋 Lister tous les contrats avec détails
- 🗑️ Supprimer un contrat

## 📱 Design Responsive

- ✅ Adapté aux ordinateurs de bureau
- ✅ Adapté aux tablettes
- ✅ Adapté aux téléphones mobiles
- 🎨 Design moderne avec gradients et animations

## 🔌 API Endpoints Attendus

L'application appelle les endpoints suivants (assurez-vous que le backend les expose):

```
GET    http://localhost:8080/clients
POST   http://localhost:8080/clients
DELETE http://localhost:8080/clients/{cin}

GET    http://localhost:8080/agents
POST   http://localhost:8080/agents
DELETE http://localhost:8080/agents/{id}

GET    http://localhost:8080/voitures
POST   http://localhost:8080/voitures
DELETE http://localhost:8080/voitures/{matricule}

GET    http://localhost:8080/contrats
POST   http://localhost:8080/contrats
DELETE http://localhost:8080/contrats/{id}
```

## 🐛 Dépannage

### "ERR_BLOCKED_BY_CLIENT" ou "CORS error"
- Assurez-vous que le backend est lancé
- Vérifiez que CORS est configuré correctement dans `CorsConfig.java`
- Essayez avec un navigateur différent (Firefox, Chrome, Edge)

### "Failed to fetch"
- Vérifiez que le backend est lancé sur http://localhost:8080
- Vérifiez la console du navigateur (F12 → Onglet Console)
- Vérifiez les logs du backend

### Pas de données affichées
- Assurez-vous que vous avez lancé les migrations Flyway (V1 et V2)
- Vérifiez que les données de test sont dans la base MySQL/H2
- Rechargez la page (Ctrl+F5)

## 🔄 Flux de Travail Recommandé

1. ✅ Lancer le backend avec MySQL (Docker Compose)
2. ✅ Ouvrir le frontend dans le navigateur
3. ✅ Vérifier le tableau de bord (statistiques)
4. ✅ Ajouter des données:
   - Ajouter des agents
   - Ajouter des clients
   - Ajouter des véhicules
   - Créer des contrats
5. ✅ Vérifier les opérations CRUD (Create, Read, Update, Delete)

## 📊 Technologies Utilisées

- **HTML5** - Structure
- **CSS3** - Design et animations (gradients, flexbox, grid)
- **JavaScript ES6+** - Logique et API calls
- **Fetch API** - Communication avec le backend
- **Responsive Design** - Mobile-first approach

## 🎯 Structure du Code JavaScript

- `switchSection()` - Navigation entre les sections
- `loadDashboard()` - Charge les statistiques
- `loadClients/Agents/Voitures/Contrats()` - Récupère les données
- `addClient/Agent/Voiture/Contrat()` - Crée une ressource
- `deleteClient/Agent/Voiture/Contrat()` - Supprime une ressource
- `openAddModal()` - Ouvre les dialogues modaux
- `closeModal()` - Ferme les dialogues modaux

## 📝 Notes Importantes

- Le frontend suppose que le backend est sur `http://localhost:8080`
- Toutes les requêtes AJAX utilisent `fetch()`
- Les alertes s'affichent temporairement (4 secondes)
- Les modales se ferment après un ajout réussi
- La validation côté client est basique (côté serveur validée par Jakarta Validation)

## 🚀 Prochaines Améliorations Possibles

- [ ] Ajouter un formulaire de connexion/authentification
- [ ] Ajouter des filtres et recherche
- [ ] Ajouter l'édition de ressources (PUT)
- [ ] Ajouter des graphiques/charts pour le tableau de bord
- [ ] Ajouter les notifications toast plus avancées
- [ ] Ajouter l'export en PDF/Excel
- [ ] Ajouter des tests unitaires (Jest/Mocha)

---

**Créé:** 2025-11-27  
**Version:** 1.0.0  
**Compatible:** FastCar Location Backend v1.0.0 (Java 21 + Spring Boot 3.2)
