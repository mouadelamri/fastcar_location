-- Flyway migration: create initial schema for FastCar Location (H2)
-- Tables: agent, client, voiture, contrat

CREATE TABLE IF NOT EXISTS agent (
    num_agent BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_agent VARCHAR(255),
    prenom_agent VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS client (
    cin_cli VARCHAR(50) PRIMARY KEY,
    nom_cli VARCHAR(255),
    prenom_cli VARCHAR(255),
    adresse VARCHAR(500),
    telephone VARCHAR(50),
    email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS voiture (
    matricule VARCHAR(50) PRIMARY KEY,
    marque VARCHAR(255),
    modele VARCHAR(255),
    prix_loc DECIMAL(10,2),
    etat_veh VARCHAR(50) DEFAULT 'DISPONIBLE',
    kilometrage BIGINT
);

CREATE TABLE IF NOT EXISTS contrat (
    num_contract BIGINT AUTO_INCREMENT PRIMARY KEY,
    date_debut DATE,
    date_fin DATE,
    montant_t DECIMAL(12,2),
    mode_paiement VARCHAR(50),
    num_agent BIGINT,
    matricule VARCHAR(50),
    cin_cli VARCHAR(50),
    CONSTRAINT FK_contrat_agent FOREIGN KEY (num_agent) REFERENCES agent(num_agent) ON DELETE SET NULL,
    CONSTRAINT FK_contrat_voiture FOREIGN KEY (matricule) REFERENCES voiture(matricule) ON DELETE SET NULL,
    CONSTRAINT FK_contrat_client FOREIGN KEY (cin_cli) REFERENCES client(cin_cli) ON DELETE SET NULL
);
