package Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html; charset=UTF-8");
        HttpSession session = req.getSession();
        List<String> articulo = (List<String>) session.getAttribute("articulo");
        List<Double> valores = (List<Double>) session.getAttribute("valor");
        List<Double> cantidades = (List<Double>) session.getAttribute("cantidad");

        if (articulo == null){
            articulo = new ArrayList<>();
            session.setAttribute("articulo", articulo);
        }
        if (valores == null){
            valores = new ArrayList<>();
            session.setAttribute("valor", valores);
        }
        if (cantidades == null){
            cantidades = new ArrayList<>();
            session.setAttribute("cantidad", cantidades);
        }

        String articuloNuevo = req.getParameter("articulo");
        double valorNuevo = Double.parseDouble(req.getParameter("valor"));
        double cantidadNueva = Double.parseDouble(req.getParameter("cantidad"));

        if (articuloNuevo != null && !articuloNuevo.trim().equals("") && valorNuevo != 0){
            articulo.add(articuloNuevo);
            valores.add(valorNuevo);
            cantidades.add(cantidadNueva);
        }

        double subtotal = 0;
        double total = 0;
        double acusub = 0;
        double iva = 0.15;

        try (PrintWriter out = resp.getWriter()){
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Factura</title>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; }");
            out.println("table { width: 100%; border-collapse: collapse; }");
            out.println("th, td { padding: 8px; text-align: left; border-bottom: 1px solid #ddd; }");
            out.println("th { background-color: #f2f2f2; }");
            out.println(".container { max-width: 600px; margin: 50px auto; padding: 20px; border: 1px solid #ccc; border-radius: 10px; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");

            out.println("<div class=\"container\">");
            out.println("<h1>Factura de Compra</h1>");

            out.println("<table>");
            out.println("<tr><th>Artículo</th><th>Precio Unitario</th><th>Cantidad</th><th>Subtotal</th></tr>");

            for (int i = 0; i < articulo.size(); i++) {
                out.println("<tr>");
                out.println("<td>" + articulo.get(i) + "</td>");
                out.println("<td>$" + valores.get(i) + "</td>");
                out.println("<td>" + cantidades.get(i) + "</td>");
                subtotal = valores.get(i) * cantidades.get(i);
                out.println("<td>$" + subtotal + "</td>");
                out.println("</tr>");
                acusub += subtotal;
            }

            out.println("<tr><td colspan=\"3\"><strong>Subtotal:</strong></td><td>$" + acusub + "</td></tr>");
            double ivafinal = acusub * iva;
            total = ivafinal + acusub;
            out.println("<tr><td colspan=\"3\"><strong>IVA (15%):</strong></td><td>$" + ivafinal + "</td></tr>");
            out.println("<tr><td colspan=\"3\"><strong>Total:</strong></td><td>$" + total + "</td></tr>");

            out.println("</table>");

            out.println("<br>");
            out.println("<a href='index.html'>Volver al Inicio</a>");
            out.println("</div>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}