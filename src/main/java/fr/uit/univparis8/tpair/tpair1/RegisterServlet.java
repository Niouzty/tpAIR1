package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.service.AuthService;
import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

// DÉSACTIVÉ : TP3 utilise une API REST pure, pas de servlets
// @WebServlet("/Register")
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User u = authService.register(username, email, password);

            // Créer la session et y ajouter les infos utilisateur
            HttpSession session = req.getSession(true);
            session.setAttribute("user", u.getUsername());
            session.setAttribute("userId", u.getId());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes

            // Rediriger vers la liste des annonces
            resp.sendRedirect(req.getContextPath() + "/AnnonceList");

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }
}


