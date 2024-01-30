package application;

import java.util.List;

import model.dao.FactoryDao;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = FactoryDao.createSellerDao();
		Seller seller = sellerDao.findById(1);
		System.out.println(seller);
		Department dep = new Department(3, null);
		
		List<Seller> list = sellerDao.findAll();
		
		for(Seller obj : list) {
			System.out.println(obj);
		}

	}

}
