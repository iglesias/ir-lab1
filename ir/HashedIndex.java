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
        case Index.RANKED_QUERY:
          answer = rankedSearch(searchterms);
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
     *  TODO Support queries with more than one word
     */
    private PostingsList rankedSearch( LinkedList<String> searchterms ) {

      // Number of documents
      int N = docIDs.size();

      // Documents that contain all the terms in the query
      PostingsList postings = index.get( searchterms.get(0) );
      // Initialize the scores
      for ( int i = 0 ; i < postings.size() ; ++i )
        postings.get(i).score = 0;

      // Document frequency, number of documents that contain the term
      int df = postings.size();

      // Compute the weight of the term in the query
      double wq = Math.log10( (double)(N) / df ); //TODO add the tf part

      for ( int i = 0 ; i < postings.size() ; ++i ) {
        
        PostingsEntry entry = postings.get(i);

        // Term frequency, times the term appears in the document
        int tf = entry.positions.size();
        // Compute the weight of the term in the document
        double wd = ( 1 + Math.log10(tf) )*Math.log10( (double)(N) / df );

        System.out.println(">>>> wq = " + wq + " wd = " + wd);

        entry.score += wq*wd;

      }

      /*
      int docID;
      // Normalize the scores using the length of the documents
      for ( int i = 0 ; i < postings.size() ; ++i ) {
        PostingsEntry entry = postings.get(i);
        docID = entry.docID;
        entry.score = entry.score / docVectorLengths.get( "" + docID );
      }
      */

      return postings.sort();

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
      int tf, df;       // Term frequency and document frequency
      double length;    // Vector length of a document

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

          System.out.println(">>>> tf = " + postings.getTermFreq(docID));

          if ( ( tf = postings.getTermFreq(docID) ) > 0 ) {
            df      = postings.size();
            length += ( 1 + Math.log10(tf) )*Math.log10( (double)(N) / df );
          }
        }
        
        // Store the vector length of the document
        docVectorLengths.put("" + docID, length);

      }

    }

}
