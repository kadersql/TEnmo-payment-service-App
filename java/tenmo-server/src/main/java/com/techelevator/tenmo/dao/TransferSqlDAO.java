package com.techelevator.tenmo.dao;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.controller.TransferServiceException;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.ToFromTransfer;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;

@Component
public class TransferSqlDAO implements TransferDAO {

	private JdbcTemplate jdbcTemplate;

	public TransferSqlDAO(DataSource datasource) {
		jdbcTemplate = new JdbcTemplate(datasource);
	}

	public Balance getBalance(int userId) {
		String sql = "SELECT balance FROM accounts WHERE user_id = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		Balance userBalance = new Balance();

		if (results.next()) {
			userBalance.setBalance(results.getDouble("balance"));

		}

		return userBalance;
	}

	// The method signature needs to include the sender's id, the recepient id, and
	// the transferAmount

	// 1. get the sender's balance.

	// 2. get the recepient's balance.

	// 3. You want to check if the sender has enough money to send over.

	// 4. (Java) Calculate new balances for sender and recepient. You get new
	// balances for recepient and sender.

	// 5. run jdbc update to update the sender's balance with #4.
	// update accounts set balance = ? where user_id = ? , first placeholder is
	// going to be the new balance from 4, the second placeholder is going to be
	// sender's id.

	// 6. run jdbc update to update the recepient's balance with #4
	// update accounts set balance = ? where user_id = ? , first placeholder is
	// going to be the new balance from 4, the second placeholder is going to be
	// recepient's id.

	public void sendTEBuck(int Id, int pId, double tranAmount) throws TransferServiceException {

		double senderBalance = getBalance(pId).getBalance();
		double receiverBalance = getBalance(Id).getBalance();

		if (senderBalance >= tranAmount) {
			senderBalance = senderBalance - tranAmount;
			receiverBalance = receiverBalance + tranAmount;

			String sqlReceiver = "UPDATE accounts SET balance =  ?  where user_id = ?";
			jdbcTemplate.update(sqlReceiver, receiverBalance, Id);

			String sqlSender = "UPDATE accounts SET balance = ?  where user_id =  ?";
			jdbcTemplate.update(sqlSender, senderBalance, pId);

			String sqlTransfers = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (2, 2, ?, ?, ?)";
			jdbcTemplate.update(sqlTransfers, pId, Id, tranAmount);
		} else {
			
			throw new TransferServiceException();
		}
	}
	
	
	public void approvedRequestSendTebuck (int transId ) throws TransferServiceException {
		
		Transfer transfer = new Transfer();
		
		String sqlIdFrom = "select account_from from transfers where transfer_id = ?";
		SqlRowSet idFromresults = jdbcTemplate.queryForRowSet(sqlIdFrom, transId);
		if (idFromresults.next()) {
			transfer.setFromId(idFromresults.getInt("account_from"));
		}
		int idFrom = transfer.getFromId();
		
		
		String sqlIdTo = "select account_to from transfers where transfer_id = ?";
		SqlRowSet idToresults = jdbcTemplate.queryForRowSet(sqlIdTo, transId);
		if (idToresults.next()) {
			transfer.setToId(idToresults.getInt("account_to"));
		}
		int idTo = transfer.getToId();
		
		
		String sqlIdAmount = "select amount from transfers where transfer_id = ?";
		SqlRowSet idAmountresults = jdbcTemplate.queryForRowSet(sqlIdAmount, transId);
		if (idAmountresults.next()) {
			transfer.setAmount(idAmountresults.getDouble("amount"));
		}
		double amount = transfer.getAmount();
		
	
		double senderBalance = getBalance(idFrom).getBalance();
		double receiverBalance = getBalance(idTo).getBalance();

		if (senderBalance >= amount) {
			senderBalance = senderBalance - amount;
			receiverBalance = receiverBalance + amount;

			String sqlReceiver = "UPDATE accounts SET balance =  ?  where user_id = ?";
			jdbcTemplate.update(sqlReceiver, receiverBalance, idTo);

			String sqlSender = "UPDATE accounts SET balance = ?  where user_id =  ?";
			jdbcTemplate.update(sqlSender, senderBalance, idFrom);
			
			
			String sqlTransfer = "UPDATE transfers set transfer_type_id = 2, transfer_status_id = 2 WHERE transfer_id = ? ";
			jdbcTemplate.update (sqlTransfer, transId )	;

		
		} else {
			
			throw new TransferServiceException();
		}	
	}
	
	public void requestTEBuck (int Id, int pId, double requestAmount)  {
		
		String sqlTransfers = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (1, 1, ?, ?, ?)";
		jdbcTemplate.update(sqlTransfers, pId, Id, requestAmount);
		
	}
	
	public void requestReject (int Transid) {
		
		String sql = "UPDATE transfers set transfer_status_id = 3 WHERE transfer_id = ?";
		
		jdbcTemplate.update(sql, Transid );
		
	}

	public List<ToFromTransfer> myFromTransfers(int pId) {

		List<ToFromTransfer> fromTrans = new ArrayList<>();

		// 1. write and run a SQL that only gets the ones where you are the sender.
		// As you are looping, for the name property , we append From in front of it. 
		// Place all objects in a list
		
		// 2. write and run a SQL that only gets the ones where you are the recpient.
		// As you are looping, for the name property , we append To in front of it. 
		// Place all objects in a list

		String sqlFromTransfer = "select transfers.transfer_id, concat('From: ' ,users.username) as username, transfers.amount "
				+ "from transfers "
				+ "inner join accounts on transfers.account_from = accounts.account_id "
				+ "inner join users on accounts.user_id = users.user_id "
				+ "where transfers.account_to =? and transfers. transfer_status_id = 2 "
				+ "UNION "
				+ "select transfers.transfer_id, concat('To: ' ,users.username), transfers.amount "
				+ "from transfers "
				+ "inner join accounts on transfers.account_to = accounts.account_id "
				+ "inner join users on accounts.user_id = users.user_id "
				+ "where transfers.account_from =? and transfers. transfer_status_id = 2  ";

		// ( 'From: ' , 'users.username')

		SqlRowSet fromResult = jdbcTemplate.queryForRowSet(sqlFromTransfer, pId, pId);
	

		while (fromResult.next()) {
			int transId = fromResult.getInt("transfer_id");
			String name = fromResult.getString("username");
			double amount = fromResult.getDouble("amount");

			ToFromTransfer transfer = new ToFromTransfer(transId, name, amount);

			fromTrans.add(transfer);
		
		}
		
		return fromTrans;
	}

//	public List <ToFrom> myPanding
	
	public List<ToFromTransfer> myPandingTransfers(int pId) {

		List<ToFromTransfer> pendingTrans = new ArrayList<>();


		String sqlFromTransfer = "select transfers.transfer_id, users.username, transfers.amount "
				+ "from transfers "
				+ "inner join accounts on transfers.account_from = accounts.account_id "
				+ "inner join users on accounts.user_id = users.user_id "
				+ "where transfers.account_to =? and transfers. transfer_status_id = 1 ";
	
		SqlRowSet fromResult = jdbcTemplate.queryForRowSet(sqlFromTransfer, pId);
	

		while (fromResult.next()) {
			int transId = fromResult.getInt("transfer_id");
			String name = fromResult.getString("username");
			double amount = fromResult.getDouble("amount");

			ToFromTransfer transfer = new ToFromTransfer(transId, name, amount);

			pendingTrans.add(transfer);
		
		}
		
		return pendingTrans;
	}


	public List <TransferDetails> transDetails (int tId) {
		
		List <TransferDetails> tDetails = new ArrayList<>();
		
		String sql = "select transfers.transfer_id as id, "
				+ "(select users.username from transfers "
				+ "inner join accounts on transfers.account_from = accounts.account_id "
				+ "inner join users on accounts.user_id = users.user_id "
				+ "where transfers.transfer_id =?) as From, "
				+ "users.username as to, transfer_types.transfer_type_desc as Type, "
				+ "transfer_statuses.transfer_status_desc as status, transfers.amount "
				+ "from transfers "
				+ "inner join accounts on transfers.account_to= accounts.account_id "
				+ "inner join users on accounts.user_id = users.user_id "
				+ "inner join transfer_types on transfer_types.transfer_type_id = transfers.transfer_type_id "
				+ "inner join transfer_statuses on transfer_statuses.transfer_status_id = transfers.transfer_status_id "
				+ "where transfers.transfer_id = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql,tId, tId);
		while (results.next()) {
			int id = results.getInt("id");
			String from = results.getString("from");
			String to = results.getString("to");
			String type = results.getString("type");
			String status = results.getString("status");
			double amount = results.getDouble("amount");
			
			
			System.out.print(from);
						
			TransferDetails transferDetails = new TransferDetails(id, from, to, type, status, amount);
			
			tDetails.add(transferDetails);
			
			
		}
		
		return tDetails;
	}

	
	
}
	
