/**
HTTP class is simplified version of HTTP which handles the GET
request message only. Once it gets the message from the HTTPRequest
class then, it parse the message to extract the file name and compose
response message with the status line, content line also
it writes the body of the message 
@author jeongj
*/
package jeongj_prog3;

import java.io.*;
import java.util.*;

public class HTTP 
{
   private String requestMessage;
   private StringTokenizer stringToken;
   private String method;
   private String URL;
   private String fileName;
   private String contentTypeLine;
   private boolean fileExist = true;
   public FileInputStream sendFile;
   private final int CHUNK_SIZE = 1024;
   private final String CRLF = "\r\n";
   private byte[] buffer;
   
   /**
   The constructor of HTTP class initialize requestMessage,
   the size of buffer, and add the header file name of the 
   contentTypeLine.
   @param reqMsg request message from socket
   */
   public HTTP(String reqMsg)
   {
      requestMessage = reqMsg;
      contentTypeLine = "content-type: ";
      buffer = new byte[CHUNK_SIZE];
   }
   
   /**
   The method parse() uses StringTokenizer to parse the 
   request message from the web. The main purpose of this
   method it to extract the filename from URL.
   */
   public void parse()
   {
      try
      {
         stringToken = new StringTokenizer(requestMessage, " \r\n");
         stringToken.nextToken(); // passes mehtod
         URL = stringToken.nextToken();
         stringToken = new StringTokenizer(URL);
         fileName = stringToken.nextToken();
      }
      catch( Exception ex )
      {
         System.out.println( "Error: " + ex ); 
      }
   }

   /**
   The method bringFile() is the first step of sending the file 
   to the web. It does initialize the FileInputStream with the 
   file that was requested from the web. if the file does not
   exist it will throw FileNotFounException then sets the 
   fileExist to false.
   */
   public void bringFile()
   {
      try
      {
         sendFile = new FileInputStream("J:\\cs3830\\jeongj_prog3" 
                                        + fileName);
      }
      catch (FileNotFoundException ex)
      {
         fileExist = false;          
      }
   }
   
   /**
   The method Status compose the status line of the response message
   according to the existence of the file determined by the fileExist
   variable. If the file does not exist then it returns '404 Not Found '
   message with an entity body, otherwise it returns "202 OK" Message with
   CRLF. 
   */   
   public String Status()
   {
      try
      {       
         String stat = "HTTP/1.0 ";
         if(fileExist)
            stat += "200 OK " + CRLF;
         else
         {
            stat += "404 Not Found " + CRLF +
                    "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>" +
                    "<BODY>Not Found</BODY></HTML>"; 
         }
         return stat;
      }
      catch (Exception ex)
      {
         System.out.println( "Error: " + ex );
         return null;
      }
   }
   
   /**
   The method Content add the content type of the file by
   calling contentType(String a), adds it into String content.
   the returns the String content  
   */
   public String Content()
   {
      try
      {
         String content;
         content = contentType(fileName);   
         return content; 
      }
      catch (Exception ex)
      {
         System.out.println( "Error: " + ex );
         return null;
      }
   }
   
   /**
   The method ContentType determines which MIME type the file is 
   by comparing the file name extentions. And add the MIME to
   contentTypeLine. If the file name is unknown, it returns
   application/octet-stream.
   */  
   private String contentType(String fileName)
   {
      if(fileName.endsWith(".htm") || fileName.endsWith(".html"))
         return contentTypeLine += "text/html \r\n\r\n";
      if(fileName.endsWith(".bmp"))
         return contentTypeLine += "image/bmp \r\n\r\n";
      if(fileName.endsWith(".gif"))
         return contentTypeLine += "image/gif \r\n\r\n";
      if(fileName.endsWith(".jpe") || fileName.endsWith(".jpeg") 
         || fileName.endsWith(".jpg"))
         return contentTypeLine += "image/jpeg \r\n\r\n";
      return "application/octet-stream \r\n\r\n";
   }
   
   /**
   The method SendFile(OutputStream os) is the method that writes
   the requested file to socket's output stream. The file is with 
   1024-byte. I have a while loop that use read method of FileInputStream
   until it returns -1 which indicate an end of file
   @param os OutputStream from HTTPRequest class
   */
   public void SendFile(OutputStream os) throws IOException
   {
      try
      {
         if (fileExist)
         {
            int size;
            while ((size = sendFile.read(buffer)) != -1)
               os.write(buffer, 0, size);
         }
      }
      catch (Exception ex)
      {
         System.out.println( "Error: " + ex );    
      }
   }
}
