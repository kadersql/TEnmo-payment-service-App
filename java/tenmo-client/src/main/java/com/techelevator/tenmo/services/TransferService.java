package com.techelevator.tenmo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.models.Balance;
import com.techelevator.tenmo.models.ToFromTransfer;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.TransferDetails;
import com.techelevator.tenmo.models.User;
import com.techelevator.view.ConsoleService;

public class TransferService {

	public static String AUTH_TOKEN = "";
	private final String BASE_URL;
	private final RestTemplate restTemplate = new RestTemplate();
	TransferDetails transferDetails	;

	public TransferService(String url) {
		BASE_URL = url;
	}

	public Balance getBalanceFromServer() {

		Balance balance = restTemplate
				.exchange(BASE_URL + "/get-balance", HttpMethod.GET, makeAuthEntity(), Balance.class).getBody();
		return balance;
	}

	public void transfer(double tAmount, int fromId, int toId) {

		Transfer transfer = new Transfer();
		transfer.setAmount(tAmount);
		transfer.setFromId(fromId);
		transfer.setToId(toId);
		try {
		restTemplate.exchange(BASE_URL + "/transfer", HttpMethod.POST, makeAuthEntityWithBody(transfer), Transfer.class)
				.getBody();

		}catch (RestClientException e) {
			System.out.println("Not enough balance");
		}

	}
	
	public void aprovedRequest (int transId) {
	
		Transfer transfer = new Transfer();
		try {
		restTemplate.exchange(BASE_URL + "/approvedrequest/" + transId, HttpMethod.POST, makeAuthEntityWithBody(transfer), Transfer.class);
		}catch (RestClientException e) {
			System.out.println("Not a valid Pending ID");
		}
		
	}
	
	public void requestreject (int Id) {
		Transfer transfer = new Transfer();
		restTemplate.exchange(BASE_URL + "/requestreject/" + Id, HttpMethod.POST, makeAuthEntityWithBody(transfer), Transfer.class)
		.getBody();
		
	}
	
	public void request (double rAmount, int fromId, int requestId) {
		Transfer transfer = new Transfer();
		transfer.setAmount(rAmount);
		transfer.setFromId(fromId);
		transfer.setToId(requestId);

		restTemplate.exchange(BASE_URL + "/request", HttpMethod.POST, makeAuthEntityWithBody(transfer), Transfer.class)
				.getBody();
		
	}
	
	

	public User[] allbutUser() {

		User[] user = restTemplate
				.exchange(BASE_URL + "/findallbutuser", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();

		return user;
	}

	public ToFromTransfer[] toFromTransfer() {

		ToFromTransfer[] tFTransfer = restTemplate
				.exchange(BASE_URL + "/fromTransfer", HttpMethod.GET, makeAuthEntity(), ToFromTransfer[].class).getBody();

		return tFTransfer;
	}
	
	public ToFromTransfer[] pendingTransfer () {
		
		ToFromTransfer[] pTransfer = restTemplate
				.exchange(BASE_URL + "/pendingtransfer", HttpMethod.GET, makeAuthEntity(), ToFromTransfer[].class).getBody();  
		
		return pTransfer;
	}
	
	
	public  TransferDetails[] transdetails (int id)  {
		//int id = transferDetails.getId();
		
		TransferDetails []   tDetails = restTemplate.exchange(BASE_URL + "/transdetails/" + id ,HttpMethod.GET, makeAuthEntity(), TransferDetails[].class).getBody();
	
	
		return tDetails;
	}
	
	
	private HttpEntity makeAuthEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(headers);
		return entity;
	}

	private HttpEntity makeAuthEntityWithBody(Transfer transfer) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(AUTH_TOKEN);
		HttpEntity entity = new HttpEntity<>(transfer, headers);
		return entity;
	}

}
 