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
<<<<<<< HEAD
        Long id = Long.parseLong(req.getParameter("id"));
        Annonce a = service.findById(id);
=======
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        Long id = Long.parseLong(req.getParameter("id"));
        Annonce a = service.findById(id);

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        if (a == null) {
            resp.sendRedirect("AnnonceList");
            return;
        }
<<<<<<< HEAD
=======

        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");
        if (!a.getAuthor().getId().equals(userId)) {
            resp.sendRedirect("AnnonceList");
            return;
        }

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        req.setAttribute("form", a);
        req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

<<<<<<< HEAD
=======
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        Long id = Long.parseLong(req.getParameter("id"));

        Annonce a = new Annonce();
        a.setId(id);
        a.setTitle(req.getParameter("title"));
        a.setDescription(req.getParameter("description"));
        a.setAdress(req.getParameter("adress"));
        a.setMail(req.getParameter("mail"));

        try {
<<<<<<< HEAD
            boolean ok = service.update(a);
=======
            // Récupérer l'ID de l'utilisateur connecté
            HttpSession session = req.getSession();
            Long userId = (Long) session.getAttribute("userId");

            // Passer userId au service pour vérifier l'ownership
            boolean ok = service.update(a, userId);
            if (!ok) {
                req.setAttribute("error", "Annonce non trouvée ou accès refusé");
                req.setAttribute("form", a);
                req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
                return;
            }
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
            resp.sendRedirect("AnnonceList");
        } catch (ValidationException ve) {
            req.setAttribute("errors", ve.getErrors());
            req.setAttribute("form", a);
            req.getRequestDispatcher("/AnnonceUpdate.jsp").forward(req, resp);
        }
    }
<<<<<<< HEAD
}
=======
}

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
