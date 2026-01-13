-- Données initiales pour FastCar Location

-- Agents
INSERT INTO Agent (Num_Agent, Nom_Agent, Prenom_Agent) VALUES
('AG-001', 'ALAMI', 'Mohamed'),
('AG-002', 'FARES', 'Lamia'),
('AG-003', 'KARIM', 'Hassan'),
('AG-205', 'BENSAID', 'Amina');

-- Clients
INSERT INTO Client (CIN_Cli, Nom_Cli, Prenom_Cli, Adresse, Telephone, Email) VALUES
('L876543', 'EL FADLI', 'Karim', '24, Av. Hassan II, Casablanca', '06 12 34 56 78', 'karim.elfadli@gmail.com'),
('AB123456', 'EL FILALI', 'Fatima', '123 Rue Mohammed V, Casablanca', '0612345678', 'fatima.elfilali@email.com'),
('CD789012', 'BENNOUNA', 'Ahmed', '45 Avenue Hassan II, Marrakech', '0623456789', 'ahmed.bennouna@email.com'),
('EF345678', 'CHRAIBI', 'Samira', '78 Boulevard des FAR, Rabat', '0634567890', 'samira.chraibi@email.com'),
('GH901234', 'TAZI', 'Youssef', '12 Rue de la Liberté, Fès', '0645678901', 'youssef.tazi@email.com');

-- Voitures
INSERT INTO Voiture (Matricule, Marque, Modele, Prix_Loc, Etat_Veh, Kilometrage) VALUES
('1234-AB-56', 'Dacia', 'Sandero', 150.00, 'Disponible', 32850),
('AB123CD', 'Renault', 'Clio', 200.00, 'Disponible', 25000),
('XY789ZT', 'Peugeot', '308', 250.00, 'Disponible', 32000),
('EF456GH', 'Dacia', 'Logan', 180.00, 'Disponible', 18000),
('IJ789KL', 'Toyota', 'Corolla', 300.00, 'Disponible', 45000),
('MN123OP', 'Hyundai', 'i20', 220.00, 'En maintenance', 30000),
('QR456ST', 'Ford', 'Fiesta', 190.00, 'Disponible', 22000),
('UV789WX', 'Volkswagen', 'Golf', 280.00, 'Disponible', 35000);

-- Contrats
INSERT INTO Contrat (Num_Contrat, Date_debut, Date_Fin, Montant_T, Mode_Paiement, Num_Agent_FK, Matricule_FK, CIN_Cli_FK) VALUES
('LOC-2024-08921', '2024-04-10', '2024-04-17', 1050.00, 'Espèce', 'AG-205', '1234-AB-56', 'L876543'),
('LOC-2024-001', '2024-01-15', '2024-01-20', 1000.00, 'Carte', 'AG-001', 'AB123CD', 'AB123456'),
('LOC-2024-002', '2024-02-01', '2024-02-10', 2250.00, 'Espèce', 'AG-002', 'XY789ZT', 'CD789012'),
('LOC-2024-003', '2024-03-05', '2024-03-12', 1260.00, 'Virement', 'AG-003', 'EF456GH', 'EF345678'),
('LOC-2024-004', '2024-04-15', '2024-04-22', 2100.00, 'Carte', 'AG-001', 'UV789WX', 'GH901234');