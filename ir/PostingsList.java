/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  

package ir;

import java.util.Collections;
import java.util.Comparator;
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
    public void insert(int docID, int offset) {

      //TODO This insert could be done faster using something similar to binary
      // search

      // The postings list is maintained ordered by docID
      int i = 0;
      while ( i < list.size() && list.get(i).docID < docID )  ++i;
      
      if ( i >= list.size() )                   // There was no bigger docID
        list.add( new PostingsEntry(docID, offset) );
      else if ( docID == list.get(i).docID )    // docID already in the list
        list.get(i).positions.add(offset);
      else                                      // Insert docID in the middle
        list.add( i, new PostingsEntry(docID, offset) );

    }

    /** Intersect two postings lists that sorted by docID */
    public static PostingsList intersect( PostingsList p1, PostingsList p2 ) {

      // Create a postings list that will contain the intersection
      PostingsList answer = new PostingsList();

      int i1 = 0, i2 = 0;   // Indices to the elements of the postings lists
      while ( i1 < p1.list.size() && i2 < p2.list.size() ) {
      
        PostingsEntry e1 = p1.list.get(i1);
        PostingsEntry e2 = p2.list.get(i2);

        if ( e1.docID == e2.docID ) {
          answer.insert(e1.docID, 0);
          ++i1;
          ++i2;
        } else if ( e1.docID < e2.docID ) {
          ++i1;
        } else {
          ++i2;
        }

      }

      return answer;

    }

    /** Returns wheter this PostingsList contains the specified docID */
    /** @deprecated Because the postings lists are ordered by docID now, remove
     *  deprecated and use again if needed
     */
    private boolean containsDocID( int docID ) {
      
      for ( int i = 0 ; i < list.size() ; ++i )
        if ( docID == list.get(i).docID )
          return true;
      
      return false;

    }

}
	
/**
 * @deprecated Because the postings lists are ordered by docID now, remove
 * deprecated and use again if needed
 */
class DocIDComparator implements Comparator< PostingsEntry > {

  public int compare( PostingsEntry lEntry, PostingsEntry rEntry ) {
    return lEntry.docID - rEntry.docID;
  }

}
			   
