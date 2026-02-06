package fr.uit.univparis8.tpair.tpair1.service;

import fr.uit.univparis8.tpair.tpair1.dao.AnnonceRepository;
import fr.uit.univparis8.tpair.tpair1.dao.CategoryRepository;
import fr.uit.univparis8.tpair.tpair1.dao.UserRepository;
import fr.uit.univparis8.tpair.tpair1.jpa.JPAUtil;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.model.Category;
import fr.uit.univparis8.tpair.tpair1.model.User;
import jakarta.persistence.EntityManager;

import java.util.List;

public class AnnonceService {

    private final AnnonceRepository annonceRepo = new AnnonceRepository();
    private final UserRepository userRepo = new UserRepository();
    private final CategoryRepository categoryRepo = new CategoryRepository();

    private User ensureDefaultUser(EntityManager em) {
        User u = userRepo.findByUsername(em, "admin");
        if (u == null) {
            u = new User();
            u.setUsername("admin");
            u.setEmail("admin@mail.com");
            u.setPassword("admin");
            userRepo.create(em, u);
        }
        return u;
    }

    private Category ensureDefaultCategory(EntityManager em) {
        Category c = categoryRepo.findByLabel(em, "General");
        if (c == null) {
            c = new Category();
            c.setLabel("General");
            categoryRepo.create(em, c);
        }
        return c;
    }

<<<<<<< HEAD
    public Annonce create(Annonce a) {
=======
    public Annonce create(Annonce a, Long userId) {
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        EntityManager em = JPAUtil.getEntityManager();
        try {
            validateAnnonce(a);
            em.getTransaction().begin();

<<<<<<< HEAD
            User author = ensureDefaultUser(em);
=======
            // Charger l'utilisateur connecté comme auteur
            User author = userRepo.findById(em, userId);
            if (author == null) {
                em.getTransaction().rollback();
                throw new ValidationException("Utilisateur non trouvé");
            }

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
            Category cat = ensureDefaultCategory(em);

            a.setAuthor(author);
            a.setCategory(cat);

            if (a.getStatus() == null) a.setStatus(AnnonceStatus.DRAFT);

            annonceRepo.create(em, a);

            em.getTransaction().commit();
            return a;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public Annonce findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return annonceRepo.findById(em, id);
        } finally {
            em.close();
        }
    }

    public List<Annonce> listAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return annonceRepo.search(em, null, null, null, 0, 100);
        } finally {
            em.close();
        }
    }

<<<<<<< HEAD
    public boolean delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
=======
    public boolean delete(Long id, Long userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Annonce a = annonceRepo.findById(em, id);
            if (a == null) {
                em.getTransaction().rollback();
                return false;
            }

            // Vérifier que l'utilisateur est le propriétaire
            if (!a.getAuthor().getId().equals(userId)) {
                em.getTransaction().rollback();
                return false;
            }

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
            boolean ok = annonceRepo.delete(em, id);
            em.getTransaction().commit();
            return ok;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

<<<<<<< HEAD
    public boolean update(Annonce a) {
=======
    public boolean update(Annonce a, Long userId) {
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Annonce existing = annonceRepo.findById(em, a.getId());
            if (existing == null) {
                em.getTransaction().rollback();
                return false;
            }

<<<<<<< HEAD
=======
            // Vérifier que l'utilisateur est le propriétaire
            if (!existing.getAuthor().getId().equals(userId)) {
                em.getTransaction().rollback();
                return false;
            }

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
            existing.setTitle(a.getTitle());
            existing.setDescription(a.getDescription());
            existing.setAdress(a.getAdress());
            existing.setMail(a.getMail());

            annonceRepo.update(em, existing);

            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

<<<<<<< HEAD
=======
    // Recherche les annonces de l'utilisateur connecté
    public List<Annonce> searchMyAnnonces(Long userId, String keyword, Long categoryId, AnnonceStatus status, int page, int size) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return annonceRepo.searchByAuthor(em, userId, keyword, categoryId, status, page, size);
        } finally {
            em.close();
        }
    }

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
    public List<Annonce> search(String keyword, Long categoryId, AnnonceStatus status, int page, int size) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return annonceRepo.search(em, keyword, categoryId, status, page, size);
        } finally {
            em.close();
        }
    }

<<<<<<< HEAD
    public boolean publish(Long id) {
        return setStatus(id, AnnonceStatus.PUBLISHED);
    }

    public boolean archive(Long id) {
        return setStatus(id, AnnonceStatus.ARCHIVED);
    }

    private boolean setStatus(Long id, AnnonceStatus status) {
=======
    public boolean publish(Long id, Long userId) {
        return setStatus(id, userId, AnnonceStatus.PUBLISHED);
    }

    public boolean archive(Long id, Long userId) {
        return setStatus(id, userId, AnnonceStatus.ARCHIVED);
    }

    private boolean setStatus(Long id, Long userId, AnnonceStatus status) {
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Annonce a = annonceRepo.findById(em, id);
            if (a == null) {
                em.getTransaction().rollback();
                return false;
            }
<<<<<<< HEAD
=======

            // Vérifier que l'utilisateur est le propriétaire
            if (!a.getAuthor().getId().equals(userId)) {
                em.getTransaction().rollback();
                return false;
            }

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
            a.setStatus(status);
            annonceRepo.update(em, a);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    private void validateAnnonce(Annonce a) {

        java.util.Map<String,String> errors = new java.util.HashMap<>();

        if (a.getTitle() == null || a.getTitle().isBlank())
            errors.put("title", "Titre obligatoire");

        if (a.getDescription() == null || a.getDescription().isBlank())
            errors.put("description", "Description obligatoire");

        if (a.getAdress() == null || a.getAdress().isBlank())
            errors.put("adress", "Adresse obligatoire");

        if (a.getMail() == null || a.getMail().isBlank())
            errors.put("mail", "Email obligatoire");

        if (a.getMail() != null && !a.getMail().matches("^[^@]+@[^@]+\\.[^@]+$"))
            errors.put("mail", "Email invalide");

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}