-- Flyway migration: create initial schema for FastCar Location (MySQL)
-- Tables: agent, client, voiture, contrat

SET sql_mode='';

CREATE TABLE IF NOT EXISTS agent (
    num_agent BIGINT NOT NULL AUTO_INCREMENT,
    nom_agent VARCHAR(255),
    prenom_agent VARCHAR(255),
    PRIMARY KEY (num_agent)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS client (
    cin_cli VARCHAR(50) NOT NULL,
    nom_cli VARCHAR(255),
    prenom_cli VARCHAR(255),
    adresse VARCHAR(500),
    telephone VARCHAR(50),
    email VARCHAR(255),
    PRIMARY KEY (cin_cli)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS voiture (
    matricule VARCHAR(50) NOT NULL,
    marque VARCHAR(255),
    modele VARCHAR(255),
    prix_loc DECIMAL(10,2),
    etat_veh ENUM('DISPONIBLE','LOUEE','EN_MAINTENANCE') DEFAULT 'DISPONIBLE',
    kilometrage BIGINT,
    PRIMARY KEY (matricule)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS contrat (
    num_contract BIGINT NOT NULL AUTO_INCREMENT,
    date_debut DATE,
    date_fin DATE,
    montant_t DECIMAL(12,2),
    mode_paiement ENUM('ESPECE','CARTE','VIREMENT'),
    num_agent BIGINT,
    matricule VARCHAR(50),
    cin_cli VARCHAR(50),
    PRIMARY KEY (num_contract),
    CONSTRAINT FK_contrat_agent FOREIGN KEY (num_agent) REFERENCES agent(num_agent) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_contrat_voiture FOREIGN KEY (matricule) REFERENCES voiture(matricule) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT FK_contrat_client FOREIGN KEY (cin_cli) REFERENCES client(cin_cli) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

