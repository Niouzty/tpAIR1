package fr.uit.univparis8.tpair.tpair1.dao;

import fr.uit.univparis8.tpair.tpair1.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CategoryRepository {

    public Category findById(EntityManager em, Long id) {
        return em.find(Category.class, id);
    }

    public List<Category> findAll(EntityManager em) {
        return em.createQuery("SELECT c FROM Category c ORDER BY c.label ASC", Category.class)
                .getResultList();
    }

    public Category findByLabel(EntityManager em, String label) {
        TypedQuery<Category> q = em.createQuery(
                "SELECT c FROM Category c WHERE c.label = :label",
                Category.class
        );
        q.setParameter("label", label);
        List<Category> r = q.getResultList();
        return r.isEmpty() ? null : r.get(0);
    }

    public void create(EntityManager em, Category c) {
        em.persist(c);
    }

    public Category update(EntityManager em, Category c) {
        return em.merge(c);
    }

    public boolean delete(EntityManager em, Long id) {
        Category c = em.find(Category.class, id);
        if (c == null) return false;
        em.remove(c);
        return true;
    }
}
