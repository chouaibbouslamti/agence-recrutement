-- ========================================
-- DONNÉES DE TEST POUR L'AGENCE DE RECRUTEMENT
-- ========================================

-- Nettoyage des tables (ordre inverse des contraintes)
DELETE FROM publications_offres;
DELETE FROM recrutements;
DELETE FROM candidatures;
DELETE FROM abonnements;
DELETE FROM editions;
DELETE FROM offres;
DELETE FROM journaux;
DELETE FROM categories;
DELETE FROM utilisateurs;

-- ========================================
-- UTILISATEURS
-- ========================================

-- 1. Administrateur
INSERT INTO utilisateurs (login, mot_de_passe, role, type_utilisateur, actif) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ADMINISTRATEUR', 'ADMINISTRATEUR', true);

-- 2. Entreprises
INSERT INTO utilisateurs (login, mot_de_passe, role, type_utilisateur, actif, raison_sociale, adresse, telephone, description_activite) 
VALUES 
('techcorp', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ENTREPRISE', 'ENTREPRISE', true, 'TechCorp Solutions', '123 Avenue de la Technologie, Paris', '0140123456', 'Développement de logiciels et solutions cloud pour entreprises'),
('innovent', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ENTREPRISE', 'ENTREPRISE', true, 'InnoVent Group', '456 Rue de l''Innovation, Lyon', '0478123456', 'Startup spécialisée en intelligence artificielle et machine learning'),
('servicesplus', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ENTREPRISE', 'ENTREPRISE', true, 'Services Plus', '789 Boulevard du Service, Marseille', '0491123456', 'Société de services informatiques et consulting'),
('retailmaster', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ENTREPRISE', 'ENTREPRISE', true, 'Retail Master France', '321 Rue du Commerce, Bordeaux', '0556123456', 'Chaîne de magasins et solutions e-commerce');

-- 3. Demandeurs d'emploi
INSERT INTO utilisateurs (login, mot_de_passe, role, type_utilisateur, actif, nom, prenom, adresse, telephone, fax, diplome, experience, salaire_souhaite) 
VALUES 
('jdupont', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'DEMANDEUR_EMPLOI', 'DEMANDEUR_EMPLOI', true, 'Dupont', 'Jean', '15 Rue de la Paix, Paris', '0612345678', '0140123456', 'Master en Informatique', 3, 45000.0),
('mmartin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'DEMANDEUR_EMPLOI', 'DEMANDEUR_EMPLOI', true, 'Martin', 'Marie', '22 Avenue des Champs-Élysées, Paris', '0623456789', '0140234567', 'Licence Marketing Digital', 2, 38000.0),
('plegrand', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'DEMANDEUR_EMPLOI', 'DEMANDEUR_EMPLOI', true, 'Legrand', 'Pierre', '8 Place Bellecour, Lyon', '0634567890', '0478345678', 'DUT Réseaux et Télécoms', 5, 42000.0),
('sbernard', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'DEMANDEUR_EMPLOI', 'DEMANDEUR_EMPLOI', true, 'Bernard', 'Sophie', '55 Canebière, Marseille', '0645678901', '0491456789', 'BTS Comptabilité et Gestion', 4, 35000.0),
('tleroi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'DEMANDEUR_EMPLOI', 'DEMANDEUR_EMPLOI', true, 'Leroi', 'Thomas', '12 Rue Sainte-Catherine, Bordeaux', '0656789012', '0556456789', 'Master en Management', 1, 32000.0),
('cfabre', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'DEMANDEUR_EMPLOI', 'DEMANDEUR_EMPLOI', true, 'Fabre', 'Claire', '99 Avenue Foch, Nice', '0667890123', '0493567890', 'Licence Communication', 6, 48000.0);

-- ========================================
-- CATÉGORIES DE JOURNAUX
-- ========================================

INSERT INTO categories (libelle) VALUES 
('Informatique et Technologie'),
('Emploi et Carrière'),
('Économie et Finance'),
('Presse Régionale'),
('Spécialisé Secteur');

-- ========================================
-- JOURNAUX
-- ========================================

INSERT INTO journaux (code_journal, nom, periodicite, langue, id_categorie) VALUES 
('INFO-01', 'Le Monde Informatique', 'HEBDOMADAIRE', 'Français', 1),
('INFO-02', '01Net', 'MENSUEL', 'Français', 1),
('EMPLOI-01', "L'Offre d'Emploi", 'HEBDOMADAIRE', 'Français', 2),
('EMPLOI-02', 'Aujourd''hui Emploi', 'MENSUEL', 'Français', 2),
('ECO-01', 'Les Echos', 'QUOTIDIEN', 'Français', 3),
('REG-01', 'Le Progrès', 'QUOTIDIEN', 'Français', 4),
('SPEC-01', 'Tech Magazine', 'MENSUEL', 'Français', 5);

-- ========================================
-- ÉDITIONS DES JOURNAUX
-- ========================================

INSERT INTO editions (numero_edition, date_parution, code_journal) VALUES 
-- Le Monde Informatique
(1525, '2024-12-01', 'INFO-01'),
(1526, '2024-12-08', 'INFO-01'),
(1527, '2024-12-15', 'INFO-01'),
-- 01Net
(245, '2024-12-01', 'INFO-02'),
(246, '2025-01-01', 'INFO-02'),
-- L'Offre d'Emploi
(1205, '2024-12-02', 'EMPLOI-01'),
(1206, '2024-12-09', 'EMPLOI-01'),
(1207, '2024-12-16', 'EMPLOI-01'),
-- Aujourd'hui Emploi
(89, '2024-12-01', 'EMPLOI-02'),
(90, '2025-01-01', 'EMPLOI-02'),
-- Les Echos
(23456, '2024-12-01', 'ECO-01'),
(23457, '2024-12-02', 'ECO-01'),
(23458, '2024-12-03', 'ECO-01'),
-- Le Progrès
(12345, '2024-12-01', 'REG-01'),
(12346, '2024-12-02', 'REG-01'),
-- Tech Magazine
(34, '2024-12-01', 'SPEC-01'),
(35, '2025-01-01', 'SPEC-01');

-- ========================================
-- ABONNEMENTS DES ENTREPRISES
-- ========================================

INSERT INTO abonnements (etat, date_expiration, id_entreprise, code_journal) VALUES 
-- TechCorp Solutions
('ACTIF', '2025-12-31', 2, 'INFO-01'),
('ACTIF', '2025-06-30', 2, 'EMPLOI-01'),
('ACTIF', '2025-12-31', 2, 'SPEC-01'),
-- InnoVent Group
('ACTIF', '2025-12-31', 3, 'INFO-01'),
('ACTIF', '2025-12-31', 3, 'INFO-02'),
-- Services Plus
('ACTIF', '2025-09-30', 4, 'EMPLOI-01'),
('ACTIF', '2025-09-30', 4, 'ECO-01'),
-- Retail Master France
('ACTIF', '2025-12-31', 5, 'EMPLOI-02'),
('ACTIF', '2025-12-31', 5, 'REG-01');

-- ========================================
-- OFFRES D'EMPLOI
-- ========================================

INSERT INTO offres (titre, competences, experience_requise, nb_postes, etat, id_entreprise) VALUES 
-- TechCorp Solutions
('Développeur Java Senior', 'Java, Spring Boot, Hibernate, MySQL, Git, Méthodes Agile', 5, 2, 'ACTIVE', 2),
('Développeur Web Full Stack', 'React, Node.js, MongoDB, Docker, CI/CD', 3, 1, 'ACTIVE', 2),
('Administrateur Système Linux', 'Linux, Bash, Docker, Kubernetes, AWS', 4, 1, 'ACTIVE', 2),
-- InnoVent Group
('Data Scientist', 'Python, TensorFlow, Scikit-learn, SQL, Machine Learning', 3, 2, 'ACTIVE', 3),
('Ingénieur IA/Machine Learning', 'Python, PyTorch, NLP, Computer Vision, Deep Learning', 4, 1, 'ACTIVE', 3),
('Développeur Python', 'Python, Django, PostgreSQL, REST API, Testing', 2, 3, 'ACTIVE', 3),
-- Services Plus
('Consultant IT Junior', 'Windows Server, Active Directory, Office 365, Réseaux', 1, 2, 'ACTIVE', 4),
('Technicien Support Informatique', 'Windows, macOS, Office, Support utilisateur, Ticketing', 0, 3, 'ACTIVE', 4),
-- Retail Master France
('Chef de Produit Digital', 'Marketing digital, E-commerce, Analytics, SEO/SEM', 3, 1, 'ACTIVE', 5),
('Responsable Logistique', 'Supply Chain, WMS, ERP, Transport, Optimisation', 5, 1, 'ACTIVE', 5);

-- ========================================
-- PUBLICATIONS D'OFFRES
-- ========================================

INSERT INTO publications_offres (date_publication, id_offre, id_abonnement, id_edition) VALUES 
-- Publications TechCorp
('2024-12-01', 1, 1, 1),
('2024-12-08', 1, 1, 2),
('2024-12-02', 2, 2, 6),
('2024-12-09', 2, 2, 7),
('2024-12-15', 3, 3, 3),
-- Publications InnoVent
('2024-12-01', 4, 4, 1),
('2024-12-01', 5, 5, 4),
('2024-12-02', 6, 4, 6),
('2024-12-09', 6, 4, 7),
-- Publications Services Plus
('2024-12-02', 7, 6, 6),
('2024-12-09', 7, 6, 7),
('2024-12-01', 8, 7, 10),
-- Publications Retail Master
('2024-12-01', 9, 8, 9),
('2024-12-02', 9, 9, 11),
('2025-01-01', 10, 9, 15);

-- ========================================
-- CANDIDATURES
-- ========================================

INSERT INTO candidatures (date_candidature, id_demandeur, id_offre, id_edition) VALUES 
-- Candidatures de Jean Dupont
('2024-12-03', 6, 1, 1),
('2024-12-05', 6, 2, 6),
('2024-12-10', 6, 4, 1),
-- Candidatures de Marie Martin
('2024-12-04', 7, 9, 9),
('2024-12-06', 7, 10, 11),
-- Candidatures de Pierre Legrand
('2024-12-03', 8, 3, 3),
('2024-12-05', 8, 7, 6),
('2024-12-08', 8, 8, 10),
-- Candidatures de Sophie Bernard
('2024-12-04', 9, 7, 6),
('2024-12-06', 9, 10, 11),
-- Candidatures de Thomas Leroi
('2024-12-05', 10, 9, 9),
('2024-12-07', 10, 5, 4),
-- Candidatures de Claire Fabre
('2024-12-03', 11, 6, 6),
('2024-12-06', 11, 9, 9);

-- ========================================
-- RECRUTEMENTS (simulant des candidatures acceptées)
-- ========================================

-- Note: La table Recrutement n'est pas directement visible dans les modèles,
-- mais on suppose qu'elle existe avec id_offre et id_demandeur
-- Si la table n'existe pas, cette section peut être ignorée

/*
INSERT INTO recrutements (date_recrutement, id_offre, id_demandeur) VALUES 
('2024-12-15', 1, 6),  -- Jean Dupont recruté par TechCorp
('2024-12-16', 9, 7),  -- Marie Martin recrutée par Retail Master
('2024-12-17', 7, 8);  -- Pierre Legrand recruté par Services Plus
*/

-- ========================================
-- COMPTES UTILISATEURS RÉCAPITULATIF
-- ========================================

/*
MOT DE PASSE UNIVERSEL: password123
(Hashé avec BCrypt: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa)

COMPTES DISPONIBLES:

1. ADMINISTRATEUR:
   - Login: admin
   - Mot de passe: password123

2. ENTREPRISES:
   - Login: techcorp (TechCorp Solutions)
   - Login: innovent (InnoVent Group)  
   - Login: servicesplus (Services Plus)
   - Login: retailmaster (Retail Master France)
   Mot de passe: password123

3. DEMANDEURS D'EMPLOI:
   - Login: jdupont (Jean Dupont)
   - Login: mmartin (Marie Martin)
   - Login: plegrand (Pierre Legrand)
   - Login: sbernard (Sophie Bernard)
   - Login: tleroi (Thomas Leroi)
   - Login: cfabre (Claire Fabre)
   Mot de passe: password123

STATISTIQUES:
- 4 entreprises
- 6 demandeurs d'emploi  
- 10 offres d'emploi
- 7 journaux
- 15 éditions
- 9 abonnements
- 13 publications
- 13 candidatures
*/
