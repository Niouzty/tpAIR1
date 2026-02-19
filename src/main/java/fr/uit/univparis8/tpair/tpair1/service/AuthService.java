package fr.uit.univparis8.tpair.tpair1.service;

import fr.uit.univparis8.tpair.tpair1.dao.UserRepository;
import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.jpa.JPAUtil;
import jakarta.persistence.EntityManager;

public class AuthService {
    private final UserRepository userRepo = new UserRepository();

    public User authenticate(String username, String password) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            if ("admin".equals(username)) {
                ensureDefaultAdmin(em);
            }
            User u = userRepo.findByUsername(em, username);
            if (u == null) return null;

            if (!u.getPassword().equals(password)) return null;

            return u;
        } finally {
            em.close();
        }
    }

    private void ensureDefaultAdmin(EntityManager em) {
        User existing = userRepo.findByUsername(em, "admin");
        if (existing != null) {
            return;
        }

        em.getTransaction().begin();
        try {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@local.test");
            admin.setPassword("admin");
            userRepo.create(em, admin);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        }
    }

    public User register(String username, String email, String password) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("Username obligatoire");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email obligatoire");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("Password obligatoire");

        EntityManager em = JPAUtil.getEntityManager();
        try {
            User exists = userRepo.findByUsername(em, username);
            if (exists != null) throw new IllegalArgumentException("Username déjà utilisé");

            em.getTransaction().begin();

            User u = new User();
            u.setUsername(username);
            u.setEmail(email);
            u.setPassword(password);

            userRepo.create(em, u);

            em.getTransaction().commit();
            return u;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
