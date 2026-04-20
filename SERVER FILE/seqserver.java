//
// The following shows the general form of a server that
// processes requests from different clients one-at-a-time.
// That is, it listens for a request from a client, processes
// that request, then looks for another client, and another,
// and another, etc.
//
// Note that the loop is an infinite loop ("while(true)"),
// so the program must be terminated manually (using Linux's
// CTRL-c command, for example).
//
// "..." below stands for omitted processing statements that
// would be resolved with specific processing statements,
// depending on the desired server behavior.
//

import java.util.*;
import java.net.*;
import java.io.*;
import java.math.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class seqserver
{
    public static final int DECIMAL_SCALE = 8;

    public static void main(String[] args)
    {

        ServerSocket serverSocket = null;
        Socket socket = null;
        int port;
        boolean listening = true; // assume serverSocket creation
        // was OK

        // get port # from command-line

        port = Integer.parseInt(args[0]);

        // try to create a server socket

        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch(IOException e)
        {
            System.out.println(e);
            listening = false;
        }

        if(listening) // i.e., serverSocket successfully created
        {
            // continue to:
            //
            //   (1) Listen for a client request
            //   (2) Read data from the client
            //   (3) Process the request: do calculation and return value
            //

            while(true) // main processing loop
            {
                try
                {

                    // Listen for a connection request from a client

                    socket = serverSocket.accept();

                    // Establish the input and output streams on the socket

                    PrintWriter out = new
                            PrintWriter(socket.getOutputStream(), true);
                    Scanner in = new Scanner(new
                            InputStreamReader(socket.getInputStream()));

                    // Read data from the client, do calculation(s),
                    // return data value(s)

                    String fullInput = in.nextLine();
                    String[] splitInput = fullInput.split(",");
                    String operation = splitInput[0];
                    String firstNum = splitInput[1];
                    String secondNum = splitInput[2];

                    if(!(operation.equals("add") || operation.equals("sub") || operation.equals("mult") || operation.equals("shuffle"))){
                        out.println("Unknown Operation");
                    }
                    else if(!isBigDecimalNumber(firstNum) && isBigDecimalNumber(secondNum)){
                        out.println("First Value: not a number");
                    }
                    else if(!isBigDecimalNumber(firstNum) && isBigDecimalNumber(secondNum)){
                        out.println("Second Value: not a number");
                    }
                    else if(!isBigDecimalNumber(firstNum) && isBigDecimalNumber(secondNum)){
                        out.println("Both Values: not numbers");
                    }
                    else{
                        BigDecimal num1 = new BigDecimal(firstNum);
                        BigDecimal num2 = new BigDecimal(secondNum);

                        switch(operation){
                            case "add":
                                out.println(num1.add(num2).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP).toPlainString());
                                break;
                            case "sub":
                                out.println(num1.subtract(num2).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP).toPlainString());
                                break;
                            case "mult":
                                out.println(num1.multiply(num2).setScale(DECIMAL_SCALE, RoundingMode.HALF_UP).toPlainString());
                                break;
                            case "shuffle":
                                out.println(ShuffleDecimals(num1.toPlainString().replace(".", ""), num2.toPlainString().replace(".", "")));
                                break;
                        }
                    }

                    // close connection to client

                    out.close();
                    in.close();
                    socket.close();

                }
                catch(IOException e)
                {
                    System.out.println(e);
                }

            } // end while (main processing loop)

        } // end if listening

    } // end main

    public static boolean isBigDecimalNumber(String num){
        String regex = "[^0-9.]";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(num);

        //note: the regex match returns TRUE if the input string IS NOT a decimal, as it is trying to find characters which aren't numbers.
        return !match.matches();
    }

    public static String ShuffleDecimals(String num1, String num2){
        String result = "";
        int ind = 0;

        while(ind < num1.length() || ind < num2.length()){
            if(ind < num1.length()){
                result += num1.charAt(ind);
            }
            if(ind < num2.length()){
                result += num2.charAt(ind);
            }
            ind += 1;
        }

        return result;
    }

} // end seqserver

