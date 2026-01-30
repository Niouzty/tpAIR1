package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.dao.AnnonceDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AnnonceDelete")
public class AnnonceDeleteServlet extends HttpServlet{

    private final AnnonceDAO dao = new AnnonceDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        dao.delete(id);
        resp.sendRedirect("AnnonceList");
    }
}
