/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.util.LinkedList;
import java.io.Serializable;

/**
 *   A list of postings for a given word.
 */
public class PostingsList implements Serializable {
    
    /** The postings list as a linked list. */
    private LinkedList<PostingsEntry> list = new LinkedList<PostingsEntry>();

    /**  Number of postings in this list  */
    public int size() {
	return list.size();
    }

    /**  Returns the ith posting */
    public PostingsEntry get( int i ) {
	return list.get( i );
    }

    //
    //  YOUR CODE HERE
    //

    /** Adds a posting */
    public void insert( PostingsEntry entry ) {

      //TODO entries in the postings list ordered by docIDs as in the book?
      if ( !containsDocID( entry.docID ) )
        list.addLast(entry);

    }

    /** Returns wheter this PostingsList contains the specified docID */
    private boolean containsDocID( int docID ) {
      
      for ( int i = 0 ; i < list.size() ; ++i )
        if ( docID == list.get(i).docID )
          return true;
      
      return false;

    }

}
	

			   
