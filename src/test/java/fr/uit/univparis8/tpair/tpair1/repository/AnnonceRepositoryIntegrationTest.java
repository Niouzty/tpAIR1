package fr.uit.univparis8.tpair.tpair1.repository;

import fr.uit.univparis8.tpair.tpair1.dao.AnnonceRepository;
import fr.uit.univparis8.tpair.tpair1.dao.UserRepository;
import fr.uit.univparis8.tpair.tpair1.dao.CategoryRepository;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.model.Category;
import fr.uit.univparis8.tpair.tpair1.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests d'intégration du Repository Annonce
 * Utilise une base de données H2 in-memory pour l'isolation des tests
 * 
 * À exécuter avec : mvn test ou mvn verify
 */
public class AnnonceRepositoryIntegrationTest {

    private EntityManagerFactory emf;
    private EntityManager em;
    private AnnonceRepository annonceRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        // Initialiser la base H2 en mémoire
        emf = Persistence.createEntityManagerFactory("h2-test");
        em = emf.createEntityManager();
        
        annonceRepository = new AnnonceRepository();
        userRepository = new UserRepository();
        categoryRepository = new CategoryRepository();
        
        // Créer les tables
        createTestData();
    }

    @AfterEach
    public void tearDown() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

    /**
     * Crée les données de test : User + Category + Annonces
     */
    private void createTestData() {
        try {
            em.getTransaction().begin();

            // Créer un utilisateur
            User user = new User();
            user.setUsername("testuser");
            user.setEmail("test@mail.com");
            user.setPassword("password123");
            userRepository.create(em, user);

            // Créer une catégorie
            Category category = new Category();
            category.setLabel("Immobilier");
            categoryRepository.create(em, category);

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erreur lors de la création des données de test", e);
        }
    }

    /**
     * Test 1 : Créer une annonce avec statut DRAFT
     */
    @Test
    public void testCreateAnnonce() {
        em.getTransaction().begin();

        User user = userRepository.findByUsername(em, "testuser");
        Category category = categoryRepository.findByLabel(em, "Immobilier");

        Annonce annonce = new Annonce();
        annonce.setTitle("Appartement 3 pièces");
        annonce.setDescription("Bel appartement au centre-ville");
        annonce.setAdress("123 Rue de Paris");
        annonce.setMail("seller@mail.com");
        annonce.setAuthor(user);
        annonce.setCategory(category);
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setDate(LocalDateTime.now());

        annonceRepository.create(em, annonce);
        em.getTransaction().commit();

        assertNotNull(annonce.getId());
        assertEquals("Appartement 3 pièces", annonce.getTitle());
        assertEquals(AnnonceStatus.DRAFT, annonce.getStatus());
    }

    /**
     * Test 2 : Lire une annonce par ID
     */
    @Test
    public void testFindById() {
        // Créer une annonce
        em.getTransaction().begin();
        User user = userRepository.findByUsername(em, "testuser");
        Category category = categoryRepository.findByLabel(em, "Immobilier");

        Annonce annonce = new Annonce();
        annonce.setTitle("Maison avec jardin");
        annonce.setDescription("Maison spacieuse");
        annonce.setAdress("456 Avenue du Bois");
        annonce.setMail("owner@mail.com");
        annonce.setAuthor(user);
        annonce.setCategory(category);
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setDate(LocalDateTime.now());

        annonceRepository.create(em, annonce);
        Long id = annonce.getId();
        em.getTransaction().commit();

        // Lire l'annonce
        Annonce found = annonceRepository.findById(em, id);

        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("Maison avec jardin", found.getTitle());
    }

    /**
     * Test 3 : Mettre à jour une annonce
     */
    @Test
    public void testUpdateAnnonce() {
        // Créer et mettre à jour une annonce
        em.getTransaction().begin();
        User user = userRepository.findByUsername(em, "testuser");
        Category category = categoryRepository.findByLabel(em, "Immobilier");

        Annonce annonce = new Annonce();
        annonce.setTitle("Studio ancien prix");
        annonce.setDescription("Description initiale");
        annonce.setAdress("789 Rue du Commerce");
        annonce.setMail("contact@mail.com");
        annonce.setAuthor(user);
        annonce.setCategory(category);
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setDate(LocalDateTime.now());

        annonceRepository.create(em, annonce);
        Long id = annonce.getId();
        em.getTransaction().commit();

        // Mettre à jour
        em.getTransaction().begin();
        Annonce toUpdate = annonceRepository.findById(em, id);
        toUpdate.setTitle("Studio nouveau prix");
        toUpdate.setDescription("Description mise à jour");
        annonceRepository.update(em, toUpdate);
        em.getTransaction().commit();

        // Vérifier
        Annonce updated = annonceRepository.findById(em, id);
        assertEquals("Studio nouveau prix", updated.getTitle());
        assertEquals("Description mise à jour", updated.getDescription());
    }

    /**
     * Test 4 : Supprimer une annonce
     */
    @Test
    public void testDeleteAnnonce() {
        // Créer une annonce
        em.getTransaction().begin();
        User user = userRepository.findByUsername(em, "testuser");
        Category category = categoryRepository.findByLabel(em, "Immobilier");

        Annonce annonce = new Annonce();
        annonce.setTitle("Annonce à supprimer");
        annonce.setDescription("Sera supprimée");
        annonce.setAdress("999 Rue Test");
        annonce.setMail("test@mail.com");
        annonce.setAuthor(user);
        annonce.setCategory(category);
        annonce.setStatus(AnnonceStatus.ARCHIVED);  // Doit être ARCHIVED pour pouvoir supprimer
        annonce.setDate(LocalDateTime.now());

        annonceRepository.create(em, annonce);
        Long id = annonce.getId();
        em.getTransaction().commit();

        // Supprimer
        em.getTransaction().begin();
        boolean deleted = annonceRepository.delete(em, id);
        em.getTransaction().commit();

        assertTrue(deleted);

        // Vérifier que l'annonce n'existe plus
        Annonce notFound = annonceRepository.findById(em, id);
        assertNull(notFound);
    }

    /**
     * Test 5 : Rechercher des annonces avec pagination
     */
    @Test
    public void testSearchWithPagination() {
        // Créer plusieurs annonces
        em.getTransaction().begin();
        User user = userRepository.findByUsername(em, "testuser");
        Category category = categoryRepository.findByLabel(em, "Immobilier");

        for (int i = 0; i < 15; i++) {
            Annonce annonce = new Annonce();
            annonce.setTitle("Annonce " + i);
            annonce.setDescription("Description " + i);
            annonce.setAdress("Adresse " + i);
            annonce.setMail("mail" + i + "@test.com");
            annonce.setAuthor(user);
            annonce.setCategory(category);
            annonce.setStatus(AnnonceStatus.DRAFT);
            annonce.setDate(LocalDateTime.now());
            annonceRepository.create(em, annonce);
        }
        em.getTransaction().commit();

        // Rechercher page 0, size 10
        List<Annonce> page1 = annonceRepository.search(em, null, null, null, 0, 10);
        assertEquals(10, page1.size());

        // Rechercher page 1, size 10
        List<Annonce> page2 = annonceRepository.search(em, null, null, null, 1, 10);
        assertEquals(5, page2.size());
    }

    /**
     * Test 6 : Rechercher par statut
     */
    @Test
    public void testSearchByStatus() {
        em.getTransaction().begin();
        User user = userRepository.findByUsername(em, "testuser");
        Category category = categoryRepository.findByLabel(em, "Immobilier");

        // Créer 3 annonces DRAFT et 2 PUBLISHED
        for (int i = 0; i < 3; i++) {
            Annonce annonce = new Annonce();
            annonce.setTitle("Draft " + i);
            annonce.setDescription("Description");
            annonce.setAdress("Adresse");
            annonce.setMail("mail@test.com");
            annonce.setAuthor(user);
            annonce.setCategory(category);
            annonce.setStatus(AnnonceStatus.DRAFT);
            annonce.setDate(LocalDateTime.now());
            annonceRepository.create(em, annonce);
        }

        for (int i = 0; i < 2; i++) {
            Annonce annonce = new Annonce();
            annonce.setTitle("Published " + i);
            annonce.setDescription("Description");
            annonce.setAdress("Adresse");
            annonce.setMail("mail@test.com");
            annonce.setAuthor(user);
            annonce.setCategory(category);
            annonce.setStatus(AnnonceStatus.PUBLISHED);
            annonce.setDate(LocalDateTime.now());
            annonceRepository.create(em, annonce);
        }
        em.getTransaction().commit();

        // Rechercher par statut PUBLISHED
        List<Annonce> published = annonceRepository.search(em, null, null, AnnonceStatus.PUBLISHED, 0, 100);
        assertEquals(2, published.size());

        // Rechercher par statut DRAFT
        List<Annonce> draft = annonceRepository.search(em, null, null, AnnonceStatus.DRAFT, 0, 100);
        assertEquals(3, draft.size());
    }

    /**
     * Test 7 : Vérifier la concurrence optimiste avec @Version
     */
    @Test
    public void testOptimisticLocking() {
        em.getTransaction().begin();
        User user = userRepository.findByUsername(em, "testuser");
        Category category = categoryRepository.findByLabel(em, "Immobilier");

        Annonce annonce = new Annonce();
        annonce.setTitle("Test concurrence");
        annonce.setDescription("Test @Version");
        annonce.setAdress("Adresse test");
        annonce.setMail("test@mail.com");
        annonce.setAuthor(user);
        annonce.setCategory(category);
        annonce.setStatus(AnnonceStatus.DRAFT);
        annonce.setDate(LocalDateTime.now());

        annonceRepository.create(em, annonce);
        Long version1 = annonce.getVersion();
        Long id = annonce.getId();
        em.getTransaction().commit();

        // Mettre à jour
        em.getTransaction().begin();
        Annonce toUpdate = annonceRepository.findById(em, id);
        toUpdate.setTitle("Titre modifié");
        annonceRepository.update(em, toUpdate);
        em.getTransaction().commit();

        // Vérifier que la version a changé
        Annonce updated = annonceRepository.findById(em, id);
        Long version2 = updated.getVersion();

        assertNotNull(version1);
        assertNotNull(version2);
        assertTrue(version2 > version1, "La version devrait avoir augmenté avec @Version");
    }
}
