package fr.uit.univparis8.tpair.tpair1.service;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class AnnonceServiceTest {

    private User user1, user2;
    private Annonce annonce1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        annonce1 = new Annonce();
        annonce1.setId(1L);
        annonce1.setTitle("Annonce Test");
        annonce1.setAuthor(user1);
        annonce1.setStatus(AnnonceStatus.DRAFT);
    }

    @Test
    void testAnnonceOwnershipLogic() {
        assertEquals(1L, annonce1.getAuthor().getId());
    }

    @Test
    void testStatusTransition() {
        annonce1.setStatus(AnnonceStatus.PUBLISHED);
        assertEquals(AnnonceStatus.PUBLISHED, annonce1.getStatus());

        annonce1.setStatus(AnnonceStatus.ARCHIVED);
        assertEquals(AnnonceStatus.ARCHIVED, annonce1.getStatus());
    }

    @Test
    void testAccessControl() {
        boolean isOwner = annonce1.getAuthor().getId().equals(1L);
        assertTrue(isOwner);

        boolean isOwner2 = annonce1.getAuthor().getId().equals(2L);
        assertFalse(isOwner2);
    }

    @Test
    void testValidationRules() {
        Annonce valid = new Annonce();
        valid.setTitle("Test");
        valid.setDescription("Desc");
        valid.setAdress("Addr");
        valid.setMail("test@test.com");

        assertNotNull(valid.getTitle());
        assertNotNull(valid.getDescription());
        assertNotNull(valid.getMail());
    }
}
