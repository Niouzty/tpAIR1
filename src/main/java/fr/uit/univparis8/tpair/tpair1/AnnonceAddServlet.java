package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import fr.uit.univparis8.tpair.tpair1.service.ValidationException;
import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

// DÉSACTIVÉ : TP3 utilise une API REST pure
// @WebServlet("/AnnonceAdd")
public class AnnonceAddServlet extends HttpServlet {

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/AnnonceAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Annonce a = new Annonce();
        a.setTitle(req.getParameter("title"));
        a.setDescription(req.getParameter("description"));
        a.setAdress(req.getParameter("adress"));
        a.setMail(req.getParameter("mail"));

        try {
            // la validation est dans le service
            // Récupérer l'ID de l'utilisateur connecté
            HttpSession session = req.getSession();
            Long userId = (Long) session.getAttribute("userId");

            // Passer userId au service
            service.create(a, userId);
            resp.sendRedirect("AnnonceList");
        } catch (ValidationException ve) {
            // erreurs + conservation valeurs
            req.setAttribute("errors", ve.getErrors());
            req.setAttribute("form", a);
            req.getRequestDispatcher("/AnnonceAdd.jsp").forward(req, resp);
        }
    }
}
