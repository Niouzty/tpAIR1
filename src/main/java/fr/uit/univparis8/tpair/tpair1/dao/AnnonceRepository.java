package fr.uit.univparis8.tpair.tpair1.dao;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AnnonceRepository {

    public Annonce findById(EntityManager em, Long id) {
        return em.find(Annonce.class, id);
    }

    public void create(EntityManager em, Annonce a) {
        em.persist(a);
    }

    public Annonce update(EntityManager em, Annonce a) {
        return em.merge(a);
    }

    public boolean delete(EntityManager em, Long id) {
        Annonce a = em.find(Annonce.class, id);
        if (a == null) return false;
        em.remove(a);
        return true;
    }

    public List<Annonce> search(EntityManager em,
                                String keyword,
                                Long categoryId,
                                AnnonceStatus status,
                                int page,
                                int size) {

        StringBuilder jpql = new StringBuilder(
                "SELECT a FROM Annonce a WHERE 1=1 "
        );

        if (keyword != null && !keyword.isBlank()) {
            jpql.append("AND (LOWER(a.title) LIKE :kw OR LOWER(a.description) LIKE :kw) ");
        }
        if (categoryId != null) {
            jpql.append("AND a.category.id = :catId ");
        }
        if (status != null) {
            jpql.append("AND a.status = :status ");
        }

        jpql.append("ORDER BY a.date DESC");

        TypedQuery<Annonce> q = em.createQuery(jpql.toString(), Annonce.class);

        if (keyword != null && !keyword.isBlank()) {
            q.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        }
        if (categoryId != null) {
            q.setParameter("catId", categoryId);
        }
        if (status != null) {
            q.setParameter("status", status);
        }

        int safePage = Math.max(1, page);
        int safeSize = Math.max(1, size);

        q.setFirstResult((safePage - 1) * safeSize);
        q.setMaxResults(safeSize);

        return q.getResultList();
    }
}