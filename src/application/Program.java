package application;

import java.util.Date;
import java.util.List;

import model.dao.FactoryDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = FactoryDao.createSellerDao();
		
		
		Department dep = new Department(3, null);
		//insert
		//Seller seller = new Seller(null,"ruma","livia@gmail.com",new Date(),2000.00,dep);
		
		//update
		/*Seller slnew = sellerDao.findById(14);
		slnew.setName("Cotiude");
		sellerDao.update(slnew);*/
		
		//delete
		//sellerDao.deleteById(14);
		
		//findbyid
		//System.out.print(sellerDao.findById(1));
		
		//findbydepartment
		//System.out.print(sellerDao.findByDepartment(dep));
		
		//findall
		
		System.out.print(sellerDao.findAll());

	}

}
