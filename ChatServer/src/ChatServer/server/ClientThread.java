package ChatServer.server;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ClientThread implements Runnable {
  private Socket socket;
  private PrintWriter clientOut;
  private String userName;
  private ChatServer server;
  TSPNearestNeighbour tspNearestNeighbour = new TSPNearestNeighbour();
  private int matrix[][] = {{0,10,15,20},{10,0,35,25},{15,35,0,30},{20,25,30,0}};
  private int[] answerArr;


  public ClientThread(ChatServer server, Socket socket){
    this.server = server;
    this.socket = socket;
  }

  private PrintWriter getWriter(){
    return clientOut;
  }

  @Override
  public void run() {
    try{
      // setup
      this.clientOut = new PrintWriter(socket.getOutputStream(), false);
      Scanner in = new Scanner(socket.getInputStream());

      // start communicating
      userName = in.nextLine();
      System.out.println(userName);
//      for (int i=0;i<4;i++){
//        for (int j=0;j<4;j++){
//          System.out.println(matrix[i][j]);
//        }
//      }
      server.addUserNameMap(userName);
      server.addClient(userName,this);
      while(!socket.isClosed()){
        if(in.hasNextLine()){
          String input = in.nextLine();
          // NOTE: if you want to check server can read input, uncomment next line and check server file console.
          // System.out.println(input);
          if (input.equalsIgnoreCase("PV")){
            String to = in.nextLine();
            String text = in.nextLine();
            sendTextToOther(to,text);
            continue;
          }else if (input.equalsIgnoreCase("TSP")){
//            server.findKey(userName);
            answerArr = tspNearestNeighbour.main(matrix);
//            for (int i=0;i<answerArr.length;i++){
//              System.out.println(answerArr[i]);
//            }
//            server.findValue(0);
            String to = in.nextLine();
            String text = in.nextLine();
            int reciever = server.findKey(to);
            int sender = server.findKey(userName);
            tspSend(sender,reciever,answerArr,text);
          }
          else {
            for(ClientThread thatClient : server.getClients()){
              if (thatClient.socket.getRemoteSocketAddress().toString().equals(socket.getRemoteSocketAddress().toString())){
                continue;
              }
              PrintWriter thatClientOut = thatClient.getWriter();
              if(thatClientOut != null){
                thatClientOut.write(userName + " > " + input + "\r\n");
                thatClientOut.flush();
              }
              continue;
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  private void sendTextToOther(String to,String text){
    ClientThread otherClient = server.findClient(to);
    if (otherClient == null){
      return;
    }
    otherClient.sendText(userName , text);
  }
  private void sendText(String from,String text){
    clientOut.println("Private Massage : " + from + " > " + text);
    clientOut.flush();
  }
  private void tspSend(int sender,int reciever,int[]answerArr,String text){
    int from=-1;
    int to=-1;
    for (int i=0;i<answerArr.length;i++){
      if (answerArr[i]==sender){
        from = i;
      }if (answerArr[i]==reciever){
        to = i;
      }
    }
    if (from < to){
      for (int i=from;i<=to;i++){
        sendTextToOther(server.findValue(answerArr[i]),text);
      }
    }else{
      for (int i=from;i<answerArr.length;i++){
        sendTextToOther(server.findValue(answerArr[i]),text);
      }
      for (int i=0;i<=to;i++){
        sendTextToOther(server.findValue(answerArr[i]),text);
      }
    }
  }
}