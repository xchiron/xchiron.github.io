///////////////////////////////////////////////////////////////////////////////
//                   ALL STUDENTS COMPLETE THESE SECTIONS
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoopIterator.java
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
 * Class for creating an iterator method for our linked loop iteration
 *
 * <p>Bugs: No known bugs
 *
 * @author Sam Adams
 */

public class LinkedLoopIterator<E> implements Iterator<E>{


	private DblListnode<E> curr; // the current circular list node
	private Integer count; //the current size of the circular list
	private Integer numIterations; //number of iterations that have been performed so far
	
	LinkedLoopIterator(DblListnode<E> input,Integer numItems){
		curr = input;
		count=numItems;
		numIterations=0;
	}
	@Override
	/**
	 * Checks whether we have iterated through all the elements of the circular list
	 *
	 * @return True if there is another node to iterate to. Otherwise returns false
	 */
	public boolean hasNext() {
		// TODO Auto-generated method stub
		if (curr.getNext()==null) {
			return false;
		}
		if (numIterations>=count) {
			return false;
		}
		return true;
	}

	@Override
	/**
	 * Iterates the circular list once
	 *
	 * @return the next node in the list.
	 */
	public E next() {
		// TODO Auto-generated method stub
		numIterations++;
		E returnData= curr.getNext().getData();
		curr=curr.getNext();
		return returnData;
	}

}
