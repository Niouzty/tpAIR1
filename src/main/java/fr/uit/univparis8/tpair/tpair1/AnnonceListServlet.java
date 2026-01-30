package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.dao.AnnonceDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AnnonceList")
public class AnnonceListServlet extends HttpServlet{

    private final AnnonceDAO dao = new AnnonceDAO();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("annonces", dao.findAll());
        req.getRequestDispatcher("/AnnonceList.jsp").forward(req, resp);
    }
}
