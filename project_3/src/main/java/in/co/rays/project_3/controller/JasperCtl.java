package in.co.rays.project_3.controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.Session;

import in.co.rays.project_3.dto.UserDTO;
import in.co.rays.project_3.util.HibDataSource;
import in.co.rays.project_3.util.JDBCDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@WebServlet(name = "JasperCtl", urlPatterns = { "/ctl/JasperCtl" })
public class JasperCtl extends BaseCtl {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ResourceBundle rb = ResourceBundle.getBundle("in.co.rays.project_3.bundle.system");
        Connection conn = null;

        try {

            /* Jasper file path */
            String jasperFile = System.getenv("jasperctl");

            if (jasperFile == null) {
                jasperFile = getServletContext().getRealPath("/jasper/dabi.jrxml");
            }

            /* Compile jrxml */
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperFile);

            /* Session user */
            HttpSession session = request.getSession(false);
            UserDTO user = (UserDTO) session.getAttribute("user");

            if (user == null) {
                throw new ServletException("User not logged in");
            }

            /* Parameters */
            Map<String, Object> map = new HashMap<>();
            map.put("ID", 1L);

            String database = rb.getString("DATABASE");

            /* ===== JDBC MODE ===== */
            if ("JDBC".equalsIgnoreCase(database)) {
                conn = JDBCDataSource.getConnection();

                JasperPrint jasperPrint =
                        JasperFillManager.fillReport(jasperReport, map, conn);

                byte[] pdf = JasperExportManager.exportReportToPdf(jasperPrint);
                response.setContentType("application/pdf");
                response.getOutputStream().write(pdf);
            }

            /* ===== HIBERNATE MODE ===== */
            if ("Hibernate".equalsIgnoreCase(database)) {

                Session hibSession = HibDataSource.getSession();

                hibSession.doWork(connection -> {
                    try {
                        JasperPrint jasperPrint =
                                JasperFillManager.fillReport(jasperReport, map, connection);

                        byte[] pdf =
                                JasperExportManager.exportReportToPdf(jasperPrint);

                        response.setContentType("application/pdf");
                        response.getOutputStream().write(pdf);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            response.getOutputStream().flush();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        } finally {
            JDBCDataSource.closeConnection(conn);
        }
    }

    @Override
    protected String getView() {
        return null;
    }
}
