package com.techelevator.tenmo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.tenmo.dao.TransferSqlDAO;

class TheIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private TransferSqlDAO dao;
	

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
		dataSource.setUrl("jdbc:postgresql://localhost:5432/tenmo");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@BeforeEach
	void setUp() throws Exception {
		dao = new TransferSqlDAO(dataSource);
	}

	@AfterEach
	void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	void Customer_1_has_a_balance_of_1000() {
		double actualResult = dao.getBalance(1).getBalance();
		
		assertEquals(1000, actualResult,0);
	}
	
	
	
	
	
	
	
	
	
	

}
