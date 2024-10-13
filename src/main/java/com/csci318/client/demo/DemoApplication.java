package com.csci318.client.demo;

import com.csci318.client.demo.Functionality.EventDemonstrator;
import com.csci318.client.demo.Functionality.UseCaseDemonstrator;

public class DemoApplication {
	public static void main(String[] args) {
		try {
			if (args.length >= 1 && args[0].equals("--use-cases")) {
				new UseCaseDemonstrator().run();
			} else {
				new EventDemonstrator().run();
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
	}
}
