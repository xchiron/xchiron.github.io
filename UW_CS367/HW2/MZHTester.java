
public class MZHTester {

	
	public static void main(String[] args) {
		LinkedList<Integer> mzhlist = new LinkedList<Integer>();
		
		for (int i = 0; i < 20; i++) {
			mzhlist.add(i);
		}
		
		
		
		
		mzhlist.reverse(0,12);
		
		
		
		
		
		for (int j = 0; j < mzhlist.size(); j++) {
			System.out.print(mzhlist.get(j)+"|");
		}
		System.out.print("size: "+mzhlist.size());
		//System.out.print(mzhlist.get(5));
		
	}
}
