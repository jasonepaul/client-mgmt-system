package server.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.ServerSocket;

import server.model.CMSModel;

/**
 * Serves multiple clients to the CMS through multi-threading.
 * @author Jason Paul
 * @version 1.0
 * @since feb 13, 2020
 */
public class ServerController {

	private ServerSocket serverSocket;
	private ExecutorService pool;
	private Socket socket;
	private ObjectInputStream objectIn = null;
	private ObjectOutputStream objectOut = null;
	private ModelController modelCon;
	
	/**
	 * Constructs a server controller, instantiating a server socket and thread
	 * pool.
	 */
	public ServerController() {
		try {
			serverSocket = new ServerSocket(9090);
			pool = Executors.newFixedThreadPool(3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * accepts connections to clients.
	 */
	public void runServer() {

		try {
			while(true) {
				socket = serverSocket.accept();
				System.out.println("Connection accepted by server!");
				objectOut = new ObjectOutputStream(socket.getOutputStream());
				objectIn = new ObjectInputStream(socket.getInputStream());
				CMSModel aModel = new CMSModel();
				modelCon = new ModelController (objectIn, objectOut, aModel);
				pool.execute(modelCon);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			objectIn.close();
			objectOut.close();
			socket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void main(String[] args) throws IOException {
		ServerController myServer = new ServerController();
		myServer.runServer();
	}

}
