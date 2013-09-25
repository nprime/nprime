package com.phoenixmanager.PageServlets;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.phoenixmanager.Data.*;


@WebServlet(name="CalcPrimeServlet", urlPatterns="/prime")
public class CalcPrimeServlet extends HttpServlet {

	private static final long serialVersionUID = 6475041456628458743L;
	private static final String EMPTY = "";
	private static final String title = "Calculate Prime";

	private int n=100;
	private long primenum=0;

	/**
	 * Get the page, calls the page to be made
	 * We used to check the parameters in here, but I moved it to a controller object to keep the logic away from the layout
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		makePage(request, response, Boolean.FALSE);
	}

	/**
	 * Get the page, we can just pass this to doPost since the client generator will be posting userIDs and authTokens all the time
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		makePage(request, response, Boolean.TRUE);
	}

	/**
	 * 
	 * @param request The incoming user request
	 * @param response  The out going user response
	 * @throws ServletException
	 * @throws IOException
	 * @throws JSONException 
	 */
	public void makePage(HttpServletRequest request, HttpServletResponse response, Boolean isGet)  throws ServletException, IOException {
		if(request !=null && response != null){
			response.setHeader("Access-Control-Allow-Origin", "*");


			try {
				databaseProcess(request);
			} catch (JSONException e) {

				e.printStackTrace();
			}

			// We are using PrintWriter to be friendly to the international community. PrintWrite auto-converts coding
			PrintWriter out = response.getWriter();

			// Set the return type
			response.setContentType("text/html");


			createResponse(out, isGet);

			// Redirect the output to start writing the the user again in case we were putting it in the black hole
			out = response.getWriter();

		}
	}

	public void databaseProcess(HttpServletRequest request) throws JSONException{
		String s=request.getParameter("n");
		n=Integer.parseInt(s);
		ArrayList<Long> primes=new ArrayList<Long>();
		primes.add((long) 2);
		primes.add((long) 3);

		long start=5;
		while(primes.size()<n){
			boolean p=isPrime(start,primes);
			if (p==true){
				primes.add(start);
			}

			start+=2;
		}
		primenum=primes.get(primes.size()-1);

	}

	private boolean isPrime(long num,ArrayList<Long> primes){
		int i=-1;
		boolean p=true;
		do{
			i++;
			if(num%primes.get(i)==0){
				p=false;
				break;
			}

		}while(primes.get(i)<=Math.sqrt((double)num));

		return p;
	}

	public void createResponse(PrintWriter out, Boolean isGet){
		if(out != null)
			try{

				//out.write(Long.toString(primenum));
				out.write(Long.toString(primenum));

			}
		catch(Exception e){
			e.printStackTrace();
		}
	}



}
