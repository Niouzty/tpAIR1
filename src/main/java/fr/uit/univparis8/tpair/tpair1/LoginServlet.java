package fr.uit.univparis8.tpair.tpair1;


import fr.uit.univparis8.tpair.tpair1.model.User;
import fr.uit.univparis8.tpair.tpair1.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/Login")
public class LoginServlet extends HttpServlet{
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
<<<<<<< HEAD
=======
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
<<<<<<< HEAD
=======
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User u = authService.authenticate(username, password);

        if (u == null) {
            req.setAttribute("error", "Identifiants invalides");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
<<<<<<< HEAD
        session.setAttribute("user", u.getUsername());
        session.setAttribute("userId", u.getId());

        resp.sendRedirect("AnnonceList");
    }
}
=======
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
        session.setAttribute("user", u.getUsername());
        session.setAttribute("userId", u.getId());

        resp.sendRedirect(req.getContextPath() + "/AnnonceList");
    }
}


>>>>>>> 67b61b3 (tp2 - update services, servlets and tests)
