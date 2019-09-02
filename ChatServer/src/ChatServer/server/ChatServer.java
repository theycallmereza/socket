package ChatServer.server;

import ChatServer.server.ClientThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ChatServer {

  private static final int portNumber = 4444;

  private int serverPort;
  private List<ClientThread> clients; // or "protected static List<ClientThread> clients;"
  HashMap<String,ClientThread> clientsMap = new HashMap<String, ClientThread>();
  HashMap<Integer,String> userNameMap = new HashMap<Integer, String>();
  private int u = 0;


  public static void main(String[] args){

    ChatServer server = new ChatServer(portNumber);
    server.startServer();
  }

  public ChatServer(int portNumber){
    this.serverPort = portNumber;
  }

  public List<ClientThread> getClients(){
    return clients;
  }

  private void startServer(){
    clients = new ArrayList<ClientThread>();
    ServerSocket serverSocket = null;

    try {
      serverSocket = new ServerSocket(serverPort);
      acceptClients(serverSocket);
    } catch (IOException e){
      System.err.println("Could not listen on port: "+serverPort);
      System.exit(1);
    }
  }

  private void acceptClients(ServerSocket serverSocket){

    System.out.println("server starts port = " + serverSocket.getLocalSocketAddress());
    while(true){
      try{
        Socket socket = serverSocket.accept();
        System.out.println("accepts : " + socket.getRemoteSocketAddress());
        ClientThread client = new ClientThread(this, socket);
        Thread thread = new Thread(client);
        thread.start();
        clients.add(client);
      } catch (IOException ex){
        System.out.println("Accept failed on : "+serverPort);
      }
    }
  }
  public void addClient (String clientName,ClientThread clientThread){
    clientsMap.put(clientName,clientThread);
  }
  public ClientThread findClient(String clientUserName){
    return clientsMap.get(clientUserName);
  }
  public void addUserNameMap (String userName){
    userNameMap.put(u,userName);
    this.u ++;
  }
  public int findKey(String userName){
    Set set=userNameMap.entrySet();
    Iterator iterator = set.iterator();
    while (iterator.hasNext()){
      Map.Entry mentry = (Map.Entry) iterator.next();
      if (mentry.getValue().equals(userName)){
//        System.out.println(mentry.getKey());
        return (Integer)mentry.getKey();
      }
    }
    return -1;
  }
  public String findValue(int key){
    Set set=userNameMap.entrySet();
    Iterator iterator = set.iterator();
    while (iterator.hasNext()){
      Map.Entry mentry = (Map.Entry) iterator.next();
      if ((Integer) mentry.getKey() == (key)){
//        System.out.println(mentry.getValue());
        return (String)mentry.getValue();
      }
    }
    return "";
  }
}