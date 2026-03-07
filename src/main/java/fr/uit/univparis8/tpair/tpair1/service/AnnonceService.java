package fr.uit.univparis8.tpair.tpair1.service;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.enums.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.model.Category;
import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.repository.AnnonceRepository;
import fr.uit.univparis8.tpair.tpair1.repository.AnnonceSpecifications;
import fr.uit.univparis8.tpair.tpair1.repository.CategoryRepository;
import fr.uit.univparis8.tpair.tpair1.repository.UserRepository;
import fr.uit.univparis8.tpair.tpair1.service.exceptions.ValidationException;
import fr.uit.univparis8.tpair.tpair1.service.exceptions.UnauthorizedAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional
public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public AnnonceService(AnnonceRepository annonceRepository,
                          UserRepository userRepository,
                          CategoryRepository categoryRepository) {
        this.annonceRepository = annonceRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Annonce create(Annonce a, Long authorId, Long categoryId) {
        validateAnnonce(a);
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        Category category = resolveCategory(categoryId);

        a.setAuthor(author);
        a.setCategory(category);
        if (a.getStatus() == null) a.setStatus(AnnonceStatus.DRAFT);

        return annonceRepository.save(a);
    }

    @Transactional(readOnly = true)
    public Annonce findById(Long id) {
        return annonceRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Page<Annonce> search(String keyword, Long categoryId, AnnonceStatus status, Long authorId, int page, int size, String sort) {
        int safePage = Math.max(0, page - 1);
        int safeSize = Math.max(1, size);
        String sortField = validateSort(sort);

        var spec = where((Specification<Annonce>) null);
        spec = spec == null ? AnnonceSpecifications.titleOrDescriptionContains(keyword) : spec.and(AnnonceSpecifications.titleOrDescriptionContains(keyword));
        spec = spec == null ? AnnonceSpecifications.categoryIs(categoryId) : spec.and(AnnonceSpecifications.categoryIs(categoryId));
        spec = spec == null ? AnnonceSpecifications.statusIs(status) : spec.and(AnnonceSpecifications.statusIs(status));
        spec = spec == null ? AnnonceSpecifications.authorIs(authorId) : spec.and(AnnonceSpecifications.authorIs(authorId));

        return annonceRepository.findAll(spec, PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, sortField)));
    }

    public boolean delete(Long id, Long userId) {
        Annonce a = annonceRepository.findById(id).orElse(null);
        if (a == null) return false;
        checkOwnership(a, userId);
        requireArchivedBeforeDelete(a);
        annonceRepository.delete(a);
        return true;
    }

    public boolean update(Annonce incoming, Long userId, Long categoryId) {
        Annonce existing = annonceRepository.findById(incoming.getId()).orElse(null);
        if (existing == null) return false;
        checkOwnership(existing, userId);
        forbidUpdateIfPublished(existing);

        validateAnnonce(incoming);

        existing.setTitle(incoming.getTitle());
        existing.setDescription(incoming.getDescription());
        existing.setAdress(incoming.getAdress());
        existing.setMail(incoming.getMail());
        if (categoryId != null) {
            existing.setCategory(resolveCategory(categoryId));
        }

        annonceRepository.save(existing);
        return true;
    }

    public boolean publish(Long id, Long userId, String role) {
        return setStatus(id, AnnonceStatus.PUBLISHED, userId, role);
    }

    public boolean archive(Long id, Long userId, String role) {
        return setStatus(id, AnnonceStatus.ARCHIVED, userId, role);
    }

    private boolean setStatus(Long id, AnnonceStatus status, Long userId, String role) {
        Annonce a = annonceRepository.findById(id).orElse(null);
        if (a == null) return false;
        boolean isAdmin = role != null && (role.equals("ROLE_ADMIN") || role.equalsIgnoreCase("ADMIN"));
        if (!isAdmin) {
            checkOwnership(a, userId);
        }
        if (status == AnnonceStatus.ARCHIVED && !isAdmin) {
            throw new UnauthorizedAccessException("Seul un admin peut archiver");
        }
        a.setStatus(status);
        annonceRepository.save(a);
        return true;
    }

    private Category ensureDefaultCategory() {
        return categoryRepository.findByLabel("General")
                .orElseGet(() -> {
                    Category c = new Category();
                    c.setLabel("General");
                    return categoryRepository.save(c);
                });
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) {
            return ensureDefaultCategory();
        }
        return categoryRepository.findById(categoryId)
                .orElseGet(this::ensureDefaultCategory);
    }

    private String validateSort(String sort) {
        if (sort == null || sort.isBlank()) return "date";
        boolean exists = java.util.Arrays.stream(Annonce.class.getDeclaredFields())
                .map(java.lang.reflect.Field::getName)
                .anyMatch(f -> f.equals(sort) && !f.equals("author") && !f.equals("category"));
        if (!exists) {
            throw new ValidationException(java.util.Map.of("sort", "Champ de tri non autorisé"));
        }
        return sort;
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

        if (!errors.isEmpty()) throw new ValidationException(errors);
    }

    private void checkOwnership(Annonce a, Long userId) {
        if (userId == null || a.getAuthor() == null || !a.getAuthor().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Action interdite sur une annonce qui ne vous appartient pas");
        }
    }

    private void forbidUpdateIfPublished(Annonce a) {
        if (AnnonceStatus.PUBLISHED.equals(a.getStatus())) {
            throw new ValidationException(java.util.Map.of("status", "Une annonce publiée ne peut plus être modifiée"));
        }
    }

    private void requireArchivedBeforeDelete(Annonce a) {
        if (!AnnonceStatus.ARCHIVED.equals(a.getStatus())) {
            throw new ValidationException(java.util.Map.of("status", "Archiver avant suppression"));
        }
    }
}
