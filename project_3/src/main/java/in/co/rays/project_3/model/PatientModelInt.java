package in.co.rays.project_3.model;

import java.util.List;

import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;

public interface PatientModelInt {

	public long add(PatientDTO dto)throws ApplicationException,DuplicateRecordException;
	public void delete(PatientDTO dto)throws ApplicationException;
	public void update(PatientDTO dto)throws ApplicationException,DuplicateRecordException;
	public PatientDTO findByPK(long pk)throws ApplicationException;
	public List list()throws ApplicationException;
	public List search(PatientDTO dto,int pageNo,int pageSize)throws ApplicationException;
}
