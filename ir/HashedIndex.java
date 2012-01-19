/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  


package ir;

import java.util.LinkedList;
import java.util.HashMap;


/**
 *   Implements an inverted index as a Hashtable from words to PostingsLists.
 */
public class HashedIndex implements Index {

    /** The index as a hashtable. */
    private HashMap<String,PostingsList> index = new HashMap<String,PostingsList>();


    /**
     *  Inserts this token in the index.
     */
    public void insert( String token, int docID, int offset ) {

      if ( index.containsKey(token) ) {

        // Add the new docID to the postings list of this token
        index.get(token).insert( new PostingsEntry(docID) );

      } else {

        // Add a new element to the hash map
        PostingsList postingsList = new PostingsList();
        postingsList.insert( new PostingsEntry(docID) );
        index.put(token, postingsList);

      }

    }


    /**
     *  Returns the postings for a specific term, or null
     *  if the term is not in the index.
     */
    public PostingsList getPostings( String token ) {
	// 
	//  REPLACE THE STATEMENT BELOW WITH YOUR CODE
	//
	return null;
    }


    /**
     *  Searches the index for postings matching the query in @code{searchterms}.
     */
    public PostingsList search( LinkedList<String> searchterms, int queryType ) {
      
      if ( index.containsKey( searchterms.get(0) ) )
        return index.get( searchterms.get(0) );
      else
        return null;

    }


    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }
}
