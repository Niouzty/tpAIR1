package fr.uit.univparis8.tpair.tpair1.service;

import fr.uit.univparis8.tpair.tpair1.enums.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.model.Category;
import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.repository.AnnonceRepository;
import fr.uit.univparis8.tpair.tpair1.repository.CategoryRepository;
import fr.uit.univparis8.tpair.tpair1.repository.UserRepository;
import fr.uit.univparis8.tpair.tpair1.service.exceptions.UnauthorizedAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnonceServiceTests {

    @Mock AnnonceRepository annonceRepository;
    @Mock UserRepository userRepository;
    @Mock CategoryRepository categoryRepository;

    @InjectMocks AnnonceService service;

    User owner;
    Category cat;

    @BeforeEach
    void setup() {
        owner = new User();
        owner.setId(1L);
        cat = new Category();
        cat.setId(10L);
    }

    @Test
    void search_validatesSortField() {
        when(annonceRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        service.search(null, null, null, null, 1, 10, "date");

        assertThatThrownBy(() -> service.search(null, null, null, null, 1, 10, "hacker"))
                .isInstanceOf(fr.uit.univparis8.tpair.tpair1.service.exceptions.ValidationException.class);
    }

    @Test
    void archive_requiresAdminButNotOwnership() {
        Annonce a = new Annonce();
        a.setId(5L);
        a.setAuthor(owner);
        a.setCategory(cat);
        when(annonceRepository.findById(5L)).thenReturn(Optional.of(a));

        assertThatThrownBy(() -> service.archive(5L, 1L, "ROLE_USER"))
                .isInstanceOf(UnauthorizedAccessException.class);

        service.archive(5L, 99L, "ROLE_ADMIN");
        verify(annonceRepository, times(1)).save(a);
        assertThat(a.getStatus()).isEqualTo(AnnonceStatus.ARCHIVED);
    }
}
