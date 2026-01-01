package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import in.co.rays.project_3.dto.PatientDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class PatientModelHibImp implements PatientModelInt {

	@Override
	public long add(PatientDTO dto) throws ApplicationException, DuplicateRecordException {
		
		Session session = null;
		Transaction tx = null;
		
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		}  catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Staff Add " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();
	}

	@Override
	public void delete(PatientDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(PatientDTO dto) throws ApplicationException, DuplicateRecordException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PatientDTO findByPK(long pk) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List search(PatientDTO dto, int pageNo, int pageSize) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

}
