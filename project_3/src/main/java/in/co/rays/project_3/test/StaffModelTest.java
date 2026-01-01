package in.co.rays.project_3.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.StaffModelHibImp;

public class StaffModelTest {
	
	public static StaffModelHibImp model = new StaffModelHibImp();
	public static void main(String[] args) throws ParseException, ApplicationException, DuplicateRecordException {
		
		testAdd();
		
	}
	
	public static void testAdd() throws ParseException, ApplicationException, DuplicateRecordException {
		
		StaffDTO dto = new StaffDTO();SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		
		dto.setFullName("Lucky Tomar");
		dto.setJoiningDate(sdf.parse("18-01-2005"));
		dto.setDivision("HR");
		dto.setPreviousEmployer("INFOSYS");
		dto.setCreatedBy("admin");
		dto.setModifiedBy("admin");
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		
		System.out.println("add");
		 long pk = model.add(dto); 
		System.out.println(pk + "data successfully insert"); 
	}
}
