package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import fr.uit.univparis8.tpair.tpair1.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/AnnonceUpdate")
public class AnnonceUpdateServlet extends HttpServlet {

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        Annonce a = service.findById(id);
        if (a == null) {
            resp.sendRedirect("AnnonceList");
            return;
        }
        req.setAttribute("form", a);
        req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Long id = Long.parseLong(req.getParameter("id"));

        Annonce a = new Annonce();
        a.setId(id);
        a.setTitle(req.getParameter("title"));
        a.setDescription(req.getParameter("description"));
        a.setAdress(req.getParameter("adress"));
        a.setMail(req.getParameter("mail"));

        try {
            boolean ok = service.update(a);
            resp.sendRedirect("AnnonceList");
        } catch (ValidationException ve) {
            req.setAttribute("errors", ve.getErrors());
            req.setAttribute("form", a);
            req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
        }
    }
}