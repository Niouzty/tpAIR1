package fr.uit.univparis8.tpair.tpair1;

<<<<<<< HEAD
import fr.uit.univparis8.tpair.tpair1.dao.AnnonceRepository;
=======
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/AnnonceList")
public class AnnonceListServlet extends HttpServlet{

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

<<<<<<< HEAD
=======
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        String q = req.getParameter("q");

        Long catId = null;
        String cat = req.getParameter("cat");
        if (cat != null && !cat.isBlank()) catId = Long.parseLong(cat);

        AnnonceStatus status = null;
        String st = req.getParameter("status");
        if (st != null && !st.isBlank()) status = AnnonceStatus.valueOf(st);

        int page = 1;
        String p = req.getParameter("page");
        if (p != null && !p.isBlank()) page = Integer.parseInt(p);

        int size = 5;

<<<<<<< HEAD
        req.setAttribute("annonces", service.search(q, catId, status, page, size));
=======
        // Récupérer l'ID de l'utilisateur connecté de la session
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");

        // Filtrer par utilisateur connecté
        req.setAttribute("annonces", service.searchMyAnnonces(userId, q, catId, status, page, size));
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        req.setAttribute("page", page);
        req.setAttribute("q", q);
        req.setAttribute("cat", cat);
        req.setAttribute("status", st);

        req.getRequestDispatcher("/AnnonceList.jsp").forward(req, resp);
    }
}
