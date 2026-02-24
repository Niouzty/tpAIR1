package fr.uit.univparis8.tpair.tpair1.resource;

import fr.uit.univparis8.tpair.tpair1.dto.AnnonceDTO;
import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import fr.uit.univparis8.tpair.tpair1.dto.PagedResponse;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Exercice 2 : API REST Annonce
 * Endpoints :
 * - GET /api/annonces (liste paginée)
 * - GET /api/annonces/{id} (détail)
 * - POST /api/annonces (création)
 * - PUT /api/annonces/{id} (mise à jour)
 * - DELETE /api/annonces/{id} (suppression)
 */
@Path("/annonces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnnonceResource {

    private final AnnonceService annonceService = new AnnonceService();

    /**
     * GET /api/annonces
     * Récupère la liste paginée des annonces
     * @param page numéro de page (défaut: 0)
     * @param size taille de la page (défaut: 10)
     */
    @GET
    public Response listerAnnonces(@DefaultValue("0") @QueryParam("page") int page,
                                   @DefaultValue("10") @QueryParam("size") int size) {
        try {
            List<Annonce> annonces = annonceService.listAll();
            List<AnnonceDTO> dtos = annonces.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            
            // Pagination simple
            int totalElements = dtos.size();
            int totalPages = (totalElements + size - 1) / size;
            int startIndex = Math.min(page * size, dtos.size());
            int endIndex = Math.min(startIndex + size, dtos.size());
            
            List<AnnonceDTO> paginated = dtos.subList(startIndex, endIndex);
            PagedResponse<AnnonceDTO> response = new PagedResponse<>(
                    paginated, page, size, totalElements, totalPages
            );
            
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de la récupération des annonces"))
                    .build();
        }
    }

    /**
     * GET /api/annonces/{id}
     * Récupère une annonce spécifique par son ID
     */
    @GET
    @Path("/{id}")
    public Response obtenirAnnonce(@PathParam("id") Long id) {
        try {
            Annonce annonce = annonceService.findById(id);
            if (annonce == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(404, "Annonce non trouvée"))
                        .build();
            }
            return Response.ok(toDTO(annonce)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de la récupération de l'annonce"))
                    .build();
        }
    }

    /**
     * POST /api/annonces
     * Crée une nouvelle annonce
     */
    @POST
    public Response creerAnnonce(@Valid AnnonceDTO dto) {
        try {
            // Validation basique
            if (dto.titre == null || dto.titre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse(400, "Le titre est obligatoire"))
                        .build();
            }
            if (dto.description == null || dto.description.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorResponse(400, "La description est obligatoire"))
                        .build();
            }

            // Convertir DTO en entité
            Annonce annonce = new Annonce();
            annonce.setTitle(dto.titre);
            annonce.setDescription(dto.description);
            annonce.setAdress(dto.categorie != null ? dto.categorie : "");
            annonce.setMail("default@mail.com"); // TODO: Récupérer depuis l'authentification

            // Persister (utiliser l'ID utilisateur depuis le token)
            Long userId = dto.auteurId != null ? dto.auteurId : 1L; // TODO: Récupérer depuis le contexte
            Annonce created = annonceService.create(annonce, userId);

            return Response.status(Response.Status.CREATED)
                    .entity(toDTO(created))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de la création de l'annonce: " + e.getMessage()))
                    .build();
        }
    }

    /**
     * PUT /api/annonces/{id}
     * Mets à jour une annonce existante
     */
    @PUT
    @Path("/{id}")
    public Response mettreAJourAnnonce(@PathParam("id") Long id, @Valid AnnonceDTO dto) {
        try {
            Annonce existante = annonceService.findById(id);
            if (existante == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(404, "Annonce non trouvée"))
                        .build();
            }

            // Mettre à jour les champs
            if (dto.titre != null && !dto.titre.trim().isEmpty()) {
                existante.setTitle(dto.titre);
            }
            if (dto.description != null && !dto.description.trim().isEmpty()) {
                existante.setDescription(dto.description);
            }
            if (dto.categorie != null && !dto.categorie.trim().isEmpty()) {
                existante.setAdress(dto.categorie);
            }

            // Persister (utiliser l'ID utilisateur depuis le token)
            Long userId = dto.auteurId != null ? dto.auteurId : 1L; // TODO: Récupérer depuis le contexte
            boolean updated = annonceService.update(existante, userId);

            if (!updated) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new ErrorResponse(403, "Vous n'êtes pas autorisé à modifier cette annonce"))
                        .build();
            }

            return Response.ok(toDTO(existante)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de la mise à jour de l'annonce"))
                    .build();
        }
    }

    /**
     * DELETE /api/annonces/{id}
     * Supprime une annonce
     */
    @DELETE
    @Path("/{id}")
    public Response supprimerAnnonce(@PathParam("id") Long id) {
        try {
            Annonce existante = annonceService.findById(id);
            if (existante == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(new ErrorResponse(404, "Annonce non trouvée"))
                        .build();
            }

            // Supprimer (utiliser l'ID utilisateur depuis le token)
            Long userId = existante.getAuthor().getId(); // On utilise l'auteur comme référence
            boolean deleted = annonceService.delete(id, userId);

            if (!deleted) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new ErrorResponse(403, "Vous n'êtes pas autorisé à supprimer cette annonce"))
                        .build();
            }

            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse(500, "Erreur lors de la suppression de l'annonce"))
                    .build();
        }
    }

    /**
     * Convertit une entité Annonce en DTO
     */
    private AnnonceDTO toDTO(Annonce annonce) {
        return AnnonceDTO.builder()
                .id(annonce.getId())
                .titre(annonce.getTitle())
                .description(annonce.getDescription())
                .categorie(annonce.getAdress())
                .statut(annonce.getStatus() != null ? annonce.getStatus().name() : "DRAFT")
                .dateCreation(annonce.getDate())
                .auteurId(annonce.getAuthor() != null ? annonce.getAuthor().getId() : null)
                .build();
    }
}
