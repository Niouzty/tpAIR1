package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet pour créer des annonces de test avec le statut PUBLISHED
 * DÉSACTIVÉ : TP3 utilise une API REST pure
 * Accès : http://localhost:8080/tpAIR1/CreateTestAnnonces
 */
// @WebServlet("/CreateTestAnnonces")
public class CreateTestAnnoncesServlet extends HttpServlet {

    private final AnnonceService annonceService = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");

        try {
            // Créer une annonce de test
            Annonce a1 = new Annonce();
            a1.setTitle("Appartement 3 pièces à Paris");
            a1.setDescription("Bel appartement rénové en plein cœur de Paris, proche métro, lumineux et spacieux");
            a1.setAdress("75004 Paris, France");
            a1.setMail("contact@example.com");
            a1.setStatus(AnnonceStatus.PUBLISHED);

            Annonce created1 = annonceService.create(a1, 1L);

            // Créer une deuxième annonce
            Annonce a2 = new Annonce();
            a2.setTitle("Studio meublé centre-ville");
            a2.setDescription("Studio moderne, entièrement meublé, idéal pour étudiant ou jeune professionnel");
            a2.setAdress("75005 Paris, France");
            a2.setMail("studio@example.com");
            a2.setStatus(AnnonceStatus.PUBLISHED);

            Annonce created2 = annonceService.create(a2, 1L);

            // Créer une troisième annonce
            Annonce a3 = new Annonce();
            a3.setTitle("Maison avec jardin en banlieue");
            a3.setDescription("Maison familiale avec jardin, garage, parfait pour famille avec enfants");
            a3.setAdress("92100 Boulogne-Billancourt, France");
            a3.setMail("maison@example.com");
            a3.setStatus(AnnonceStatus.PUBLISHED);

            Annonce created3 = annonceService.create(a3, 1L);

            resp.getWriter().println("<html><body style='font-family: Arial; margin: 40px;'>");
            resp.getWriter().println("<h1>✅ Annonces de test créées avec succès !</h1>");
            resp.getWriter().println("<p>3 annonces publiées ont été créées :</p>");
            resp.getWriter().println("<ul>");
            resp.getWriter().println("<li>" + created1.getTitle() + "</li>");
            resp.getWriter().println("<li>" + created2.getTitle() + "</li>");
            resp.getWriter().println("<li>" + created3.getTitle() + "</li>");
            resp.getWriter().println("</ul>");
            resp.getWriter().println("<p><a href='AnnoncesPubliees.jsp'>👉 Voir les annonces publiées</a></p>");
            resp.getWriter().println("<p><a href='index.jsp'>🏠 Retour à l'accueil</a></p>");
            resp.getWriter().println("</body></html>");

        } catch (Exception e) {
            resp.getWriter().println("<html><body style='font-family: Arial; margin: 40px;'>");
            resp.getWriter().println("<h1>❌ Erreur lors de la création</h1>");
            resp.getWriter().println("<p>Message : " + e.getMessage() + "</p>");
            resp.getWriter().println("<pre>" + e.toString() + "</pre>");
            resp.getWriter().println("<p><a href='index.jsp'>🏠 Retour à l'accueil</a></p>");
            resp.getWriter().println("</body></html>");
            e.printStackTrace();
        }
    }
}
