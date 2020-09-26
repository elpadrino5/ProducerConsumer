package OSprj1;
//COP4610
//Programming Project 1
//Author: Angel Hierrezuelo Martinez
//n01419229
import java.util.Scanner;

class Producer implements Runnable
{
	private Integer numArray[];
	private boolean exception = false;
	int count = 0; 
	int sleeptime;
	
	public Producer(Integer numArray[], int prosleeptime) 
	{
		this.numArray = numArray;
		this.sleeptime = prosleeptime;
	}	
	public void run() 
	{
		osprj1 inclass = new osprj1();
		//System.out.println("Producer thread started.");
		//System.out.println("");
		 while (!exception && count <= 10)
		 {
			 try 
			 {		
				 Thread.sleep(sleeptime);	
				 double dblnum = Math.random() * (20 - 1);
			     int num = (int) dblnum;
			     exception = inclass.insert(numArray, num); 
			     count ++;
			     //System.out.println("");
			 }			 
			catch (InterruptedException e) 
			{
				  //System.out.println("Interrupted");
			}
			 //System.out.println("");
		}    	
		 //System.out.println("Producer thread exiting.");
	}
}
//--------//
class Consumer implements Runnable 
{
	private Integer numArray[];
	private boolean exception = false;
	int count = 0;
	int sleeptime;
	
	public  Consumer(Integer numArray[], int consleeptime) 
	{
		this.numArray = numArray;
		this.sleeptime = consleeptime;
	}
	public void run() 
	{
		 osprj1 rmclass = new osprj1();
		 //System.out.println("Consumer thread started.");
		 //System.out.println("");
		 while (!exception && count <= 10)
		 {
			 try 
			 {						 
				  Thread.sleep(sleeptime);
				  exception = rmclass.remove(numArray);  	
			      count++;
			      //System.out.println("");
			 }			 
			 catch (InterruptedException e) 
			 {
			     //System.out.println("Interrupted");
			 }
		 }
		 //System.out.println("");
		 //System.out.println("Consumer thread exiting");	
	}
}
//---//
public class osprj1
{
	private int i;

	public synchronized boolean insert(Integer numArray[], int num)
	{
		synchronized (numArray)
		{
			while (numArray[numArray.length - 1] != null)
			{	
				try 
				 {	
					 //System.out.println("Producer waiting because array is FULL.\n");
					 numArray.wait();
					 //System.out.println("Producer restarted after notify.\n");
				 }			 
				 catch (InterruptedException e) 
				 {
					Thread.currentThread().interrupt();
					//System.out.printf("Thread interrupted", e); 
				 }
			}
			try 
			{
			   	//System.out.println("");	
				for (i=0; i < numArray.length; i++)
				 {
			    	 if (numArray[i] == null)
			    	 {
			    		 numArray[i] = num;
			    		 break;
			    	 }
				 }
				//System.out.println("After inserting: ");
			     for (i=0; i < numArray.length; i++)
				 {
			    	 //System.out.printf("%d, ", numArray[i]);
				 }	
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				//System.out.println("Invalid insertion. Buffer is FULL.");
				return true;
			}
			numArray.notifyAll();
			return false;
		}
	}
	
	public synchronized boolean remove(Integer[] numArray)
    {	
	    synchronized(numArray)
		{
			while (numArray[0] == null)
			{				
				 try 
				 {	
					 //System.out.println("Consumer waiting because array is EMPTY.\n");		
					 numArray.wait();
					 //System.out.println("Consumer restarted after notify.\n");
				 }			 
				 catch (InterruptedException e) 
				 {
					Thread.currentThread().interrupt();
					//System.out.printf("Thread interrupted", e); 
				 }
			}
			try 
			{
				for (i= numArray.length - 1; i > -1; i--)
				 {
			    	 if (numArray[i] != null)
			    	 {
			    		 numArray[i] = null;
			    		 break;
			    	 }
				 }
		        //System.out.println("After removing: ");	        
				for (i=0; i < numArray.length; i++)
				{
					  //System.out.printf("%d, ", numArray[i]);
				}
				//System.out.println("");
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				//System.out.println("Invalid removal. Buffer is EMPTY.");
				return true;
			}
			numArray.notifyAll();
			return false;
		}
    }

	public static void main(String[] args) 
	{
		Integer array[] = new Integer[6];
		int prosleeptime = 300;
		int consleeptime = 300;

		//System.out.println("Scenario 0: Producer runs more frequent than Consumer thus setting empty buffer to FULL.");
		//System.out.println("Scenario 1: Consumer runs more frequent than Producer thus setting full buffer to EMPTY.");
		//System.out.println("Scenario 2: Producer and Consumer run with same frequency thus half-full buffer is never empty or full.");
		//System.out.println("Select scenario. Type 0, 1 or 2:");
		//System.out.println("");
		Scanner sc = new Scanner(System.in);
		int scenario = sc.nextInt();
		sc.close();
		Thread producer = new Thread(new Producer(array,prosleeptime));
		Thread consumer = new Thread(new Consumer(array,consleeptime));

		switch(scenario)
		{
			case 0: //empty buffer gets filled up
				consleeptime = 900;
				consumer = new Thread(new Consumer(array,consleeptime));
				producer.start();
				consumer.start();
				break;			
			case 1: //full buffer gets emptied out
				array[0] = 4;
				array[1] = 11;
				array[2] = 7;	
				array[3] = 18;
				array[4] = 5;
				array[5] = 16;
				prosleeptime = 900;
				producer = new Thread(new Producer(array,prosleeptime));
				consumer.start();
				producer.start();
				break;				
			case 2: //half-full buffer does not get full or empty
				array[0] = 4;
				array[1] = 11;
				array[2] = 7;		
				producer.start();
				consumer.start();
				break;
			default:
				break;
		}
		int i;
		//System.out.println("");
		//System.out.printf("Array length is %d.\n", array.length);
		//System.out.println("Initial array:");
		for (i=0; i < array.length;i++)
		{
			//System.out.printf("%d, ", array[i]);
		}
		//System.out.println("\n");

		try 
		{Thread.sleep(10000);} 
		catch (InterruptedException e) 
		{//System.out.println("Main thread Interrupted");}		
		 //System.out.println("Main thread exiting.");
	}
}



