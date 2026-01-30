package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.dao.AnnonceDAO;
import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AnnonceUpdate")
public class AnnonceUpdateServlet extends HttpServlet{

    private final AnnonceDAO dao = new AnnonceDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        req.setAttribute("annonce", dao.find(id));
        req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Annonce a = new Annonce();
        a.setId(Integer.parseInt(req.getParameter("id")));
        a.setTitle(req.getParameter("title"));
        a.setDescription(req.getParameter("description"));
        a.setAdress(req.getParameter("adress"));
        a.setMail(req.getParameter("mail"));

        dao.update(a);
        resp.sendRedirect("AnnonceList");
    }
}
