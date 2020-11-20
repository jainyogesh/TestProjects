package org.jainy.personal;

import java.io.*;
import java.util.Arrays;
public class CandidateCode2 
{ 
    public static String criticalBridges(int input1,String input2)
    {
        StringBuilder result = new StringBuilder();
        result.append('{');
        
        String[] testCases = input2.split("\\}\\),");
        
        for(int i=0; i < testCases.length;i++){
        	if(i>0){
        		result.append(',');
        	}
        	String testCase = testCases[i];
        	String[] testCaseData = testCase.split("\\},\\{");
        	String[] icelandNames = testCaseData[0].substring(2).split(",");
        	Iceland[] icelands = new Iceland[icelandNames.length];
        	for(int ic=0; ic < icelands.length; ic++){
        		icelands[ic] = new Iceland(icelandNames[ic]);
        	}
        	String[] bridges = i==testCases.length-1 ? testCaseData[1].substring(0, testCaseData[1].length()-2).split("\\),") : testCaseData[1].split("\\),");
        	StringBuilder allBridges = new StringBuilder();
        	for(int j=0; j < bridges.length; j++){
        		String bridge = j==bridges.length-1 ? bridges[j].substring(1, bridges[j].length()-1) : bridges[j].substring(1);
        		String connectedIcelands[] = bridge.split(",");
        		if(connectedIcelands.length ==2){
        			Bridge b = new Bridge(connectedIcelands[0], connectedIcelands[1]);
        			if(allBridges.length() > 0){
        				allBridges.append(',');
        			}
        			allBridges.append(b);
        			int match = 0;
        			for(int ic=0; ic < icelands.length; ic++){
        				if(b.connects(icelands[ic].getName())){
        					icelands[ic].addBridge(b);
        					match++;
        				}
        				if(match ==2)
        					break;
        			}
        		}
        		
        	}
        	
        	int criticalBridgeCount= 0;
        	for(int ic=0; ic < icelands.length; ic++){
        		if(icelands[ic].getBrigeCount() == 0){
        			criticalBridgeCount = bridges.length;
        			result.append('{').append(allBridges);
        			break;
        		}
        		
        		if(icelands[ic].getBrigeCount() == 1){
        			if (criticalBridgeCount ==0){
        				result.append('{');
        			}else{
        				result.append(',');
        			}
        			result.append(icelands[ic].getBridges());
        			criticalBridgeCount++;
        		}
        	}
        	
        	if(criticalBridgeCount ==0){
        		result.append("{NA");
        	}
        	result.append('}');
        	System.out.println(Arrays.toString(icelandNames));
        	System.out.println(Arrays.toString(bridges));
        	
        	
        }
        return result.append('}').toString();
    }
    
    
    private static class Iceland{
    	
    	String icelandName;
    	StringBuilder bridges = new StringBuilder();
    	int bridgeCount;
    	
    	private Iceland(String icelandName){
    		this.icelandName=icelandName;
    	}
    	
    	private void addBridge(Bridge bridge){
    		if(bridgeCount > 0){
    			bridges.append(',');
    		}
    		bridges.append(bridge);
    		bridgeCount++;
    	}
    	
    	private String getName(){
    		return this.icelandName;
    	}
    	private int getBrigeCount(){
    		return bridgeCount;
    	}
    	
    	private String getBridges(){
    		return bridges.toString();
    	}
    }
    
    private static class Bridge{
    	String iceland1;
    	String iceland2;
    	
    	private Bridge(String iceland1, String iceland2){
    		this.iceland1=iceland1;
    		this.iceland2=iceland2;
    	}
    	
    	private boolean connects(String iceland){
    		return iceland.equals(iceland1) || iceland.equals(iceland2);
    	}
    	
    	public String toString(){
    		return new StringBuilder().append('(').append(iceland1).append(',').append(iceland2).append(')').toString();
    	}
    	
    }
}