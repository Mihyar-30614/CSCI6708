import java.util.*;
class DiffieHellman{
	public static void main(String args[]){
		
		Scanner input 	= new Scanner(System.in);
		Random rand 	= new Random();

		System.out.println("Enter the value of P and G: ");
		int p = input.nextInt();
		int g = input.nextInt();

		int sa = (int)(rand.nextInt(10) + 1);
		int sb = (int)(rand.nextInt(10) + 1);

		int Ta = (int)((Math.pow(g,sa)) % p);
		int Tb = (int)((Math.pow(g,sb)) % p);

		int Ka = (int)((Math.pow(Tb,sa)) % p);
		int Kb = (int)((Math.pow(Ta,sb)) % p);

		if(Ka==Kb){
			System.out.println("The Secret Key is: " + Ka);
		}else{
			System.out.println("Generation Failed");
		}
	}
}