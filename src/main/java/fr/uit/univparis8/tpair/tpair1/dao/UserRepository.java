package fr.uit.univparis8.tpair.tpair1.dao;

import fr.uit.univparis8.tpair.tpair1.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserRepository {

    public User findById(EntityManager em, Long id) {
        return em.find(User.class, id);
    }

    public List<User> findAll(EntityManager em) {
        return em.createQuery("SELECT u FROM User u ORDER BY u.createdAt DESC", User.class)
                .getResultList();
    }

    public User findByUsername(EntityManager em, String username) {
        TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.username = :username",
                User.class
        );
        q.setParameter("username", username);
        List<User> r = q.getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    public User findByEmail(EntityManager em, String email) {
        TypedQuery<User> q = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email",
                User.class
        );
        q.setParameter("email", email);
        List<User> r = q.getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    public User create(EntityManager em, User u) {
        em.persist(u);
        return u;
    }

    public User update(EntityManager em, User u) {
        return em.merge(u);
    }

    public boolean delete(EntityManager em, Long id) {
        User u = em.find(User.class, id);
        if (u == null) return false;
        em.remove(u);
        return true;
    }



}
