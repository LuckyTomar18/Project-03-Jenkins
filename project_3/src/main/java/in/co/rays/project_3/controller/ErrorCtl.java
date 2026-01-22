package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.util.ServletUtility;

/**
 * Global Error Controller
 * Handles DB down / runtime exceptions safely
 * 
 * @author Lucky
 */
@WebServlet(name = "ErrorCtl", urlPatterns = { "/ErrorCtl" })
public class ErrorCtl extends BaseCtl {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // ===== HTTP STATUS =====
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE); // 503

        // ===== FIND LAST CONTROLLER =====
        String lastCtl = (String) request.getAttribute("lastCtl");

        if (lastCtl == null) {
            lastCtl = (String) request.getAttribute("javax.servlet.error.request_uri");
        }

        System.out.println("Error on controller : " + lastCtl);

        // ===== ERROR MESSAGE =====
        ServletUtility.setErrorMessage(
                " Database service is Down.",
                request);

        // ===== LIST PAGE SAFETY =====
        if (lastCtl != null && lastCtl.contains("ListCtl")) {

            if (ServletUtility.getList(request) == null) {
                ServletUtility.setList(new ArrayList<>(), request);
            }

            request.setAttribute("pageNo", 1);
            request.setAttribute("pageSize", 10);
            request.setAttribute("nextListSize", 0);
        }

        // ===== RESOLVE VIEW =====
        String view = getViewFromCtl(lastCtl);

        // ===== ALWAYS FORWARD TO JSP (NEVER CTL) =====
        ServletUtility.forward(view, request, response);
    }

    /**
     * Resolve JSP view from controller
     */
    private String getViewFromCtl(String ctl) {

        if (ctl == null) {
            return ORSView.ERROR_VIEW;
        }

        // ===== LIST VIEWS (PRIORITY) =====
        if (ctl.contains("StaffListCtl"))
            return ORSView.STAFF__LIST_VIEW;

        if (ctl.contains("CollegeListCtl"))
            return ORSView.COLLEGE_LIST_VIEW;

        if (ctl.contains("StudentListCtl"))
            return ORSView.STUDENT_LIST_VIEW;

        if (ctl.contains("FacultyListCtl"))
            return ORSView.FACULTY_LIST_VIEW;

        if (ctl.contains("CourseListCtl"))
            return ORSView.COURSE_LIST_VIEW;

        if (ctl.contains("SubjectListCtl"))
            return ORSView.SUBJECT_LIST_VIEW;

        if (ctl.contains("RoleListCtl"))
            return ORSView.ROLE_LIST_VIEW;

        if (ctl.contains("UserListCtl"))
            return ORSView.USER_LIST_VIEW;

        if (ctl.contains("MarksheetListCtl"))
            return ORSView.MARKSHEET_LIST_VIEW;

        if (ctl.contains("MarksheetMeritListCtl"))
            return ORSView.MARKSHEET_MERIT_LIST_VIEW;

        // ===== FORM VIEWS =====
        if (ctl.contains("StaffCtl"))
            return ORSView.STAFF__VIEW;

        if (ctl.contains("CollegeCtl"))
            return ORSView.COLLEGE_VIEW;

        if (ctl.contains("StudentCtl"))
            return ORSView.STUDENT_VIEW;

        if (ctl.contains("FacultyCtl"))
            return ORSView.FACULTY_VIEW;

        if (ctl.contains("CourseCtl"))
            return ORSView.COURSE_VIEW;

        if (ctl.contains("SubjectCtl"))
            return ORSView.SUBJECT_VIEW;

        if (ctl.contains("RoleCtl"))
            return ORSView.ROLE_VIEW;

        if (ctl.contains("UserCtl"))
            return ORSView.USER_VIEW;
        
        if (ctl.contains("LoginCtl"))
            return ORSView.LOGIN_VIEW;

        // ===== DEFAULT ERROR =====
        return ORSView.ERROR_VIEW;
    }

    @Override
    protected String getView() {
        return ORSView.ERROR_VIEW;
    }
}
