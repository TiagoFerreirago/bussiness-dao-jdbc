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
	
	public void insert(Seller sl) {

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO seller "+
									   "(Name,Email,BirthDate,BaseSalary,DepartmentId)"+
									   	"VALUES "+
									    "(?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, sl.getName());
			ps.setString(2, sl.getEmail());
			ps.setDate(3, new java.sql.Date(sl.getBirthDate().getTime()));;
			ps.setDouble(4, sl.getBaseSalary());
			ps.setInt(5, sl.getDepartment().getId());
			
			int result = ps.executeUpdate();
			System.out.println("!Done row "+result);
			if(result > 0) {
				ResultSet st = ps.getGeneratedKeys();
				if(st.next()) {
					int sa = st.getInt(1);
					System.out.println("!Done id = "+sa);
					sl.setId(sa);
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
			DB.closeStatement(ps);
		}
	}
	@Override
	public void update(Seller sl) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("UPDATE seller "+
										"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "+
										"WHERE Id = ?");
		
			st.setString(1,sl.getName());
			st.setString(2,sl.getEmail());
			st.setDate(3, new java.sql.Date(sl.getBirthDate().getTime()));
			st.setDouble(4, sl.getBaseSalary());
			st.setInt(5, sl.getDepartment().getId());
			st.setInt(6, sl.getId());
			System.out.print("Modifield"+ st.executeUpdate());
		
		
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
		  DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM seller "+
										"WHERE Id = ?");
		
			st.setInt(1, id);
					
			int respost = st.executeUpdate();
			System.out.println("!Sucess delete"+ respost);
		}
		catch(SQLException e ) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
		
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
