package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AnnoncePublish")
public class AnnoncePublishServlet extends HttpServlet {

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Long id = Long.parseLong(req.getParameter("id"));
<<<<<<< HEAD
            service.publish(id);
=======

            // Récupérer l'ID de l'utilisateur connecté
            HttpSession session = req.getSession();
            Long userId = (Long) session.getAttribute("userId");

            // Passer userId au service pour vérifier l'ownership
            service.publish(id, userId);
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        } catch (Exception ignored) {}

        resp.sendRedirect("AnnonceList");
    }
}