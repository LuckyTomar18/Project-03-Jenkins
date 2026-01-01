package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StaffModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "StaffCtl", urlPatterns = { "/ctl/StaffCtl" })
public class StaffCtl extends BaseCtl {

	private static Logger log = Logger.getLogger(SubjectListCtl.class);
	
	@Override
	protected void preload(HttpServletRequest request) {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("IT", "IT");
		map.put("HR", "HR");
		map.put("FINANCE", "FINANCE");
		map.put("MARKETING", "MARKETING");

		request.setAttribute("map", map);
	}
	
	@Override
	protected boolean validate(HttpServletRequest request) {
		
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("fullName"))) {
			request.setAttribute("fullName", PropertyReader.getValue("error.require", "firstName"));
		} else if (!DataValidator.isName(request.getParameter("fullName"))) {
			request.setAttribute("fullName", "Invalid Full Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("joiningDate"))) {
			request.setAttribute("joiningDate", PropertyReader.getValue("error.require", "Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("joiningDate"))) {
			request.setAttribute("joiningDate", PropertyReader.getValue("error.date", "Date"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("division"))) {
			request.setAttribute("division", PropertyReader.getValue("error.require", "division"));
			pass = false;
		}
		if (DataValidator.isNull(request.getParameter("previousEmployer"))) {
			request.setAttribute("previousEmployer", PropertyReader.getValue("error.require", "previousEmployer"));
			pass = false;
		}
		return pass;
	}
	
	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		StaffDTO dto = new StaffDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setFullName(DataUtility.getString(request.getParameter("fullName")));
		dto.setJoiningDate(DataUtility.getDate(request.getParameter("joiningDate")));
		dto.setDivision(DataUtility.getString(request.getParameter("division")));
		dto.setPreviousEmployer(DataUtility.getString(request.getParameter("previousEmployer")));

		populateBean(dto, request);
		return dto;	
		}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		StaffModelInt model = ModelFactory.getInstance().getStaffModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (id > 0 || op != null) {
			System.out.println("in id > 0  condition");
			StaffDTO dto = null;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String op = DataUtility.getString(request.getParameter("operation"));

		// get model
		StaffModelInt model = ModelFactory.getInstance().getStaffModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {

			StaffDTO dto = (StaffDTO) populateDTO(request);

			

			try {
				if (id > 0) {
					model.update(dto);
					ServletUtility.setSuccessMessage("Data is successfully Updated", request);
				} else {

					try {
						model.add(dto);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (ApplicationException e) {
						log.error(e);
						ServletUtility.handleException(e, request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Login id already exists", request);
					}

				}
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			StaffDTO dto = (StaffDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.STAFF_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.STAFF_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.STAFF_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("StaffCtl Method doPostEnded");
	}
	
	@Override
	protected String getView() {
		return ORSView.STAFF__VIEW;
	}

}
