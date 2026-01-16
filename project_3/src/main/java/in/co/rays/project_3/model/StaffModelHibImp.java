package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class StaffModelHibImp implements StaffModelInt {

	@Override
	public long add(StaffDTO dto) throws ApplicationException, DuplicateRecordException {

	    Session session = null;
	    Transaction tx = null;
	    long pk = 0;

	    try {
	        session = HibDataSource.getSession();
	        tx = session.beginTransaction();

	        session.save(dto);
	        pk = dto.getId();

	        tx.commit();

	    } catch (Exception e) {

	        //  SAFE rollback
	        try {
	            if (tx != null && tx.isActive()) {
	                tx.rollback();
	            }
	        } catch (Exception rbEx) {
	            // DB already down, ignore rollback failure
	        }

	        throw new ApplicationException("Database is down, please try later"+ e);

	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
	    return pk;
	}


	@Override
	public void delete(StaffDTO dto) throws ApplicationException {

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Staff Delete" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public void update(StaffDTO dto) throws ApplicationException, DuplicateRecordException {

		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Staff update" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public StaffDTO findByPK(long pk) throws ApplicationException {

		Session session = null;
		StaffDTO dto = null;

		try {
			session = HibDataSource.getSession();
			dto = (StaffDTO) session.get(StaffDTO.class, pk);
			
		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in getting Staff by pk");
		} finally {
			session.close();
		}
		System.out.println("++++"+dto);
		return dto;
	}

	@Override
	public List list() throws ApplicationException {
		return search(null, 0, 0);
	}

	@Override
	public List search(StaffDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(StaffDTO.class);
			if (dto.getId() > 0) {
				criteria.add(Restrictions.eq("id", dto.getId()));

			}
			if(dto.getFullName()!=null && dto.getFullName().length()>0){
				criteria.add(Restrictions.like("fullName", dto.getFullName()+"%"));
			}
			if(dto.getJoiningDate()!=null && dto.getJoiningDate().getTime()>0){
				criteria.add(Restrictions.like("joiningDate", dto.getJoiningDate()+"%"));
			}
			if(dto.getDivision()!=null&&dto.getDivision().length()>0){
				criteria.add(Restrictions.like("division", dto.getDivision()+"%"));
			}
			if(dto.getPreviousEmployer()!=null&&dto.getPreviousEmployer().length()>0){
				criteria.add(Restrictions.like("previousEmployer", dto.getPreviousEmployer()+"%"));
			}
			if(pageSize>0){
				criteria.setFirstResult((pageNo-1)*pageSize);
				criteria.setMaxResults(pageSize);
				
			}
              list=criteria.list();
		}catch (HibernateException e) {
           e.printStackTrace();
            throw new ApplicationException("Exception in staff search");
        } finally {
            session.close();
        }
		return list;
	}

	

	}

	

	
	 	