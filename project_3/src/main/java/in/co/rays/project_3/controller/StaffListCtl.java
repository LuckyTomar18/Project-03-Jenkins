
package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.StaffModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "StaffListCtl", urlPatterns = { "/ctl/StaffListCtl" })
public class StaffListCtl extends BaseCtl {
	private static Logger log = Logger.getLogger(StaffListCtl.class);
	
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
	protected BaseDTO populateDTO(HttpServletRequest request) {
		
		StaffDTO dto = new StaffDTO();
		
		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setFullName(DataUtility.getString(request.getParameter("fullName")));
		dto.setJoiningDate(DataUtility.getDate(request.getParameter("joiningDate")));
		dto.setDivision(DataUtility.getString(request.getParameter("division")));
		dto.setPreviousEmployer(DataUtility.getString(request.getParameter("previousEmployer")));
		
		populateBean(dto, request);
		return dto ;
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		log.debug("StaffListCtl doGet Start");

		

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		StaffDTO dto = (StaffDTO) populateDTO(request);
		// get the selected checkbox ids array for delete list

		StaffModelInt model = ModelFactory.getInstance().getStaffModel();
		try {

		List<StaffDTO>	list = model.search(dto, pageNo, pageSize);

			
		List<StaffDTO>	next = model.search(dto, pageNo + 1, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {

			e.printStackTrace();
		}
		log.debug("StaffListCtl doGet End");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log.debug("StaffListCtl doPost Start");

		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		StaffDTO dto = (StaffDTO) populateDTO(request);

		String op = DataUtility.getString(request.getParameter("operation"));

       // get the selected checkbox ids array for delete list
		String[] ids = request.getParameterValues("ids");

		StaffModelInt model = ModelFactory.getInstance().getStaffModel();
		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STAFF_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.STAFF_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					StaffDTO deletedto = new StaffDTO();
					for (String id : ids) {
						deletedto.setId(DataUtility.getLong(id));
						model.delete(deletedto);
						ServletUtility.setSuccessMessage("Data Successfully Deleted!", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select atleast one record", request);
				}
			}
			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.STAFF_LIST_CTL, request, response);
				return;
			}
			dto = (StaffDTO) populateDTO(request);

			list = model.search(dto, pageNo, pageSize);

			ServletUtility.setDto(dto, request);
			next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setList(list, request);
			ServletUtility.setList(list, request);

			if (list == null || list.size() == 0) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.debug("UserListCtl doPost End");
	}
	
	@Override
	protected String getView() {
		return ORSView.STAFF__LIST_VIEW;
	}

}
