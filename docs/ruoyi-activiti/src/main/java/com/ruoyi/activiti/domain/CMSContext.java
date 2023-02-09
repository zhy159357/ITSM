package com.ruoyi.activiti.domain;

import java.util.Enumeration;
import java.util.Hashtable;

public class CMSContext {
	private static Integer id = 1;
	private Hashtable hashCommand=new Hashtable();
	
	/**
	 * 获得下一个上下文ID
	 * @return
	 */
	public static int nextContextId(){
		int n=0;
		synchronized(id){
			++id;
			n=id.intValue();
		}
		return n;
	}
	
	public static int currContextId(){
		return id.intValue();
	}
	
	public void addCommand(int nContextId,Object obj){
		//Integer nId = Integer.valueOf(nContextId);
		hashCommand.put(Integer.valueOf(nContextId) , obj);
	}
	public Object getCommand(int nContextId){
		Integer nKey = Integer.valueOf(nContextId);
		Object obj =hashCommand.get(nKey);
		return obj;
	}
	public Object removeCommand(int nContextId){
		Integer nKey = Integer.valueOf(nContextId);
		Object obj =hashCommand.remove(nKey);
		return obj;
	}
	public void deleteCommand(int nContextId){
		Integer nKey = Integer.valueOf(nContextId);
		Object obj =hashCommand.remove(nKey);
		obj=null;
	}
	private void printHash(){
		Enumeration en = hashCommand.keys();
		while(en.hasMoreElements()){
			Integer key = (Integer)en.nextElement();
			String sValue = (String)hashCommand.get(key);
			System.out.println(key+":"+sValue );
		}
	}
	
	public static void main(String[] agv){
		CMSContext ctxt = new CMSContext();
		ctxt.addCommand(1, "aa1");
		ctxt.addCommand(2, "aa2");
		ctxt.addCommand(3, "aa3");
		String s2 = (String)ctxt.getCommand(2);
		System.out.println("s2="+s2);
		String s3 = (String)ctxt.removeCommand(3);
		System.out.println("s3="+s3);
		ctxt.printHash();
	}
}
