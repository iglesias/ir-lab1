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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;


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

      switch (queryType) {
        case Index.INTERSECTION_QUERY:
          answer = intersectionSearch(searchterms);
          break;
        case Index.PHRASE_QUERY:
          answer = phraseSearch(searchterms);
          break;
        case Index.RANKED_QUERY:
          answer = rankedSearch(searchterms);
          break;
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
     *
     */
    private PostingsList rankedSearch( LinkedList<String> searchterms ) {

      // Number of documents
      int N = docIDs.size();

      PostingsList answer = new PostingsList();
      // Documents that contain at least one term of the query, union
      for ( String term : searchterms )
        answer.unite( index.get(term) );
      System.out.println("answer has " + answer.size() + " elements");

      // Record of the position where they are inserted in the union
      HashMap<Integer, Integer> idxs = new HashMap<Integer, Integer>();
      for ( int i = 0 ; i < answer.size() ; ++i )
        idxs.put(answer.get(i).docID, i);

      // Initialize the scores
      for ( int i = 0 ; i < answer.size() ; ++i )
        answer.get(i).score = 0;

      for ( String term : searchterms ) {

        PostingsList postings = index.get(term);

        // Document frequency, number of documents that contain the term
        int df = postings.size();

        for ( int i = 0 ; i < postings.size() ; ++i ) {
          
          PostingsEntry entry = postings.get(i);

          // Term frequency, times the term appears in the document
          int tf = entry.positions.size();
          // Compute the weight of the term in the document
          double wd = ( 1 + Math.log10(tf) )*Math.log10( (double)(N) / df );
          // Update the score of the adequate entry in the answer postings list
          answer.get( idxs.get( entry.docID ) ).score += wd;

        }

      }

      /*
      // Normalize the scores using the length of the documents
      for ( int i = 0 ; i < answer.size() ; ++i ) {
        PostingsEntry entry = answer.get(i);
        int docID = entry.docID;
        entry.score = entry.score / Math.sqrt( docLengths.get( ""+ docID ) );
        //entry.score = entry.score / docVectorLengths.get( "" + docID );
      }
      */

      return answer.sort();

    }

    /**
     *  No need for cleanup in a HashedIndex.
     */
    public void cleanup() {
    }

    public void computeDocVectorLengths() {

      // Number of documents
      int N = docIDs.size();
      // Number of different indexed terms
      int M = index.size();

      Set<String> keyDocIDs = docIDs.keySet();
      Set<String> keyTerms;
      Iterator<String> docIDIt = keyDocIDs.iterator();
      Iterator<String> termIt;
      int docID;
      int tf, df;         // Term frequency and document frequency
      double length, w;   // Vector length of a doc and weight of an element

      // For every document in the collection
      while ( docIDIt.hasNext() ) {

        docID = Integer.parseInt( docIDIt.next() );

        // For every term, check if the document contains that term
        keyTerms  = index.keySet();
        termIt    = keyTerms.iterator();
        length    = 0;

        while ( termIt.hasNext() ) {
          String term = termIt.next();
          PostingsList postings = index.get(term);

          if ( postings == null ) continue;

          if ( ( tf = postings.getTermFreq(docID) ) > 0 ) {
            df      = postings.size();
            w       = ( 1 + Math.log10(tf) )*Math.log10( (double)(N) / df );
            length += w*w;
          }
        }
        
        // Store the vector length of the document
        docVectorLengths.put( "" + docID, Math.sqrt(length) );

      }

    }

}
