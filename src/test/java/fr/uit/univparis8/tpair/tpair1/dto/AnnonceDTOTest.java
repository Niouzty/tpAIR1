package fr.uit.univparis8.tpair.tpair1.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AnnonceDTOTest {

    @Test
    void builderShouldPopulateFields() {
        LocalDateTime now = LocalDateTime.now();

        AnnonceDTO dto = AnnonceDTO.builder()
                .id(1L)
                .titre("Titre")
                .description("Description")
                .categorie("Maison")
                .prix(10.0)
                .statut("DRAFT")
                .auteurId(99L)
                .dateCreation(now)
                .dateModification(now)
                .version(2L)
                .build();

        assertEquals(1L, dto.id);
        assertEquals("Titre", dto.titre);
        assertEquals("Description", dto.description);
        assertEquals("Maison", dto.categorie);
        assertEquals("DRAFT", dto.statut);
        assertEquals(99L, dto.auteurId);
        assertEquals(2L, dto.version);
    }
}
