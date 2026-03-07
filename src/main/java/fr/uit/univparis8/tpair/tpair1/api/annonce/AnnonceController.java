package fr.uit.univparis8.tpair.tpair1.api.annonce;

import fr.uit.univparis8.tpair.tpair1.api.annonce.dto.AnnonceRequest;
import fr.uit.univparis8.tpair.tpair1.api.annonce.dto.AnnonceResponse;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.enums.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.api.mapper.AnnonceMapper;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import fr.uit.univparis8.tpair.tpair1.config.JwtUserPrincipal;
import fr.uit.univparis8.tpair.tpair1.service.exceptions.UnauthorizedAccessException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    private final AnnonceService annonceService;
    private final AnnonceMapper mapper;

    public AnnonceController(AnnonceService annonceService, AnnonceMapper mapper) {
        this.annonceService = annonceService;
        this.mapper = mapper;
    }

    @GetMapping
    public Page<AnnonceResponse> list(@RequestParam(name = "q", required = false) String q,
                                      @RequestParam(name = "cat", required = false) Long cat,
                                      @RequestParam(name = "status", required = false) AnnonceStatus status,
                                      @RequestParam(name = "sort", defaultValue = "date") String sort,
                                      @RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      @AuthenticationPrincipal JwtUserPrincipal principal) {
        Long authorId = principal != null ? principal.getUserId() : null;
        Page<Annonce> p = annonceService.search(q, cat, status, authorId, page, size, sort);
        return p.map(mapper::toResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceResponse> getOne(@PathVariable("id") Long id) {
        Annonce a = annonceService.findById(id);
        if (a == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapper.toResponse(a));
    }

    @PostMapping
    public ResponseEntity<AnnonceResponse> create(@Validated @RequestBody AnnonceRequest req,
                                                  @AuthenticationPrincipal JwtUserPrincipal principal) {
        Long userId = requireUserId(principal);
        Annonce created = annonceService.create(mapper.toEntity(req), userId, req.getCategoryId());
        return ResponseEntity.created(URI.create("/api/annonces/" + created.getId()))
                .body(mapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceResponse> update(@PathVariable("id") Long id,
                                                  @Validated @RequestBody AnnonceRequest req,
                                                  @AuthenticationPrincipal JwtUserPrincipal principal) {
        Long userId = requireUserId(principal);
        Annonce a = mapper.toEntity(req);
        a.setId(id);
        boolean ok = annonceService.update(a, userId, req.getCategoryId());
        if (!ok) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(mapper.toResponse(annonceService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id,
                                       @AuthenticationPrincipal JwtUserPrincipal principal) {
        Long userId = requireUserId(principal);
        boolean ok = annonceService.delete(id, userId);
        return ok ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/publish")
    public ResponseEntity<Void> publish(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal JwtUserPrincipal principal) {
        Long userId = requireUserId(principal);
        boolean ok = annonceService.publish(id, userId, principal.getRole());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable("id") Long id,
                                        @AuthenticationPrincipal JwtUserPrincipal principal) {
        Long userId = requireUserId(principal);
        boolean ok = annonceService.archive(id, userId, principal.getRole());
        return ok ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    private Long requireUserId(JwtUserPrincipal principal) {
        if (principal == null) throw new UnauthorizedAccessException("Auth requise");
        return principal.getUserId();
    }

}
