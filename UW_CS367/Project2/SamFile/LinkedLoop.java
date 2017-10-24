///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoop.java
// Semester:         CS 367 - 4 - Fall 2017
//
// Author:           Sam Adams - sajadams@gmail.com
// CS Login:         sjadams3
// Lecturer's Name:  Dr. Charles Fischer
// Lab Section:      Section 4
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
//
// Pair Partner:     Mike Huang
// Email:            miked.huang@gmail.com
// CS Login:         zhuang335
// Lecturer's Name:  Dr. Charles Fischer
// Lab Section:      Section 4
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.Iterator;

/**
 * Implementation of a circular list. Consists of a doubly linked list whose head node links to the tail node
 *
 * <p>Bugs: No none bugs
 *
 * @author Sam Adams
 */
public class LinkedLoop<E> implements LoopADT<E>{
	
	//Properties
	private DblListnode<E> current; //pointer to the currently active node in the loop
	private int numItems; //Number of items that exist in the circular list

	//constructor
	public LinkedLoop(){
		current = new DblListnode<E>(null);
		numItems = 0;
	}
	@Override
	/**
	 * Adds the supplied item to the circular list
	 *
	 * @param (item) - The item to add to the list
	 */
	public void add(E item) {
		// TODO Auto-generated method stub
		if (numItems==0) { //create a single node that points to itself
			current = new DblListnode<E>(item);
			current.setNext(current);
			current.setPrev(current);
		}
		else { //otherwise insert the item between current and the previous item
		DblListnode<E> tmp = new DblListnode<E>(item);
		DblListnode<E> tmpPrev = current.getPrev();
		tmpPrev.setNext(tmp);
		current.setPrev(tmp);
		tmp.setNext(current);
		tmp.setPrev(tmpPrev);
		current = tmp;
		}
		numItems++;
		
	}

	@Override
	/**
	 * Returns the current item in the list 
	 *
	 * @return The current list node's data. If list is empty, throws an exception.
	 */
	public E getCurrent() throws EmptyLoopException {
		// TODO Auto-generated method stub
		if (numItems==0) {
			throw new EmptyLoopException();
			}
		else {
		return current.getData();
		}
	}

	@Override
	/**
	 * Removes the current node and returns the new current node's data.
	 *
	 * @return The current list node's data. If list is empty, throws an exception.
	 */
	public E removeCurrent() throws EmptyLoopException {
		// TODO Auto-generated method stub
		if (numItems==0) { //throw exception
			throw new EmptyLoopException();
		}
		else if (numItems==1) { //create a null list
			current = new DblListnode<E>(null);
		}
		else { //link the two nodes bordering current together
			DblListnode<E> tmpPrev = current.getPrev();
			current = current.getNext();
			tmpPrev.setNext(current);
			current.setPrev(tmpPrev);
		}
		numItems--;
		return current.getData();
	}

	@Override
	/**
	 * Iterates the list to the next node.
	 *
	 */
	public void next() {
		// TODO Auto-generated method stub
		if (numItems!=0) {
		current = current.getNext();
		}
	}

	@Override
	/**
	 * Iterates the list to the previous node.
	 *
	 */
	public void previous() {
		// TODO Auto-generated method stub
		if (numItems!=0) {
		current=current.getPrev();
		}
	}

	@Override
	/**
	 * Checks whether the list is empty or not.
	 *
	 * @return Returns true if the list is empty. Otherwise, returns false.
	 */
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if (numItems==0) {
			return true;
		}
		return false;
	}

	@Override
	/**
	 * Returns the size of the circular list
	 *
	 * @return The number of items in the circular list
	 */
	public int size() {
		// TODO Auto-generated method stub
		return numItems;
	}

	@Override
	/**
	 * Builds an iterator for the data structure
	 *
	 * @return An iterator that will loop through all items in the data structure once.
	 */
	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		LinkedLoopIterator<E> ret = new LinkedLoopIterator<E>(current,numItems);
		return ret;
	}

}
