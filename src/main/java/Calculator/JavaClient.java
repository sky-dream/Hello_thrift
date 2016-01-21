/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

// Generated code

package Calculator;import tutorial.*;
import shared.*;

import java.util.Scanner;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TSSLTransportFactory;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TSSLTransportFactory.TSSLTransportParameters;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

public class JavaClient {
	public static void main(String [] args) throws InterruptedException {
		String trans_method=new String("");
		System.out.println("Please enter 'simple' or 'secure'");
		Scanner input=new Scanner(System.in);

		trans_method=input.nextLine();
		   int i=0;
           while(true){
		try {
			TTransport transport;
			if (trans_method.contains("simple")) {
				transport = new TSocket("localhost", 9090);

				transport.open();
				System.out.println(" simple transport  open");
			}
			else   {

				TSSLTransportParameters params = new TSSLTransportParameters();
				params.setTrustStore("lib/java/test/.truststore", "thrift", "SunX509", "JKS");

				/*  Get a client transport instead of a server transport. The connection is opened on
				     invocation of the factory method, no need to specifically call open()*/

				transport = TSSLTransportFactory.getClientSocket("localhost", 9091, 0, params);
				System.out.println(" secure  transport  open");
			}


			TProtocol protocol = new  TBinaryProtocol(transport);
			Calculator.Client client = new Calculator.Client(protocol);
			
            	perform(client);
            	Thread.sleep(10000);
            	
            	    i++;
            	System.out.println("called : "+i+"  times");
            	System.out.println("perform(client)");
 			transport.close();
			System.out.println("transport.close()");
		} catch (TException x) {
			x.printStackTrace();
		} 
	}
    }
	private static void perform(Calculator.Client client) throws TException
	{
		client.ping();
		System.out.println("ping()");

		int sum = client.add(1,1);
		System.out.println("1+1=" + sum);

		Work work = new Work();

		work.op = Operation.DIVIDE;
		work.num1 = 1;
		work.num2 = 0;
		try {
			int quotient = client.calculate(1, work);
			System.out.println("Whoa we can divide by 0");
		} catch (InvalidOperation io) {
			System.out.println("Invalid operation: " + io.why);
		}

		work.op = Operation.SUBTRACT;
		work.num1 = 15;
		work.num2 = 10;
		try {
			int diff = client.calculate(1, work);
			System.out.println("15-10=" + diff);
		} catch (InvalidOperation io) {
			System.out.println("Invalid operation: " + io.why);
		}

		SharedStruct log = client.getStruct(1);
		System.out.println("Check log: " + log.value);
	}
}
