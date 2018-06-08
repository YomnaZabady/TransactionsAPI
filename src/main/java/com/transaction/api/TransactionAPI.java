package com.transaction.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("api")
public class TransactionAPI {
	public int a[] = new int[10];
	public List<Integer> num;
	public static ArrayList<Transaction> transList = new ArrayList<Transaction>();
	public static Deque<Transaction> transactionDeque = new LinkedList<Transaction>();
	public static Double max = 0d;
	public static Double min = 999999999d;
	public static Double sum = 0d;
	public static long count = 0;
	public static ArrayList<Double> amountValues = new ArrayList<Double>();
	
	@GET
	@Path("/apiStats")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getResult() {
		updateDeque();
		StatisticsResult statResult = new StatisticsResult();
		statResult.sum = sum;
		statResult.count= count;
		statResult.min = min;
		statResult.max = max;
		return Response.ok(statResult).type(MediaType.APPLICATION_JSON_TYPE).build();
	}

	@Path("newTransaction")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response test(@FormParam("param1") double param1, @FormParam("param2") long param2) {
		long now = Instant.now().toEpochMilli(); //Long.valueOf("1528448206641"); 
//		1528448206641
//		1528448216314
		Transaction trans = new Transaction(param1, param2);
		System.out.println("***&&&&*****&&&&&***&&" + param1 + " " + param2);
		transactionDeque.addLast(trans);
		Double transAmount = param1;
		amountValues.add(transAmount);
		sum += transAmount;
		count +=1;
		if (transAmount > max) {
			max = transAmount;
		}
		if (transAmount < min) {
			min = transAmount;
		}

		updateDeque();
		long timeDiff = now - param2;
		if (timeDiff >  60000) {
			return Response.status(Response.Status.NO_CONTENT).build();
		} else {		
			//TODO check if it should be inserted in between the deque
			return Response.status(Response.Status.CREATED).build(); 
		}
		
	    
	}
	// function to update the deque for the last 60 seconds
	
	public void updateDeque() {
		if (transactionDeque.size() > 0) {
			long now = Instant.now().toEpochMilli();//Long.valueOf("1528448206641");
			Transaction earlyTrans = transactionDeque.getFirst();
			if (now - earlyTrans.time > 60000) {
				sum = sum - earlyTrans.amount;
				count--;
				transactionDeque.removeFirst();
				
				if (max == earlyTrans.amount) {
					amountValues.remove(amountValues.size()-1);
					if (!amountValues.isEmpty()) {
						Collections.sort(amountValues);
						max = amountValues.get(amountValues.size()-1);
					} else {
						max = 0d;
					}
				}
				if (min == earlyTrans.amount) {
					amountValues.remove(0);
					if (!amountValues.isEmpty()) {
						Collections.sort(amountValues);
						min = amountValues.get(0);
					} else {
						min = 0d;
					}
				}
			}
		}
	}
	
	
}

