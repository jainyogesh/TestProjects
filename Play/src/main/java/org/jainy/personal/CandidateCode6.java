package org.jainy.personal;

public class CandidateCode6
{ 
	
	static List<Element> encodingList= null;
    public static String constructTree(String input1)
    {
    	StringBuilder result = new StringBuilder();
    	
		initEncodingList(input1);
		for(int i=0; i<input1.length(); i++){
    		char c = input1.charAt(i);
    		result.append(encodingList.get(c).getEncodeKey());
		}
    	
    	return result.toString();
    }
    
    static void initEncodingList(String input1){
    	encodingList = new List<Element>(256);
    	List<Element> sortedList = new List<Element>();
    	for(int i=0; i<input1.length(); i++){
    		char c = input1.charAt(i);
    		int position = c;
    		Element e = encodingList.get(position);
    		if(e == null){
    			e = new Element(position);
    			sortedList.add(e);
    			encodingList.add(e, position);
    		}
    		e.incrementTotal();
    		float f = (float)e.getTotal()/(float)input1.length();
    		e.setFrequency(f);
    	}
    	//System.out.println(encodingList.get(97));
    	//System.out.println(sortedList);
    	
    	sortedList.sort(new Comparator<CandidateCode6.Element>() {

			@Override
			public int compare(Element e1, Element e2) {
				if(e1.frequency > e2.frequency)
					return 1;
				if(e1.frequency < e2.frequency)
					return -1;
				if(e1.frequency == e2.frequency){
					if(e1.getPosition() < e2.getPosition())
						return 1;
					if(e1.getPosition() > e2.getPosition())
						return -1;
				}
				return 0;
			}
		});
    	//System.out.println(sortedList);
    	
    	String key = "0";
    	for(int i=0; i<sortedList.size();i++){
    		Element e = sortedList.get(i);
    		e.setEncodeKey(key);
    		encodingList.add(e, e.getPosition());
    		if(i == sortedList.size()-2){
    			key = key.replace('0', '1');
    		}else{
    			key = "1"+key;
    		}
    	}
    	
    	//System.out.println(sortedList);

    }
    
    private static class Element{
    	int position;
    	char c;
    	int total;
    	float frequency;
    	String encodeKey;
    	
    	Element(int position){
    		this.position = position;
    		this.c = (char)position;
    	}
    	
    	void incrementTotal(){
    		total++;
    	}
    	
    	void setFrequency(float frequency){
    		this.frequency = frequency;
    	}
    	
    	void setEncodeKey(String key){
    		this.encodeKey = key;
    	}
    	
    	String getEncodeKey(){
    		return this.encodeKey;
    	}
    	
    	int getPosition(){
    		return this.position;
    	}
    	
    	char getChar(){
    		return this.c;
    	}
    	
    	int getTotal(){
    		return this.total;
    	}
    	
    	float getFrequency(){
    		return this.frequency;
    	}
    	
    	public String toString(){
    		return this.c + ":" + this.position + ":" + this.total + ":" + this.frequency + ":" + this.encodeKey;
    	}
    }
    
    private static interface Comparator<E>{
    	
    	int compare(E e1, E e2);
    }
    private static class List<E> {
		Object[] buckets = null;
		int count = 0;

		private List() {
			buckets = new Object[100];
		}

		private List(int size) {
			buckets = new Object[size];
		}

		private List(E[] arr) {
			buckets = arr;
			count = arr.length;
		}

		private void add(E obj) {
			if (count + 1 > buckets.length) {
				Object[] copy = new Object[buckets.length * 2];
				System.arraycopy(buckets, 0, copy, 0, buckets.length);
				buckets = copy;
			}
			buckets[count] = obj;
			count++;
		}
		
		private void add(E obj, int position) {
			if (position + 1 > buckets.length) {
				Object[] copy = new Object[position * 2];
				System.arraycopy(buckets, 0, copy, 0, buckets.length);
				buckets = copy;
			}
			buckets[position] = obj;
			count++;
		}

		private E get(int index) {
			return (E) buckets[index];
		}

		private int size() {
			return count;
		}

		public String toString() {
			StringBuilder res = new StringBuilder();
			for (int i = 0; i < count - 1; i++) {
				res.append(buckets[i]).append(',');
			}
			res.append(buckets[count - 1]);
			return res.toString();
		}
		
		void sort(Comparator<E> cmp){
			for(int i=0; i < count; i++){
				E e1 = (E)buckets[i];
				for(int j=i; j<count;j++){
					E e2 = (E)buckets[j];
					if(cmp.compare(e1, e2)<0){
						buckets[j] = e1;
						buckets[i] = e2;
						e1 = e2;
					}
				}
			}
		}
	}
}