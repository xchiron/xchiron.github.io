///////////////////////////////////////////////////////////////////////////////
//
// Main Class File:  ImageLoopEditor.java
// File:             LinkedLoopIterator.java
// Semester:         CS 367 Fall 2017
//
// Author:           Mike Huang		miked.huang@gmail.com
// CS Login:         zhuang335
// Lecturer's Name:  Charles N. Fischer
//
// Pair Partner:     Sam Adams
// Email:            sajadams@gmail.com
// CS Login:         sjadams3
// Lecturer's Name:  Charles N. Fischer
//
//////////////////////////// 80 columns wide //////////////////////////////////
import java.util.Iterator;

/**
 * An iterator to move through a circular doubly-linked list
 *
 * <p>Bugs: none known
 *
 * @author Mike Huang
 */
public class LinkedLoopIterator<E> implements Iterator<E> 
{
	
	/** Private doubly-linked node */
	private DblListnode<E> items;
	
	/** Private integer storing the total number of nodes in the list */
	private int numNodes;
	
	/** Private integer storing the current position */
	private int pos;

	/**
	 * Constructor for the Linked Loop Iterator
	 * 
	 * @param item current node in the list
	 * @param numNode total number of items in the list
	 */
	LinkedLoopIterator(DblListnode<E> item, int numNodes) 
	{
		this.items = item;
		this.numNodes = numNodes;
		this.pos = 0;
	}

	/**
	 * Determines if we have more nodes to iterate over
	 * 
	 * @return true if there are more, or false if we have gone through the
	 *         entire loop
	 */
	@Override
	public boolean hasNext() 
	{
		return this.pos < this.numNodes;
	}

	/**
	 * Gets the next value in the list and increment the position
	 * 
	 * @return the data from the next item
	 */
	@Override
	public E next() 
	{
		items = items.getNext();
		E data = items.getData();
		
		// if position hits the end, reset to beginning
		if (pos == numNodes)
		{
			pos=0;
		}
		else
		{
			pos++;
		}
		return data;
	}

	/**
	 * Unimplemented remove method
	 */
	@Override
	public void remove() 
	{
		throw new UnsupportedOperationException();
	}
}
