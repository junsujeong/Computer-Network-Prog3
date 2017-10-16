/**
WebServer class designed to communicate with web. 
the server listen to socket for connection and once 
it receives the request, it will handle the task
and communicate with the web
@author jeongj
*/

package jeongj_prog3;

import java.net.*;


public class WebServer {
   /**
   Main method of Server that allows to run the server.
   @param args is unused
   */
   public static void main(String [] args)
   {
      try
      {
         WebServer server = new WebServer();
         server.run();
      }
      catch( Exception ex )
      {
         System.out.println( "Error: " + ex ); 
      }
   }
   
   /**
   Run method that create server socket with port number '5764'
   then accept the socket and hand it the HTTPR thread to handle 
   the request by creating and start instance of HTTPRequest class. 
   */
   public void run()
   {
      try
      {
         int portNum = 5764;
         
         ServerSocket servSock = new ServerSocket( portNum );
         while ( true )
         {
            Socket sock = servSock.accept();
            HTTPRequest servThread = new HTTPRequest( sock );
            servThread.start();
         }
      }
      catch( Exception ex )
      {
         System.out.println( "Error: " + ex ); 
      }
   }    
}
