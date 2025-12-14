-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS bibliotheque;
USE bibliotheque;

-- Drop tables in correct order (considering foreign key constraints)
DROP TABLE IF EXISTS prets;
DROP TABLE IF EXISTS exemplaires;
DROP TABLE IF EXISTS documents;
DROP TABLE IF EXISTS abonnes;

-- Create abonnes table (corrected structure)
CREATE TABLE abonnes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cin VARCHAR(20) NOT NULL UNIQUE,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    adresse TEXT NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    date_abonnement DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create documents table
CREATE TABLE documents (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cote VARCHAR(20) UNIQUE NOT NULL,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255) NOT NULL,
    theme VARCHAR(100) NOT NULL,
    type_document ENUM('OUVRAGE', 'MEMOIRE') NOT NULL,
    annee_etude ENUM('LICENCE', 'MASTER', 'DOCTORAT'),
    date_parution DATE,
    isbn VARCHAR(20),
    editeur VARCHAR(255)
);

-- Create exemplaires table
CREATE TABLE exemplaires (
    id INT PRIMARY KEY AUTO_INCREMENT,
    document_id INT,
    libelle VARCHAR(50) NOT NULL,
    localisation VARCHAR(100) NOT NULL,
    date_acquisition DATE NOT NULL,
    statut ENUM('DISPONIBLE', 'EMPRUNTE') DEFAULT 'DISPONIBLE',
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Create prets table
CREATE TABLE prets (
    id INT PRIMARY KEY AUTO_INCREMENT,
    abonne INT,
    document INT,
    datePret DATE,
    dateRetourPrevu DATE,
    statut VARCHAR(20),
    dateRetourEffective DATE,
    FOREIGN KEY (abonne) REFERENCES abonnes(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (document) REFERENCES documents(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Insert sample data

INSERT INTO documents (cote, titre, auteur, theme, type_document, annee_etude, date_parution, isbn, editeur) VALUES
('LI-789-2023', 'Algorithmes Avancés', 'Martin Dubois', 'Informatique', 'MEMOIRE', 'MASTER', '2023-09-15', NULL, NULL),
('U-456-2024', 'Java Programming', 'Sophie Martin', 'Informatique', 'OUVRAGE', NULL, '2024-01-20', '978-3-1234-5678-9', 'Éditions Tech');

INSERT INTO abonnes (cin, nom, prenom, adresse, telephone, date_abonnement) VALUES
('145124187', 'BEN HASSINE', 'AMENI', 'ARIANA', '54 822 662', '2025-12-02'),
('14511789', 'MARWA', 'SAIDI', 'TUNIS', '54879214', '2025-12-02');


INSERT INTO exemplaires (document_id, libelle, localisation, date_acquisition, statut) VALUES
(1, 'LI-789-A', 'Rayon A2', '2023-10-15', 'DISPONIBLE'),
(1, 'LI-789-B', 'Rayon A2', '2023-10-20', 'DISPONIBLE'),
(2, 'U-456-A', 'Rayon B1', '2024-02-01', 'DISPONIBLE');

INSERT INTO prets (abonne, document, datePret, dateRetourPrevu, statut, dateRetourEffective) VALUES
(1, 1, '2025-12-13', '2025-12-13', 'En cours', '2025-12-13'),
(1, 1, '2025-12-13', '2025-12-13', 'En cours', '2025-12-13'),
(1, 1, '2025-12-13', '2025-12-13', 'Retourné', '2025-12-13'),
(1, 2, '2025-12-14', '2025-12-12', 'Retourné', '2026-01-01');