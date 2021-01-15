package com.techelevator.tenmo;

import java.util.Scanner;


import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Balance;
import com.techelevator.tenmo.models.ToFromTransfer;
import com.techelevator.tenmo.models.TransferDetails;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;

public class App {

	private static final String API_BASE_URL = "http://localhost:8080/";

	private static final String MENU_OPTION_EXIT = "Exit";
	private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN,
			MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS,
			MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS,
			MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };

	private AuthenticatedUser currentUser;
	private ConsoleService console;
	private AuthenticationService authenticationService;
	private TransferService transferService = new TransferService("http://localhost:8080");
	Scanner scanner = new Scanner(System.in);
	TransferDetails transferDetails	;
	ToFromTransfer toFromTransfer;

	public static void main(String[] args) {
		App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
		app.run();
	}

	public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;

	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");

		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while (true) {
			String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		Balance balance = transferService.getBalanceFromServer();
		System.out.println("-------------------------------------------------------- ");
		System.out.println("Your current account balance is: $" + balance.getBalance());
		System.out.println("-------------------------------------------------------- ");
	}

	private void viewTransferHistory() {

		ToFromTransfer[] tFTransferList = transferService.toFromTransfer();
		
		System.out.println("-------------------------------------------------------- ");
		System.out.println("Transfer" + "   " + "From/To" + "   " + "Amount");
		System.out.println("Id");
		System.out.println("-------------------------------------------------------- ");
		for (ToFromTransfer tft : tFTransferList) {
			System.out.println(tft.getTransfer_id() + "          " + tft.getToFromUserName() 
							  + "       " + tft.getAmount());
			
		}
		
		System.out.println("-------------------------------------------------------- ");
		
		System.out.println("Please enter transfer ID to view details");
		
		int id = scanner.nextInt();
	
		
		TransferDetails[] transDetails = transferService.transdetails(id);
		
		for (TransferDetails tf : transDetails ) {
			System.out.println("-------------------------------------------------------- ");
			System.out.println("Transfer Details");
			System.out.println("-------------------------------------------------------- ");
			System.out.println("id: " + tf.getId());
			System.out.println("From: " + tf.getFrom());
			System.out.println("To: " + tf.getTo());
			System.out.println("Type: " + tf.getType());
			System.out.println("Status: " + tf.getStatus() );
			System.out.println("Amount: " + tf.getAmount());
			System.out.println("-------------------------------------------------------- ");
		}

	}
		
	

	private void viewPendingRequests() {

		ToFromTransfer[] pendingTransferList = transferService.pendingTransfer();
		
		System.out.println("-------------------------------------------------------- ");
		System.out.println("Pending" + "     " + "Transfers T0" + "   " + "Amount");
		System.out.println("Id");
		System.out.println("-------------------------------------------------------- ");
		
		for (ToFromTransfer pft : pendingTransferList) {
			System.out.println(pft.getTransfer_id() + "          " + pft.getToFromUserName() 
							  + "       " + pft.getAmount());
		}
		System.out.println("-------------------------------------------------------- ");
		System.out.println("Please enter transfer ID to approve/reject (0 to cancel):");
		System.out.println("-------------------------------------------------------- ");
		
		int transId = scanner.nextInt();
		
		if (transId == 0) {
			System.out.println("You didn't add any id");
			mainMenu();
		}else {
		System.out.println("-------------------------------------------------------- ");
		System.out.println("1: Approve");
		System.out.println("2: Reject");
		System.out.println("0: Don't approve or reject");
		System.out.println("-------------------------------------------------------- ");
		System.out.println("Please choose an option:");
		
		int decision = scanner.nextInt();
		
		if (decision == 1) {
			transferService.aprovedRequest(transId);
			System.out.println("Request Approved");
		}
		else if (decision == 2) {
			
			transferService.requestreject(transId);
			System.out.println("Request rejected");
		}
		
		
		else if (decision == 0) {
			System.out.println("You didn't gave any decision");
			mainMenu();
		}
		}
	}

	private void sendBucks() {

		User[] usersList = transferService.allbutUser();

		for (User user : usersList) {
			System.out.println(user.getId() + " " + user.getUsername());
		}

		System.out.println("Enter ID of user you are sending to(0 to cancle): ");
		
		int inputId = scanner.nextInt();
		
		if (inputId != 0 ) {
			
		System.out.println("Enter amount: ");

		Double amount = scanner.nextDouble();
		
		

		transferService.transfer(amount, currentUser.getUser().getId(), inputId);
		}
		else {System.out.println("Your didnt send any money");
			
			 mainMenu();
		}
	}
	


	private void requestBucks() {
		User[] usersList = transferService.allbutUser();

		for (User user : usersList) {
			System.out.println(user.getId() + " " + user.getUsername());
		}

		System.out.println("Enter ID of user you are requesting from(0 to cancle): ");
		
		int fromId = scanner.nextInt();
		
		System.out.println("Enter amount: ");

		Double rAmount = scanner.nextDouble();
		
		transferService.request (rAmount, currentUser.getUser().getId() , fromId );
		
		
	}

	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while (!isAuthenticated()) {
			String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
		while (!isRegistered) // will keep looping until user is registered
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				authenticationService.register(credentials);
				isRegistered = true;
				System.out.println("Registration successful. You can now login.");
			} catch (AuthenticationServiceException e) {
				System.out.println("REGISTRATION ERROR: " + e.getMessage());
				System.out.println("Please attempt to register again.");
			}
		}
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) // will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
			try {
				currentUser = authenticationService.login(credentials);
				String token = currentUser.getToken();
//				System.out.println("The token I got back from server is: " + token);
				TransferService.AUTH_TOKEN = token;

			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: " + e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}

	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
