package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
public class AnnonceListServlet extends HttpServlet{

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String q = req.getParameter("q");

        Long catId = null;
        String cat = req.getParameter("cat");
        if (cat != null && !cat.isBlank()) catId = Long.parseLong(cat);

        AnnonceStatus status = null;
        String st = req.getParameter("status");
        if (st != null && !st.isBlank() && !"ALL".equalsIgnoreCase(st)) {
            status = AnnonceStatus.valueOf(st);
        }

        int page = 0;
        String p = req.getParameter("page");
        if (p != null && !p.isBlank()) {
            page = Integer.parseInt(p);
        }
        if (page < 0) {
            page = 0;
        }

        int size = 10;
        String s = req.getParameter("size");
        if (s != null && !s.isBlank()) {
            size = Integer.parseInt(s);
        }
        if (size <= 0) {
            size = 10;
        }
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");
        req.setAttribute("annonces", service.searchMyAnnonces(userId, q, catId, status, page, size));
        req.setAttribute("page", page);
        req.setAttribute("size", size);
        req.setAttribute("q", q);
        req.setAttribute("cat", cat);
        req.setAttribute("status", (st == null || st.isBlank()) ? "ALL" : st);

        req.getRequestDispatcher("/AnnonceList.jsp").forward(req, resp);
    }
}
