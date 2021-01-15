package com.techelevator.tenmo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.TransferDAO;
import com.techelevator.tenmo.dao.TransferSqlDAO;
import com.techelevator.tenmo.dao.UserDAO;
import com.techelevator.tenmo.dao.UserSqlDAO;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.ToFromTransfer;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDetails;
import com.techelevator.tenmo.model.User;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

	@Autowired
	TransferDAO transferDao;

	@Autowired
	TransferSqlDAO transferSqlDAO;

	@Autowired
	UserSqlDAO userSqlDAO;

	@Autowired
	UserDAO userDao;

	@RequestMapping(path = "/get-balance", method = RequestMethod.GET)
	public Balance getBalance(Principal principal) {

		int id = userDao.findIdByUsername(principal.getName());

		Balance balance = transferDao.getBalance(id);

		return balance;
	}

	@RequestMapping(path = "/transfer", method = RequestMethod.POST)
	// transfer(@RequestBody Transfer transfer)
	public void transfer(@RequestBody Transfer transfer) throws TransferServiceException {

		double amount = transfer.getAmount();
		int senderId = transfer.getFromId();
		int receiverId = transfer.getToId();

		transferSqlDAO.sendTEBuck(receiverId, senderId, amount);
	}
	
	@RequestMapping(path = "/approvedrequest/{id}", method = RequestMethod.POST)
	// transfer(@RequestBody Transfer transfer)
	public void aprovedReqTrans(@PathVariable int id) throws TransferServiceException {
		
		transferSqlDAO.approvedRequestSendTebuck(id);
	}
	
	
	@RequestMapping(path = "/request", method = RequestMethod.POST)
	public void request (@RequestBody Transfer transfer) {
		
		double amount = transfer.getAmount();
		int senderId = transfer.getFromId();
		int requestId = transfer.getToId();

		transferSqlDAO.requestTEBuck(requestId, senderId, amount);
		
	}
	
	@RequestMapping (path = "/requestreject/{transId}", method = RequestMethod.POST)
	public void requestReject (@PathVariable int transId , @RequestBody Transfer transfer) {
		
		transferSqlDAO.requestReject(transId);
	}

	@RequestMapping(path = "/findallbutuser", method = RequestMethod.GET)
	public List<User> findAllButUser(Principal principal) {
		int id = userDao.findIdByUsername(principal.getName());

		List<User> allButUser = userSqlDAO.findAllButUser(id);

		return allButUser;
	}

	@RequestMapping(path = "/fromTransfer", method = RequestMethod.GET)
	public List<ToFromTransfer> myToFromTransfer(Principal principal) {

		int id = userDao.findIdByUsername(principal.getName());

		List<ToFromTransfer> fromTransder = transferSqlDAO.myFromTransfers(id);
	

		return fromTransder;
	}
	
	@RequestMapping(path = "/pendingtransfer", method = RequestMethod.GET)
	public List<ToFromTransfer> myPendingTransfer(Principal principal) {

		int id = userDao.findIdByUsername(principal.getName());

		List<ToFromTransfer> pandingTransder = transferSqlDAO.myPandingTransfers(id);
	

		return pandingTransder;
	}
	
	
	@RequestMapping (path = "/transdetails/{id}", method = RequestMethod.GET)
	public List <TransferDetails> tdetails (@PathVariable int id ) {
		
		List <TransferDetails>  transdetails =   transferSqlDAO.transDetails(id);
		
		return  transdetails;
	}
	
	
	
	
	
	
	
	
	

}
