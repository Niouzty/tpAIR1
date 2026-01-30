package fr.uit.univparis8.tpair.tpair1;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "formulaireServlet", value = "/formulaire-servlet")
public class FormulaireServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();

        String nom = request.getParameter("nom");

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Formulaire</title></head><body>");

        out.println("<h1>Saisie du nom</h1>");

        out.println("<form method='get' action='hello-servlet'>");
        out.println("Nom : <input type='text' name='nom' required /> <br><br>");
        out.println("<input type='submit' value='Envoyer' />");
        out.println("</form>");

        if (nom != null && !nom.isEmpty()) {
            out.println("<h3>Bonjour " + nom + " !</h3>");
        }
        out.println("</body></html>");
    }
}
