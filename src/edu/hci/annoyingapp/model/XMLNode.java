package edu.hci.annoyingapp.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class XMLNode {
	
	private String mName;
	
	private Map<String, String> mAttributes;

	private List<XMLNode> mChildren;
	
	public XMLNode(String name) {
		mName = name;
		mAttributes = new TreeMap<String, String>();
		mChildren = new LinkedList<XMLNode>();
	}
	
	public void addChild(XMLNode child) {
		mChildren.add(child);
	}
	
	public void addAttribute(String key, String value) {
		mAttributes.put(key, value);
	}
	
	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		
		res.append('<');
		res.append(mName);
		
		for(Map.Entry<String, String> e : mAttributes.entrySet()) {
			res.append(' ');
			res.append(e.getKey());
			res.append('=');
			res.append(e.getValue());
		}
		
		res.append('>');
		
		for(XMLNode node : mChildren) {
			res.append(node.toString());
		}
		
		res.append("</");
		res.append(mName);
		res.append('>');
		
		return res.toString();
	}
	
}
