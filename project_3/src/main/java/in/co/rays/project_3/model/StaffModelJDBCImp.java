
package in.co.rays.project_3.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.project_3.dto.StaffDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DatabaseException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.JDBCDataSource;

public class StaffModelJDBCImp implements StaffModelInt {
	
	public Integer nextPk() throws DatabaseException {

		Connection conn = null;
		int pk = 0;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_staff");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				pk = rs.getInt(1);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			throw new DatabaseException("Exception : Exception in getting PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public long add(StaffDTO dto) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;
		int pk = 0;

	
		try {
			pk = nextPk();
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_staff values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, dto.getFullName());
			pstmt.setDate(3, new java.sql.Date(dto.getJoiningDate().getTime()));
			pstmt.setString(4, dto.getDivision());
			pstmt.setString(5, dto.getPreviousEmployer());
			pstmt.setString(6, dto.getCreatedBy());
			pstmt.setString(7, dto.getModifiedBy());
			pstmt.setTimestamp(8, dto.getCreatedDatetime());
			pstmt.setTimestamp(9, dto.getModifiedDatetime());
			pstmt.executeUpdate();

			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ApplicationException("Exception : add rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in add staff");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}


	public void update(StaffDTO dto) throws ApplicationException, DuplicateRecordException {
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_staff set full_name = ?, joining_date = ?, division = ?, previous_employer = ?, created_by = ?, modified_by = ?, created_datetime = ?, modified_datetime = ? where id = ?");
			pstmt.setString(1, dto.getFullName());
			pstmt.setDate(2, new java.sql.Date(dto.getJoiningDate().getTime()));
			pstmt.setString(3, dto.getDivision());
			pstmt.setString(4, dto.getPreviousEmployer());
			pstmt.setString(5, dto.getCreatedBy());
			pstmt.setString(6, dto.getModifiedBy());
			pstmt.setTimestamp(7, dto.getCreatedDatetime());
			pstmt.setTimestamp(8, dto.getModifiedDatetime());
			pstmt.setLong(9, dto.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception in updating Staff ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	@Override
	public void delete(StaffDTO dto) throws ApplicationException {
		
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_staff where id = ?");
			pstmt.setLong(1, dto.getId());
			pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception : Delete rollback exception " + ex.getMessage());
			}
			throw new ApplicationException("Exception : Exception in delete staff");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		
	}

	@Override
	public StaffDTO findByPK(long pk) throws ApplicationException {
		StaffDTO bean = null;
		Connection conn = null;

		StringBuffer sql = new StringBuffer("select * from st_staff where id = ?");

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			pstmt.setLong(1, pk);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				bean = new StaffDTO();
				bean.setId(rs.getLong(1));
				bean.setFullName(rs.getString(2));
				bean.setJoiningDate(rs.getDate(3));
				bean.setDivision(rs.getString(4));
				bean.setPreviousEmployer(rs.getString(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in getting staff by pk");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	@Override
	public List list() throws ApplicationException {
		return search(null, 0, 0);
	}

	@Override
	public List search(StaffDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Connection conn = null;
		ArrayList<StaffDTO> list = new ArrayList<StaffDTO>();

		StringBuffer sql = new StringBuffer("select * from st_staff where 1=1");

		if (dto != null) {
			if (dto.getId() > 0) {
				sql.append(" and id = " + dto.getId());
			}
			if (dto.getFullName() != null && dto.getFullName().length() > 0) {
				sql.append(" and full_name like '" + dto.getFullName() + "%'");
			}
			if (dto.getJoiningDate() != null && dto.getJoiningDate().getTime() > 0) {
				sql.append(" and joining_date like '" + new java.sql.Date(dto.getJoiningDate().getTime()) + "%'");
			}
			if (dto.getDivision() != null && dto.getDivision().length() > 0) {
				sql.append(" and division like '" + dto.getDivision() + "%'");
			}
			if (dto.getPreviousEmployer() != null && dto.getPreviousEmployer().length() > 0) {
				sql.append(" and previous_employer like '" + dto.getPreviousEmployer() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}
		System.out.println("sql ===== > " + sql.toString());
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				dto = new StaffDTO();
				dto.setId(rs.getLong(1));
				dto.setFullName(rs.getString(2));
				dto.setJoiningDate(rs.getDate(3));
				dto.setDivision(rs.getString(4));
				dto.setPreviousEmployer(rs.getString(5));
				dto.setCreatedBy(rs.getString(6));
				dto.setModifiedBy(rs.getString(7));
				dto.setCreatedDatetime(rs.getTimestamp(8));
				dto.setModifiedDatetime(rs.getTimestamp(9));
				list.add(dto);
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception : Exception in search staff");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}
	}



	
	


