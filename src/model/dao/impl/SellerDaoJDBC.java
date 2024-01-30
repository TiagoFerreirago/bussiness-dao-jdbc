package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	public SellerDaoJDBC(Connection cn) {
		this.conn= cn;
	}
	
	@Override
	public void insert(Seller sl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Seller sl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}
	public Department impDepartment(ResultSet rs) throws SQLException {
		Department dpt = new Department();
		dpt.setId(rs.getInt("DepartmentId"));
		dpt.setName(rs.getString("DepName"));
		return dpt;
		
	}
	
	public Seller impSeller(ResultSet rs,Department dp) throws SQLException {
		Seller sl = new Seller();
		sl.setId(rs.getInt("Id"));
		sl.setName(rs.getString("Name"));
		sl.setEmail(rs.getString("Email"));
		sl.setBirthDate(rs.getDate("BirthDate"));
		sl.setBaseSalary(rs.getDouble("BaseSalary"));
		sl.setDepartment(dp);
		return sl;
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet  rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "+
                                       "FROM seller INNER JOIN department "+
                                        "ON seller.DepartmentId = department.Id "+
                                         "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				
				Department department = impDepartment(rs);
				Seller sl = impSeller(rs, department);
				return sl;
			}
			return null;
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
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement  st = null;
		ResultSet rs= null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "+
										"FROM seller INNER JOIN department "+
										"ON seller.DepartmentId = department.Id "+
										"WHERE DepartmentId = ? "+
										"ORDER BY Name");
			st.setInt(1, department.getId());
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId"));
				if(dep == null) {
				dep = impDepartment(rs);
				map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = impSeller(rs, dep);
				list.add(seller);
			}
			return list;
		
	}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
		
	@Override
	public List<Seller> findAll() {
		Statement stan = null;
		ResultSet result = null;
		
		try {
			
				stan = conn.createStatement();
				result = stan.executeQuery("SELECT seller.*,department.Id AS Id,department.Name AS DepName "+
										"From seller INNER JOIN department "+
										"ON seller.DepartmentId = department.Id "+
										"ORDER BY seller.Id");
				
				List<Seller> list= new ArrayList<>();
				while(result.next()) {
					Department dp = impDepartment(result);
					Seller seller = impSeller(result, dp);
					list.add(seller);
				}
				return list;
		}
		
		catch(SQLException e) {
			throw new DbException (e.getMessage());
		}
	}
}
