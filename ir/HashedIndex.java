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
        index.get(token).insert(docID, offset);

      } else {

        // Add a new element to the hash map
        PostingsList postingsList = new PostingsList();
        postingsList.insert(docID, offset);
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
     
      PostingsList answer = new PostingsList();

      int i = 0;
      while ( i < searchterms.size() && ! index.containsKey( searchterms.get(i) ) )
        ++i;

      if ( i < searchterms.size() )
        answer = index.get( searchterms.get(i) );

      ++i;
      while ( i < searchterms.size() ) {
      
        if ( index.containsKey( searchterms.get(i) ) ) {
          answer = 
            PostingsList.intersect(answer, index.get( searchterms.get(i) ) );
        }
        
        ++i;

      }

      return answer;

    }


    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }
}
