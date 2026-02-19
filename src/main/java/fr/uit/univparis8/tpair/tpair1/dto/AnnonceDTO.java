package fr.uit.univparis8.tpair.tpair1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;


public class AnnonceDTO {
    
    public Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    public String titre;
    
    @NotBlank(message = "La description est obligatoire")
    public String description;
    
    public String categorie;
    
    @Positive(message = "Le prix doit être positif")
    public Double prix;
    
    public String statut;
    
    public Long auteurId;
    
    public LocalDateTime dateCreation;
    
    public LocalDateTime dateModification;

    public Long version;
    public AnnonceDTO() {}

    public AnnonceDTO(Long id, String titre, String description, String categorie,
                     Double prix, String statut, Long auteurId, LocalDateTime dateCreation,
                     LocalDateTime dateModification, Long version) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.statut = statut;
        this.auteurId = auteurId;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
        this.version = version;
    }
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String titre;
        private String description;
        private String categorie;
        private Double prix;
        private String statut;
        private Long auteurId;
        private LocalDateTime dateCreation;
        private LocalDateTime dateModification;
        private Long version;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder titre(String titre) {
            this.titre = titre;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder categorie(String categorie) {
            this.categorie = categorie;
            return this;
        }

        public Builder prix(Double prix) {
            this.prix = prix;
            return this;
        }

        public Builder statut(String statut) {
            this.statut = statut;
            return this;
        }

        public Builder auteurId(Long auteurId) {
            this.auteurId = auteurId;
            return this;
        }

        public Builder dateCreation(LocalDateTime dateCreation) {
            this.dateCreation = dateCreation;
            return this;
        }

        public Builder dateModification(LocalDateTime dateModification) {
            this.dateModification = dateModification;
            return this;
        }

        public Builder version(Long version) {
            this.version = version;
            return this;
        }

        public AnnonceDTO build() {
            return new AnnonceDTO(id, titre, description, categorie, prix, statut,
                                auteurId, dateCreation, dateModification, version);
        }
    }
}
