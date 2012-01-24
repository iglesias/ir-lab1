/*  
 *   This file is part of the computer assignment for the
 *   Information Retrieval course at KTH.
 * 
 *   First version:  Johan Boye, 2010
 *   Second version: Johan Boye, 2012
 */  


package ir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;


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
     
      PostingsList answer = null;

      // If there is only one term to look for, the type does not matter
      if ( searchterms.size() == 1 )
        return index.get( searchterms.get(0) );

      switch (queryType) {
        case Index.INTERSECTION_QUERY:
          answer = intersectionSearch(searchterms);
          break;
        case Index.PHRASE_QUERY:
          answer = phraseSearch(searchterms);
      }

      return answer;

    }

    private PostingsList intersectionSearch( LinkedList<String> searchterms ) {

      PostingsList answer = index.get( searchterms.get(0) );

      for ( int i = 1 ; i < searchterms.size() ; ++i )
          answer = 
            PostingsList.intersect( answer, index.get( searchterms.get(i) ) );

      return answer;

    }

    private PostingsList phraseSearch( LinkedList<String> searchterms ) {

      PostingsList answer = index.get( searchterms.get(0) );

      for ( int i = 1 ; i < searchterms.size() ; ++i )
          answer = 
            PostingsList.posIntersect(answer, index.get( searchterms.get(i) ), 1);

      return answer;

    }

    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }
}
