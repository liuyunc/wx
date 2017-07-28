package com.wx.android.fx;
/*
该程序用来计算PTT，通过使用两路信号：ecg和ppg信号。
一开始对信号进行去直流，然后经过低通滤波器，再分别对两路信号进行
检测峰值，最后计算两路信号峰值的时间差。
 */
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class BloodPressure2 {
	//去直流；
	public static void detrend(double array[], int n)
	{	  
			
			 double x, y, a, b;
		     double sy = 0.0,
		            sxy = 0.0,
		            sxx = 0.0;
		     int i;

		     for (i=0, x=(-n/2.0+0.5); i<n; i++, x+=1.0)
		     {
		          y = array[i];
		          sy += y;
		          sxy += x * y;
		          sxx += x * x;
		     }
		     b = sxy / sxx;
		     a = sy / n;

		     for (i=0, x=(-n/2.0+0.5); i<n; i++, x+=1.0)
			 {
				 array[i] -= (a+b*x);
			 }
				
	}
	//低通滤波器：
	public static void  ButterworthLowpassFilter0040SixthOrder(double src[], double dest[], int size) {
		   			int NZEROS = 6;
				    int NPOLES = 6;
				    double GAIN = 4.004448900e+05;
				     //double xv[NZEROS+1] = {0}, yv[NPOLES+1] = {0};
				     //double xv[NZEROS+1]=new double(NZEROS+1){0};
				    double xv[]={0.0,0.0,0.0,0.0,0.0,0.0,0.0,};
				    
				    double yv[]={0.0,0.0,0.0,0.0,0.0,0.0,0.0,};
				    for (int i = 0; i < size; i++)
				    { 
				        xv[0] = xv[1]; xv[1] = xv[2]; xv[2] = xv[3]; xv[3] = xv[4]; xv[4] = xv[5]; xv[5] = xv[6]; 
				        xv[6] = src[i] / GAIN;
				        yv[0] = yv[1]; yv[1] = yv[2]; yv[2] = yv[3]; yv[3] = yv[4]; yv[4] = yv[5]; yv[5] = yv[6]; 
				        yv[6] =   (xv[0] + xv[6]) + 6.0 * (xv[1] + xv[5]) + 15.0 * (xv[2] + xv[4])
				                     + 20.0 * xv[3]
				                     + ( -0.3774523864 * yv[0]) + (  2.6310551285 * yv[1])
				                     + ( -7.6754745482 * yv[2]) + ( 11.9993158160 * yv[3])
				                     + (-10.6070421840 * yv[4]) + (  5.0294383514 * yv[5]);
				        dest[i] = yv[6];
					}
}
		   	
	/*public static void findPeaks(double[] num,double threshold)  
	{  
		int count = num.length;
	    Vector<Integer> sign = new Vector<Integer>();  
	    for(int i = 1;i<count;i++)  
	    {  
	        相邻值做差： 
	         *小于0，赋-1 
	         *大于0，赋1 
	         *等于0，赋0 
	          
	        double diff = num[i] - num[i-1];  
	        if(diff>0)  
	        {  
	            sign.add(1);  
	        }  
	        else if(diff<0)  
	        {  
	            sign.add(-1);  
	        }  
	        else  
	        {  
	            sign.add(0);  
	        }  
	    }  
	    //再对sign相邻位做差  
	    //保存极大值和极小值的位置  
	    Vector<Integer> indMax = new Vector<Integer>();  
	    Vector<Integer> indMin = new Vector<Integer>();  
	    int temp=sign.size();
	    for(int j = 1;j<sign.size();j++)  
	    {  
	        //if((sign.get(j)>0.6||sign.get(j)<-0.6)){
	        	double diff = sign.get(j)-sign.get(j-1);  
		        if(diff<0&&(num[j]>threshold||num[j]<-threshold)) 
		        {  
		            indMax.add(j);  
		        }  
		        else if(diff>0&&(num[j]>0.6||num[j]<-0.6))  
		        {  
		            indMin.add(j);  
		        }
	        //}
	    	double diff = sign.get(j)-sign.get(j-1);  
	        if(diff<0)  
	        {  
	            indMax.add(j);  
	        }  
	        else if(diff>0)  
	        {  
	            indMin.add(j);  
	        }  
	     
	    }  
	    System.out.println("极大值为:"+indMax.size());  
	    for(int m = 0;m<indMax.size();m++)  
	    {  
	    	System.out.print(num[indMax.get(m)]+ " ");
	    	System.out.print(indMax.get(m)+" ");
	    }  
	    System.out.println(); 
	    System.out.println("极小值为:"+indMin.size());  
	    for(int n = 0;n<indMin.size();n++)  
	    {  
	    	System.out.print(num[indMin.get(n)]+ " ");
	    	System.out.print(indMax.get(n)+" ");
	    }
	}  */
	//找波峰
	public static ArrayList<Integer> findPeaks(double[] num,double threshold)  
	{  
		int count = num.length;
		ArrayList<Integer> tempList=new ArrayList<>();
	    Vector<Integer> sign = new Vector<Integer>();  
	    for(int i = 1;i<count;i++)  
	    {  
	        /*相邻值做差： 
	         *小于0，赋-1 
	         *大于0，赋1 
	         *等于0，赋0 */
	          
	        double diff = num[i] - num[i-1];  
	        if(diff>0)  
	        {  
	            sign.add(1);  
	        }  
	        else if(diff<0)  
	        {  
	            sign.add(-1);  
	        }  
	        else  
	        {  
	            sign.add(0);  
	        }  
	    }  
	    //再对sign相邻位做差  
	    //保存极大值和极小值的位置  
	    Vector<Integer> indMax = new Vector<Integer>();  
	    Vector<Integer> indMin = new Vector<Integer>();  
	    int temp=sign.size();
	    for(int j = 1;j<sign.size();j++)  
	    {  
	        //if((sign.get(j)>0.6||sign.get(j)<-0.6)){
	        	double diff = sign.get(j)-sign.get(j-1);  
		        if(diff<0&&(num[j]>threshold||num[j]<-threshold)) 
		        {  
		            indMax.add(j); 
		            tempList.add(j);
		        }  
		        else if(diff>0&&(num[j]>0.6||num[j]<-0.6))  
		        {  
		            indMin.add(j);
		            //tempList.add(j);
		        }
	        //}
	    	/*double diff = sign.get(j)-sign.get(j-1);  
	        if(diff<0)  
	        {  
	            indMax.add(j);  
	        }  
	        else if(diff>0)  
	        {  
	            indMin.add(j);  
	        }*/  
	     
	    }  
	    System.out.println("极大值为:"+indMax.size());  
	    for(int m = 0;m<indMax.size();m++)  
	    {  
	    	System.out.print(num[indMax.get(m)]+ " ");
	    	System.out.print(indMax.get(m)+" ");
	    }  
	    System.out.println(); 
	    System.out.println("极小值为:"+indMin.size());  
	    for(int n = 0;n<indMin.size();n++)  
	    {  
	    	System.out.print(num[indMin.get(n)]+ " ");
	    	System.out.print(indMin.get(n)+" ");
	    }
	    System.out.println();
	    return tempList;
	}
	public  static double calcuPTT(ArrayList<Integer> ecgList,ArrayList<Integer> ppgList,int f) {
		int i;
		double ptt=0;
		int pptSize;
		if(ecgList.size()<=ppgList.size())
			pptSize=ecgList.size();
		else {
			pptSize=ppgList.size();
		}
		for(i=0;i<ppgList.size();i++){
			 ptt+=ecgList.get(i)-ppgList.get(i);
		}
		return (ptt/pptSize)/f;
	}
	public static void main(String[] args) throws IOException {
		BufferedReader in=new BufferedReader(new FileReader("C:\\ecg.txt"));   //读取ecg的信号
		ArrayList<String> lineList=new ArrayList<>();
		String line;
		while((line=in.readLine())!=null){
			//Double doublea=(Double)in.read();
			//in.r
			lineList.add(line);
		}
		for(String s:lineList)
			System.out.println(s);
		double[] array=new double[lineList.size()];
		int i=0;
		for(String s:lineList){
			array[i++]=Double.parseDouble(s);
			//System.out.println(Double.parseDouble(s));
		}
		double dest[]=new double[array.length];
		
		
		detrend(array,array.length);
		/*for(Double d:array)
			System.out.println(d);*/
		
		
		
		ButterworthLowpassFilter0040SixthOrder(array, dest, array.length);
		/*for(Double d:dest)
			//System.out.println(d);*/
		//findPeaks(dest,0.6);
		
		
		BufferedReader inn=new BufferedReader(new FileReader("C:\\ppg.txt")); //读取ppg的信号
		ArrayList<String> lList=new ArrayList<>();
		String string;
		while((string=inn.readLine())!=null){
			//Double doublea=(Double)in.read();
			//in.r
			lList.add(string);
		}
		double[] arrayPpg=new double[lList.size()];
		i=0;
		for(String s:lList){
			arrayPpg[i++]=Double.parseDouble(s);
			//System.out.println(Double.parseDouble(s));
		}
		double destPpg[]=new double[arrayPpg.length];
		
		
		detrend(arrayPpg,arrayPpg.length);
		/*for(Double d:array)
			System.out.println(d);*/
		
		
		
		ButterworthLowpassFilter0040SixthOrder(arrayPpg, destPpg, arrayPpg.length);
		/*for(Double d:dest)
			//System.out.println(d);*/
		//findPeaks(destPpg,0.6);
		/*double ecg[]=new double{};*/
		//calcuPTT(findPeaks(dest,0.6), findPeaks(destPpg,0.6));
		System.out.println("ppt时间是"+calcuPTT(findPeaks(dest,0.6), findPeaks(destPpg,0.6),2000));
		
		double pwv;
		double height=175;//设置身高；
		pwv=(calcuPTT(findPeaks(dest,0.6), findPeaks(destPpg,0.6),2000))/height;
		
		double bp;
		int p1=700,p2=76600,p3=-1,p4=9;
		bp=Math.pow(p1*pwv*Math.E, p3*pwv)+p2*+p2*Math.pow(pwv, p4);//由ptt到血压的公式；
		
		System.out.println("血压为："+bp);
		
		
		
	}
}
