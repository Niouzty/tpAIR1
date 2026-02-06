package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import fr.uit.univparis8.tpair.tpair1.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/AnnonceAdd")
public class AnnonceAddServlet extends HttpServlet {

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/AnnonceAdd.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Annonce a = new Annonce();
        a.setTitle(req.getParameter("title"));
        a.setDescription(req.getParameter("description"));
        a.setAdress(req.getParameter("adress"));
        a.setMail(req.getParameter("mail"));

        try {
            // la validation est dans le service
            service.create(a);
            resp.sendRedirect("AnnonceList");
        } catch (ValidationException ve) {
            // erreurs + conservation valeurs
            req.setAttribute("errors", ve.getErrors());
            req.setAttribute("form", a);
            req.getRequestDispatcher("/AnnonceAdd.jsp").forward(req, resp);
        }
    }
}