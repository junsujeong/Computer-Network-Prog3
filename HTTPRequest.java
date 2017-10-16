/**
HTTPRequest class which serves the request from web.
Basically, in this program HTTP thread reads the request message
from the socket which were from web and write the requested file back 
to socket with the response message. This class make and use 
instance of HTTP class to handle the request message.
*/

package jeongj_prog3;

import java.io.*;
import java.net.*;


public class HTTPRequest extends Thread
{
   private Socket socket;
   private BufferedReader inFromClient;
   private OutputStream writeToSocket;
   
   /**
   Constructor of HTTPRequestclass that initialize the socket,
   bufferedReader that reads request message from socket
   and OutputStream which write the response message and 
   requested file into socket.
   @param sock socket which was created in the webServer class
   */
   public HTTPRequest(Socket sock)
   {
      try
      {
         socket = sock;
         inFromClient = new BufferedReader(new InputStreamReader
                                          (socket.getInputStream())); 
         writeToSocket = socket.getOutputStream();
      }
      catch( Exception ex )
      {
         System.out.println( "Error: " + ex ); 
      }
   }

   /**
   run method which reads a request message from the web and 
   calls few methods of HTTP class to process the request.
   Once the response message is ready then, 
   it writes the response message back to socket. And close it.
   */      
   public void run()
   {
      try
      {
         String inLine = inFromClient.readLine();
         HTTP http = new HTTP(inLine);
         http.parse();
         http.bringFile();
         writeToSocket.write(http.Status().getBytes());
         writeToSocket.write(http.Content().getBytes());
         http.SendFile(writeToSocket);
         socket.close();
      }
      catch( Exception ex )
      {
         System.out.println( "Error: " + ex ); 
      }
   }
}
