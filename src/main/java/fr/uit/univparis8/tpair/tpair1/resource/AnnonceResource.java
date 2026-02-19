package fr.uit.univparis8.tpair.tpair1.resource;

import fr.uit.univparis8.tpair.tpair1.dto.AnnonceDTO;
import fr.uit.univparis8.tpair.tpair1.dto.ErrorResponse;
import fr.uit.univparis8.tpair.tpair1.dto.PagedResponse;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;


@Path("/annonces")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnnonceResource {

    private final AnnonceService annonceService = new AnnonceService();

    
    @GET
    public Response listerAnnonces(@DefaultValue("0") @QueryParam("page") int page,
                                   @DefaultValue("10") @QueryParam("size") int size) {
        int safeSize = Math.max(1, size);
        int safePage = Math.max(0, page);
        List<AnnonceDTO> paginated = annonceService.listPaged(safePage, safeSize).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        long totalElements = annonceService.countAll();
        int totalPages = (int) ((totalElements + safeSize - 1) / safeSize);

        PagedResponse<AnnonceDTO> response = new PagedResponse<>(
                paginated, safePage, safeSize, totalElements, totalPages
        );

        return Response.ok(response).build();
    }

    
    @GET
    @Path("/{id}")
    public Response obtenirAnnonce(@PathParam("id") Long id) {
        Annonce annonce = annonceService.findById(id);
        if (annonce == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, "Annonce non trouvée"))
                    .build();
        }
        return Response.ok(toDTO(annonce)).build();
    }

    
    @POST
    public Response creerAnnonce(@Valid AnnonceDTO dto, @Context ContainerRequestContext requestContext) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(400, "Payload JSON obligatoire"))
                    .build();
        }

        Annonce annonce = new Annonce();
        annonce.setTitle(dto.titre);
        annonce.setDescription(dto.description);
        annonce.setAdress(dto.categorie != null ? dto.categorie : "General");
        annonce.setMail(resolveAuthenticatedMail(requestContext));

        Long userId = requireAuthenticatedUserId(requestContext);
        Annonce created = annonceService.create(annonce, userId);

        return Response.status(Response.Status.CREATED)
                .entity(toDTO(created))
                .build();
    }

    
    @PUT
    @Path("/{id}")
    public Response mettreAJourAnnonce(@PathParam("id") Long id, @Valid AnnonceDTO dto,
                                       @Context ContainerRequestContext requestContext) {
        if (dto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse(400, "Payload JSON obligatoire"))
                    .build();
        }

        Annonce existante = annonceService.findById(id);
        if (existante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, "Annonce non trouvée"))
                    .build();
        }
        if (dto.version != null && existante.getVersion() != null && !dto.version.equals(existante.getVersion())) {
            throw new fr.uit.univparis8.tpair.tpair1.service.ValidationException("Conflit de version: rechargez la ressource avant modification");
        }

        if (dto.titre != null && !dto.titre.trim().isEmpty()) {
            existante.setTitle(dto.titre);
        }
        if (dto.description != null && !dto.description.trim().isEmpty()) {
            existante.setDescription(dto.description);
        }
        if (dto.categorie != null && !dto.categorie.trim().isEmpty()) {
            existante.setAdress(dto.categorie);
        }
        existante.setMail(resolveAuthenticatedMail(requestContext));

        Long userId = requireAuthenticatedUserId(requestContext);
        boolean updated = annonceService.update(existante, userId);

        if (!updated) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse(403, "Vous n'êtes pas autorisé à modifier cette annonce"))
                    .build();
        }

        Annonce reloaded = annonceService.findById(id);
        return Response.ok(toDTO(reloaded != null ? reloaded : existante)).build();
    }

    
    @DELETE
    @Path("/{id}")
    public Response supprimerAnnonce(@PathParam("id") Long id, @Context ContainerRequestContext requestContext) {
        Annonce existante = annonceService.findById(id);
        if (existante == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, "Annonce non trouvée"))
                    .build();
        }

        Long userId = requireAuthenticatedUserId(requestContext);
        boolean deleted = annonceService.delete(id, userId);

        if (!deleted) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse(403, "Vous n'êtes pas autorisé à supprimer cette annonce"))
                    .build();
        }

        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/publish")
    public Response publierAnnonce(@PathParam("id") Long id, @Context ContainerRequestContext requestContext) {
        if (annonceService.findById(id) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, "Annonce non trouvée"))
                    .build();
        }

        boolean updated = annonceService.publish(id, requireAuthenticatedUserId(requestContext));
        if (!updated) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse(403, "Vous n'êtes pas autorisé à publier cette annonce"))
                    .build();
        }
        return Response.ok(toDTO(annonceService.findById(id))).build();
    }

    @POST
    @Path("/{id}/archive")
    public Response archiverAnnonce(@PathParam("id") Long id, @Context ContainerRequestContext requestContext) {
        if (annonceService.findById(id) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(404, "Annonce non trouvée"))
                    .build();
        }

        boolean updated = annonceService.archive(id, requireAuthenticatedUserId(requestContext));
        if (!updated) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(new ErrorResponse(403, "Vous n'êtes pas autorisé à archiver cette annonce"))
                    .build();
        }
        return Response.ok(toDTO(annonceService.findById(id))).build();
    }

    
    private AnnonceDTO toDTO(Annonce annonce) {
        return AnnonceDTO.builder()
                .id(annonce.getId())
                .titre(annonce.getTitle())
                .description(annonce.getDescription())
                .categorie(annonce.getAdress())
                .statut(annonce.getStatus() != null ? annonce.getStatus().name() : "DRAFT")
                .dateCreation(annonce.getDate())
                .auteurId(annonce.getAuthor() != null ? annonce.getAuthor().getId() : null)
                .version(annonce.getVersion())
                .build();
    }

    private Long requireAuthenticatedUserId(ContainerRequestContext requestContext) {
        Object userId = requestContext.getProperty("userId");
        if (userId instanceof Long) {
            return (Long) userId;
        }
        if (userId instanceof Number) {
            return ((Number) userId).longValue();
        }
        throw new NotAuthorizedException("Bearer");
    }

    private String resolveAuthenticatedMail(ContainerRequestContext requestContext) {
        Object username = requestContext.getProperty("username");
        if (username instanceof String && !((String) username).isBlank()) {
            return username + "@local.test";
        }
        return "unknown@local.test";
    }
}
