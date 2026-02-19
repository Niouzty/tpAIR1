package fr.uit.univparis8.tpair.tpair1;

import java.io.*;

import jakarta.servlet.http.*;
public class HelloServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String nom = request.getParameter("nom");

        out.println("<!DOCTYPE html><html><body>");

        if (nom != null && !nom.trim().isEmpty()) {
            out.println("<h1>Bonjour " + nom + "</h1>");
        } else {
            out.println("<h1>Aucun nom reçu</h1>");
        }

        out.println("</body></html>");
    }
}
