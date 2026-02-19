package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import fr.uit.univparis8.tpair.tpair1.service.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

import java.io.IOException;
public class AnnonceUpdateServlet extends HttpServlet {

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Long id = Long.parseLong(req.getParameter("id"));
        Annonce a = service.findById(id);

        if (a == null) {
            resp.sendRedirect("AnnonceList");
            return;
        }

        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (!a.getAuthor().getId().equals(userId)) {
            resp.sendRedirect("AnnonceList");
            return;
        }

        req.setAttribute("form", a);
        req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Long id = Long.parseLong(req.getParameter("id"));

        Annonce a = new Annonce();
        a.setId(id);
        a.setTitle(req.getParameter("title"));
        a.setDescription(req.getParameter("description"));
        a.setAdress(req.getParameter("adress"));
        a.setMail(req.getParameter("mail"));

        try {
            HttpSession session = req.getSession();
            Long userId = (Long) session.getAttribute("userId");
            boolean ok = service.update(a, userId);
            if (!ok) {
                req.setAttribute("error", "Annonce non trouvée ou accès refusé");
                req.setAttribute("form", a);
                req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect("AnnonceList");
        } catch (ValidationException ve) {
            req.setAttribute("errors", ve.getErrors());
            req.setAttribute("form", a);
            req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
        }
    }
}


