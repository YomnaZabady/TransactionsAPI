package com.transaction.api;

import java.time.Instant;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StatisticsResult {
	
	public Double sum;
	public  Double max;
	public  Double min ;
	public  long count ;
	public Double average = ((sum == null) ? 0:sum)/((count == 0) ? 1 : count);
	public long time = Instant.now().toEpochMilli();
	
	public StatisticsResult() {}

}
