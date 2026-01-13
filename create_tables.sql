-- 1. NETTOYAGE ET CRÉATION DE LA BASE DE DONNÉES

-- Supprime la base de données si elle existe déjà (pour un script réutilisable)
DROP DATABASE IF EXISTS FastCar_Location;

-- Crée la nouvelle base de données
CREATE DATABASE FastCar_Location;

-- Sélectionne la base de données pour les commandes suivantes
USE FastCar_Location;

-- 2. CRÉATION DES TABLES PRINCIPALES (Ordre: Parentes en premier)

-- Table AGENT
CREATE TABLE Agent (
    Num_Agent VARCHAR(10) PRIMARY KEY,
    Nom_Agent VARCHAR(50) NOT NULL,
    Prenom_Agent VARCHAR(50) NOT NULL
);

-- Table CLIENT
CREATE TABLE Client (
    CIN_Cli VARCHAR(15) PRIMARY KEY,
    Nom_Cli VARCHAR(50) NOT NULL,
    Prenom_Cli VARCHAR(50) NOT NULL,
    Adresse VARCHAR(100),
    Telephone VARCHAR(15),
    Email VARCHAR(100)
);

-- Table VOITURE
CREATE TABLE Voiture (
    Matricule VARCHAR(20) PRIMARY KEY,
    Marque VARCHAR(50) NOT NULL,
    Modele VARCHAR(50) NOT NULL,
    Prix_Loc DECIMAL(10, 2) NOT NULL,
    -- 'Très bon état' est utilisé ici comme exemple tiré des données de test
    Etat_Veh ENUM('Disponible', 'Louée', 'En maintenance', 'Très bon état') NOT NULL, 
    Kilometrage INT
);

-- 3. CRÉATION DE LA TABLE DE RELATION (CONTRAT - la table enfant)

-- Toutes les tables référencées existent maintenant, donc les FK sont possibles
CREATE TABLE Contrat (
    Num_Contrat VARCHAR(20) PRIMARY KEY,
    Date_debut DATE NOT NULL,
    Date_Fin DATE NOT NULL,
    Montant_T DECIMAL(10, 2) NOT NULL,
    Mode_Paiement ENUM('Espèce', 'Carte', 'Virement') NOT NULL,

    -- Clés étrangères
    Num_Agent_FK VARCHAR(10) NOT NULL,
    Matricule_FK VARCHAR(20) NOT NULL,
    CIN_Cli_FK VARCHAR(15) NOT NULL,

    FOREIGN KEY (Num_Agent_FK) REFERENCES Agent(Num_Agent),
    FOREIGN KEY (Matricule_FK) REFERENCES Voiture(Matricule),
    FOREIGN KEY (CIN_Cli_FK) REFERENCES Client(CIN_Cli),
    
    -- Contrainte de validation
    CHECK (Date_Fin >= Date_debut)
);


-- 4. INSERTION DES DONNÉES DE TEST (Ordre: Parent -> Enfant)

-- Insertion de l'AGENT
INSERT INTO Agent (Num_Agent, Nom_Agent, Prenom_Agent) VALUES
('AG-205', 'BENSAID', 'Amina');

-- Insertion du CLIENT
INSERT INTO Client (CIN_Cli, Nom_Cli, Prenom_Cli, Adresse, Telephone, Email) VALUES
('L876543', 'EL FADLI', 'Karim', '24, Av. Hassan II, Casablanca', '06 12 34 56 78', 'karim.elfadli@gmail.com');

-- Insertion de la VOITURE
INSERT INTO Voiture (Matricule, Marque, Modele, Prix_Loc, Etat_Veh, Kilometrage) VALUES
('1234-AB-56', 'Dacia', 'Sandero', 150.00, 'Très bon état', 32850);

-- Insertion du CONTRAT
INSERT INTO Contrat (Num_Contrat, Date_debut, Date_Fin, Montant_T, Mode_Paiement, Num_Agent_FK, Matricule_FK, CIN_Cli_FK) VALUES
('LOC-2024-08921', '2024-04-10', '2024-04-17', 1050.00, 'Espèce', 'AG-205', '1234-AB-56', 'L876543');