package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		
		this.conn = conn;
	}

	@Override
	public void insert(Department dp) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("INSERT INTO department "+
										"(Name) "+
										"VALUES "+
									   "(?)",Statement.RETURN_GENERATED_KEYS);
			st.setString(1, dp.getName());
			int result = st.executeUpdate();
			if(result > 0) {
				rs = st.getGeneratedKeys();
				System.out.print("!Sucess insert");
				if(rs.next()) {
					
					int r = rs.getInt(1);
					dp.setId(r);
					
				}
			}
			else {
				System.out.println("!No row affect");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}


	@Override
	public void update(Department dp) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("UPDATE department "+
									   "SET Id = ?, Name = ? "+
										"WHERE Id = ?");
			st.setInt(1, dp.getId());
			st.setString(2, dp.getName());
			st.setInt(3, dp.getId());
			int result = st.executeUpdate();
			
				System.out.print("!Sucess update" + result);
				
			}
		
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Department findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Department> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
