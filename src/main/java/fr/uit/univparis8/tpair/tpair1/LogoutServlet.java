package fr.uit.univparis8.tpair.tpair1;

//import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

// DÉSACTIVÉ : TP3 utilise une API REST pure
// @WebServlet("/Logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
        resp.sendRedirect("Login");
    }
}