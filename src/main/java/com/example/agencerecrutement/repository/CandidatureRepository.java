package com.example.agencerecrutement.repository;

import com.example.agencerecrutement.model.Candidature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatureRepository extends JpaRepository<Candidature, Long> {
    List<Candidature> findByOffreIdOffre(Long idOffre);
    List<Candidature> findByDemandeurIdUtilisateur(Long idDemandeur);
    Optional<Candidature> findByDemandeurIdUtilisateurAndOffreIdOffre(Long idDemandeur, Long idOffre);
    boolean existsByDemandeurIdUtilisateurAndOffreIdOffre(Long idDemandeur, Long idOffre);
}


