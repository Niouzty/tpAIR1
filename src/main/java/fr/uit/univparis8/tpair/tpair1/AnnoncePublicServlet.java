package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.model.Annonce;
import fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus;
import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AnnoncePublicServlet extends HttpServlet {

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");

        List<Annonce> annonces = service.listAll()
                .stream()
                .filter(a -> a != null && AnnonceStatus.PUBLISHED.equals(a.getStatus()))
                .collect(Collectors.toList());

        req.setAttribute("annonces", annonces);
        req.getRequestDispatcher("/AnnoncesPubliees.jsp").forward(req, resp);
    }
}
