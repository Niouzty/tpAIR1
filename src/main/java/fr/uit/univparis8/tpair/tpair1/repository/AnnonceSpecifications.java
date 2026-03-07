package fr.uit.univparis8.tpair.tpair1.repository;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.enums.AnnonceStatus;
import org.springframework.data.jpa.domain.Specification;

public final class AnnonceSpecifications {

    private AnnonceSpecifications() {}

    public static Specification<Annonce> titleOrDescriptionContains(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        String kw = "%" + keyword.toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), kw),
                cb.like(cb.lower(root.get("description")), kw)
        );
    }

    public static Specification<Annonce> categoryIs(Long categoryId) {
        if (categoryId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Annonce> statusIs(AnnonceStatus status) {
        if (status == null) return null;
        return (root, query, cb) -> cb.equal(root.get("status"), status);
    }

    public static Specification<Annonce> authorIs(Long authorId) {
        if (authorId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("author").get("id"), authorId);
    }
}
