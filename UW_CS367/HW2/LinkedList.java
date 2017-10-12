
public class LinkedList<E> implements ListADT<E>{
	
	private DblListnode<E> items; // pointer to the header node of the list of items
	private DblListnode<E> lastNode;
	private int numItems;
	
	/**
	 Constructs a Linked List 
	 */
	public LinkedList() {
		items = lastNode = new DblListnode<E>(null);
		numItems=0;
	}
	
	/**
	 * Adds an item to the Double Linked List
	 *
	 * @param item object to add to a liked list defined by E
	 */
	public void add(E item) {
		DblListnode<E> tmp = lastNode;
		lastNode.setNext(new DblListnode<E>(item));
	    lastNode = lastNode.getNext();
	    lastNode.setPrev(tmp);
	    numItems++;
	}
	
	/**
	 * Adds an item to a certain position in the Double Linked List
	 *
	 * @param pos the position to add the item
	 * @param item object to add to a liked list defined by E
	 */
    public void add(int pos, E item) {
    	 // check for bad position
        if (pos < 0 || pos > numItems) {
            throw new IndexOutOfBoundsException();
        }
    	
        // if asked to add to end, let the other add method do the work
        if (pos == numItems) {
            add(item);
            return;
        }
     
        // find the node n after which to add a new node and add the new node
        DblListnode<E> n = items.getNext();
        for (int k = 0; k < pos; k++) {
            n = n.getNext();
        }
        
        // store the previous node of that position
        DblListnode<E> tmp = n.getPrev();
		
        // set the new node with the new item and attach the previous one to it
        tmp.setNext(new DblListnode<E>(item));
		
        // attach new node to the previous node
		tmp.getNext().setPrev(tmp);
		
		// attach new node to the n node
		tmp.getNext().setNext(n);
		
		//  attach n node to the new node
		n.setPrev(tmp.getNext());
		
		// increment count.
	    numItems++;
    }
    
    /**
	 * Returns whether the item exists in the List
	 *
	 * @param item object to add to a liked list defined by E
	 * @return True if item is in the list.  Otherwise False
	 */
    public boolean contains(E item) {
    	DblListnode<E> n = items.getNext();
    	for (int k = 0; k < numItems; k++) {
            n = n.getNext();
            if (n.getData() == item) {
            	return true;
            }
        }
    	return false;
    }
    
    /**
     * Provides the size of the list
     * 
     * @return Returns an integer representing the size of the list
     */
    public int size() {
    	return numItems;
    }
    
    /**
     * Returns true if there are no items in the list
     * 
     * @return True if the list is empty. Otherwise False
     */
    public boolean isEmpty() {
    	if (numItems==0) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Returns the object at a certain position
     * 
     * @param pos The position you want to get the Object from
     * @return The object at position pos
     */
    public E get(int pos) {
    	
    	if (pos < 0 || pos > numItems) {
            throw new IndexOutOfBoundsException();
        }
    	
    	/*
    	if (pos == numItems-1) {
    		return lastNode.getData();
        }
    	*/
    	
    	DblListnode<E> n = items.getNext();
        for (int k = 0; k < pos; k++) {
            n = n.getNext();
        }
        return n.getData();
    }
    
    /**
     * Removes the object at a certain position
     * 
     * @param pos The position to remove the object
     * @return The object that is removed
     */
    public E remove(int pos) {
    	
    	if (pos < 0 || pos > numItems) {
            throw new IndexOutOfBoundsException();
        }
    	
    	if (pos == numItems-1) {
    		E tmpData = lastNode.getData();
    		lastNode = lastNode.getPrev();
    		lastNode.setNext(null);
    		// decrement count.
    	    numItems--;
    		return tmpData;
        }
    	
    	// loop through to find the nth position node
    	DblListnode<E> n = items.getNext();
        for (int k = 0; k < pos; k++) {
            n = n.getNext();
        }
	    
	    // change the prev field of the node after n
	    DblListnode<E> tmp = n.getNext();
	    tmp.setPrev(n.getPrev());
	   
	    // change the next field of the node before n
	    tmp = n.getPrev();
	    tmp.setNext(n.getNext());
	    
	    // decrement count.
	    numItems--;
	    
	    return n.getData();
    }
    
    /**
     * Reverses the order of the items from pos1 to pos2
     * 
     * @param pos1 The start position of the reversal group
     * @param pos2 The end position of the reversal group
     */
    public void reverseEasy(int pos1, int pos2){
    	  // If pos1 == pos2, reversing a single list element does nothing
    	  // If pos1 > pos2, reversing an empty sublist does nothing
    	  if (pos1 >= pos2)
    	     return;
    										
    	  // We swap the 1st and last items in the sublist,
    	  //  then recursively reverse the remaining sublist
    	  // We stop when the remaining sublist has size 0 or 1

    	  // Swap list items at pos1 and pos2
    	  E temp = remove(pos2);
    	  add(pos2, get(pos1));
    	  remove(pos1);
    	  add(pos1, temp);

    	  // Now recursively reverse remainder of sublist (if any)
    	  // The remaining sublist is from pos1+1 to pos2-1
    	  reverseEasy(pos1+1, pos2-1);
    	}
    
    /**
     * Reverses the order of the items from pos1 to pos2 the difficult way for homework
     * 
     * @param pos1 The start position of the reversal group
     * @param pos2 The end position of the reversal group
     */
    public void reverse(int pos1, int pos2){
  	  // If pos1 == pos2, reversing a single list element does nothing
  	  // If pos1 > pos2, reversing an empty sublist does nothing
  	  if (pos1 >= pos2) {
  	     return;
  	  }
  	  
  	  // If either positions are out of bonds, throw IndexOutOfBoundsException
      if (pos1 < 0 || pos1 > numItems-1 || pos2 < 0 || pos2 > numItems-1) {
         throw new IndexOutOfBoundsException();
      }				
            
      // We swap the 1st and last items in the sublist,
  	  //  then recursively reverse the remaining sublist
  	  // We stop when the remaining sublist has size 0 or 1
      
  	  // find the node in pos1
      DblListnode<E> pos1node = items.getNext(); // items is the header node so n is the first node in the list
      for (int k = 0; k < pos1; k++) {
    	  pos1node = pos1node.getNext();
      }
      
      // find the node in pos2
      DblListnode<E> pos2node = items.getNext(); // items is the header node so n is the first node in the list
      for (int j = 0; j < pos2; j++) {
    	  pos2node = pos2node.getNext();
      }
      
      // establish the previous and next nodes for each except pos2Next
      // this is in case pos2 is the last position in the list
      DblListnode<E> pos1Prev = pos1node.getPrev();
      DblListnode<E> pos1Next = pos1node.getNext();
      DblListnode<E> pos2Prev = pos2node.getPrev();
      
      // If pos2node is the last node, swap the points and establish pos1node as lastNode
      // pos1node will not have a linkage out to a next position
      //if (pos2node == lastNodej) {
      if (pos2 == numItems-1) {
    	  if (pos1node.getNext() == pos2node) {
    		  pos1node.setNext(null);
        	  pos1node.setPrev(pos2node);
        	  pos1Prev.setNext(pos2node);
        	  pos2node.setPrev(pos1Prev);
        	  pos2node.setNext(pos1node);
          }
          else {
        	  pos1node.setNext(null);
        	  pos1node.setPrev(pos2Prev);
              pos2Prev.setNext(pos1node);
              pos2node.setPrev(pos1Prev);
              pos2node.setNext(pos1Next);
              pos1Prev.setNext(pos2node);
              pos1Next.setPrev(pos2node);
          }
      }
      // If pos2node isn't the last node, we do the full swamp and linkage for both
      else {
    	  DblListnode<E> pos2Next = pos2node.getNext();
          
          if (pos1node.getNext() == pos2node) {
        	  pos1node.setNext(pos2Next);
        	  pos1node.setPrev(pos2node);
        	  pos1Prev.setNext(pos2node);
        	  pos2node.setPrev(pos1Prev);
        	  pos2node.setNext(pos1node);
        	  pos2Next.setPrev(pos1node);
          }
          else {
        	  pos1node.setPrev(pos2Prev);
              pos1node.setNext(pos2Next);
              pos2Prev.setNext(pos1node);
              pos2Next.setPrev(pos1node);
              pos2node.setPrev(pos1Prev);
              pos2node.setNext(pos1Next);
              pos1Prev.setNext(pos2node);
              pos1Next.setPrev(pos2node);
          }
      }
        	  
  	  // Now recursively reverse remainder of sublist (if any)
  	  // The remaining sublist is from pos1+1 to pos2-1
  	  reverse(pos1+1, pos2-1);
  	}
	
}
