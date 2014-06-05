package demo.controller;

import stone.json.JSONEntry;

public class DemoEntity implements JSONEntry {
	private String demo;
	
	private String demo1;
	
	public DemoEntity(){
		
	}

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
	}

	public String getDemo1() {
		return demo1;
	}

	public void setDemo1(String demo1) {
		this.demo1 = demo1;
	}
	
}
