import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
class PNN
{
	public static void main(String args[])
	{
		String csvFile = "G:/PNN/md.csv";
        String line = "";
        String cvsSplitBy = ",";
		List<double[]> NSDI_train=new ArrayList<double[]>();
		List<double[]> INSPIRE_train=new ArrayList<double[]>();
		List<double[]> FGDC_train=new ArrayList<double[]>();
		int count[]=new int[3];
		//storing training data in 2D array based on the class
		int c=3;
		
		double train=10;   //train% of input data is taken as training data
		System.out.print("Enter the amount of data to be taken for training:(in %)");
		Scanner s=new Scanner(System.in);
		train=s.nextInt();
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
		{
			br.readLine(); //Skip first line
			while ((line = br.readLine()) != null)
			{
                String[] tuple= line.split(cvsSplitBy);
				if(tuple[176].equals("\"FGDC\""))
				{
					double temp[]=new double[175];
					for(int i=1;i<176;i++)
					{
						temp[i-1]=i*Float.parseFloat(tuple[i]);
					}
					FGDC_train.add(temp);
					count[0]++;
				}
				else if(tuple[176].equals("\"INSPIRE\""))
				{
					double temp[]=new double[175];
					for(int i=1;i<176;i++)
					{
						temp[i-1]=i*Float.parseFloat(tuple[i]);
					}
					INSPIRE_train.add(temp);
					count[1]++;
				}
				else //NSDI
				{
					double temp[]=new double[175];
					for(int i=1;i<176;i++)
					{
						temp[i-1]=i*Float.parseFloat(tuple[i]);
					}
					NSDI_train.add(temp);
					count[2]++;
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		//create test data
		List<Integer> output_labels=new ArrayList<Integer>();
		List<double[]> test=new ArrayList<double[]>();
		int limit=(int)Math.floor(count[0]*train/100);
		for(int i=limit;i<count[0];i++)
		{
			test.add(FGDC_train.get(i));
			output_labels.add(0);
			/*float temp[]=FGDC_train.get(i);
			System.out.println("i="+i);
			for(int z=0;z<temp.length;z++)
			{
				System.out.print(temp[z]+",");
			}
			System.out.println("\n");*/
		}
		for(int i=count[0]-1;i>=limit;i--)
		{
			FGDC_train.remove(i);  //this is working
		}
		limit=(int)Math.floor(count[1]*train/100);
		for(int i=limit;i<count[1];i++)
		{
			test.add(INSPIRE_train.get(i));
			output_labels.add(1);
		}
		for(int i=count[1]-1;i>=limit;i--)
		{
			INSPIRE_train.remove(i);  //this is working
		}
		limit=(int)Math.floor(count[2]*train/100);
		for(int i=limit;i<count[2];i++)
		{
			test.add(NSDI_train.get(i));
			output_labels.add(2);
		}
		for(int i=count[2]-1;i>=limit;i--)
		{
			NSDI_train.remove(i);  //this is working
		}
		int iterator=0;
		
		/*System.out.println("total test = "+test.size());
		System.out.println("FGDC_train = "+FGDC_train.size());
		System.out.println("INSPIRE_train = "+INSPIRE_train.size());
		System.out.println("NSDI_train = "+NSDI_train.size());
		*/
		
		
		
		double confusion[][]=new double[c][c];
		for (double[] row : test) 
		{   //loop through every test data
			double dr=0;
			ArrayList<Double> test_data = new ArrayList<Double>();
			for(int i=0;i<row.length;i++)
			{   //loop through each data in each test TUPLE to find dr
				test_data.add(row[i]);
				dr+=(row[i]*row[i]);
			}
			dr=Math.sqrt(dr);
			for(int i=0;i<row.length;i++)   //normalizing test data dr
			{
				row[i]/=dr;
			}
			double final_sum[]=new double[3];

			
		//----------------------------------------------------------------------	
			for(int j=0;j<FGDC_train.size();j++)//loop through training data of each class
			{
				dr=0;
				double data[]=FGDC_train.get(j);
				for (int k=0;k<data.length;k++) {
					dr+=(data[k]*data[k]);
				}
				dr=Math.sqrt(dr);
				for (int k=0;k<data.length;k++) {
					data[k]/=dr;
				}
				double sum=0;
				//multiply corresponding training and test data
				for(int k=0;k<row.length;k++)
				{
					sum+=(row[k]*data[k]);
					//if(k==1&&j==1)
					//System.out.println("row["+k+"]*data["+k+"="+row[k]+"*"+data[k]);
				}
				sum=(sum-1)/1; //denominator is sigma and its value is 1
				

				final_sum[0]+=Math.exp(sum);
			}
			final_sum[0]/=FGDC_train.size();
			//System.out.println("final final Sum="+final_sum[0]);
			
		//-------------------------------------------------------------------

			for(int j=0;j<INSPIRE_train.size();j++)//loop through training data of each class
			{
				dr=0;
				double data[]=INSPIRE_train.get(j);
				for (int k=0;k<data.length;k++) {
					dr+=(data[k]*data[k]);
				}
				dr=Math.sqrt(dr);
				for (int k=0;k<data.length;k++) {
					data[k]/=dr;
				}
				double sum=0;

				//multiply corresponding training and test data
				for(int k=0;k<row.length;k++)
				{
					sum+=(row[k]*data[k]);
				}
				sum=(sum-1)/1; //denominator is sigma and its value is 1
				final_sum[1]+=Math.exp(sum);
			}
			final_sum[1]/=INSPIRE_train.size();
			//System.out.println("final final Sum="+final_sum[1]);
		//----------------------------------------------------------------

			for(int j=0;j<NSDI_train.size();j++)//loop through training data of each class
			{
				dr=0;
				double data[]=NSDI_train.get(j);
				for (int k=0;k<data.length;k++) {
					dr+=(data[k]*data[k]);
				}
				dr=Math.sqrt(dr);
				for (int k=0;k<data.length;k++) {
					data[k]/=dr;
				}
				double sum=0;

				//multiply corresponding training and test data
				for(int k=0;k<row.length;k++)
				{
					sum+=(row[k]*data[k]);
				}

				sum=(sum-1)/1; //denominator is sigma and its value is 1

				final_sum[2]+=Math.exp(sum);
				//System.out.println("final sum="+final_sum[2]);
			}
			final_sum[2]/=NSDI_train.size();
			//System.out.println("final final Sum="+final_sum[2]);
			double avg[]=new double[c];
			for(int i=0;i<c;i++)
			{
				avg[i]=Math.abs(final_sum[i]-1);
			}
			//find least avg and that determines the output class
			double smallest=Double.POSITIVE_INFINITY;
			int index=-1;
			for(int i=0;i<c;i++)
			{
				if(avg[i]<smallest)
				{
					smallest=avg[i];
					index=i;
				}
			}
			//System.out.println("Iterator: "+iterator);
			//System.out.println("The output class is: "+(index+1));
			if(output_labels.get(iterator)==index)
			{
				confusion[output_labels.get(iterator)][output_labels.get(iterator)]++;
			}
			else
			{
				//confusion[index][output_labels.get(iterator)]++;
				System.out.println(index);
				confusion[output_labels.get(iterator)][index]++;
			}
			iterator++;
		}
		
		//calculate accuracy from confusion matrix
		double sum_confusion=0;
		double main_diagonal=0;
		for(int i=0;i<c;i++)
		{
			for(int j=0;j<c;j++)
			{
				sum_confusion+=confusion[i][j];
				if(i==j)
					main_diagonal+=confusion[i][j];
			}
		}
		System.out.println("Total number of tuples found in file: "+(limit+iterator));
		System.out.println("Number of training tuples(for each class):"+limit);
		System.out.println("Number of test tuples: "+iterator);
		System.out.println("The confusion matrix is:");
		for(int i=0;i<c;i++)
		{
			for(int j=0;j<c;j++)
			{
				System.out.print((int)confusion[i][j]+"   ");
			}
			System.out.println("");
		}
		System.out.println("The accuracy of prediction is:"+(main_diagonal/sum_confusion)*100+"%");
	}
}