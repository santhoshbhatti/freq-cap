package com.piedpiper.adengine.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Campaign implements Serializable{
	String name;
	int cap;
	int frequency;
	
	List<LineItem> lineItems; 
}
