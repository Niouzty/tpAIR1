package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.dao.AnnonceDAO;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AnnonceAdd")
public class AnnonceAddServlet extends HttpServlet {

    private final AnnonceDAO dao = new AnnonceDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/AnnonceAdd.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String adress = req.getParameter("adress");
        String mail = req.getParameter("mail");

        if (title.isEmpty() || description.isEmpty() || adress.isEmpty() || mail.isEmpty()) {
            req.setAttribute("error", "Tous les champs sont obligatoires");
            req.getRequestDispatcher("/AnnonceAdd.jsp").forward(req, resp);
            return;
        }

        Annonce a = new Annonce();
        a.setTitle(title);
        a.setDescription(description);
        a.setAdress(adress);
        a.setMail(mail);

        dao.create(a);
        resp.sendRedirect("AnnonceList");
    }
}
