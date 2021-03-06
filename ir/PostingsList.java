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

    /** Number of postings in this list  */
    public int size() {
	return list.size();
    }

    /**  Returns the ith posting */
    public PostingsEntry get( int i ) {
	return list.get( i );
    }

    /**
     *  Return the number of times the term appears in docID
     */
    public int getTermFreq( int docID ) {
    
      int i = 0;
      while ( i < list.size() && docID <= list.get(i).docID ) {
      
        if ( list.get(i).docID == docID )
          return list.get(i).positions.size();

        ++i;
        
      }

      // If the docID is not found, return 0
      return 0;

    }

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

    /** Intersect two postings lists that are sorted by docID */
    public static PostingsList intersect( PostingsList p1, PostingsList p2 ) {

      if ( p1 == null || p2 == null )
        return null;

      // Create a postings list that will contain the intersection
      PostingsList answer = new PostingsList();

      int i1 = 0, i2 = 0;   // Indices to the elements in the postings lists
      while ( i1 < p1.list.size() && i2 < p2.list.size() ) {
      
        PostingsEntry e1 = p1.list.get(i1);
        PostingsEntry e2 = p2.list.get(i2);

        if ( e1.docID == e2.docID ) {
          answer.insert(e1.docID, 0);   // The offset is not relevant here
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

    /** Intersect two postings lists that are sorted by docID if the term
     *  associated to p2 appears k positions ahead of the term associated to p1 */
    public static PostingsList posIntersect(
        PostingsList  p1, 
        PostingsList  p2,
        int           k ) {

      if ( p1 == null || p2 == null )
        return null;

      // Postings list that will contain the intersection
      PostingsList answer = new PostingsList();

      int i1 = 0, i2 = 0;       // Indices to the elements in the postings lists
      while ( i1 < p1.list.size() && i2 < p2.list.size() ) {

        PostingsEntry e1 = p1.list.get(i1);
        PostingsEntry e2 = p2.list.get(i2);

        if ( e1.docID == e2.docID ) {
        
          int ii1 = 0, ii2 = 0; // Indices for the positions lists
          while ( ii1 < e1.positions.size() ) {
            while ( ii2 < e2.positions.size() ) {

              if ( e2.positions.get(ii2) - e1.positions.get(ii1) == k ) {
                answer.insert( e2.docID, e2.positions.get(ii2) );
                break;
              } else if ( e2.positions.get(ii2) - e1.positions.get(ii1) > k ) {
                break;
              }

              ++ii2;

            }
            ++ii1;
          }
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

    /**
     * Add to the contents of the first list the contents of the second assuming 
     * that the same docID does not appear in both lists
     */
    public static PostingsList unite( PostingsList p1, PostingsList p2 ) {

      // Create a postings list that will contain the union
      PostingsList answer = new PostingsList();

      int i1 = 0, i2 = 0;   // Indexes for the lists
      while ( i1 < p1.list.size() && i2 < p2.list.size() ) {
      
        PostingsEntry e1 = p1.list.get(i1);
        PostingsEntry e2 = p2.list.get(i2);

        if ( e1.docID < e2.docID ) {
          answer.list.add(e1);
          ++i1;
        } else {
          answer.list.add(e2);
          ++i2;
        }

      }

      // Insert the terms that may remain left in either of the lists

      while ( i1 < p1.list.size() )
        answer.list.add( p1.list.get(i1++) );

      while ( i2 < p2.list.size() )
        answer.list.add( p2.list.get(i2++) );

      return answer;

    }

    /**
     *  Add to this list all the elements that are in p and not in the list
     */
    public void unite( PostingsList p ) {
    
      int ii = 0, ip = 0;
      while ( ii < list.size() && ip < p.list.size() ) {
      
        PostingsEntry ei =   list.get(ii);
        PostingsEntry ep = p.list.get(ip);

        if ( ei.docID < ep.docID ) {
          ++ii;  
        } else if ( ei.docID > ep.docID ) {
          list.add(ii, ep);
          ++ip;
          ++ii;
        } else if ( ei.docID == ep.docID ) {
          ++ip;
          ++ii;
        }

      }

      while ( ip < p.list.size() )
        list.add( p.list.get(ip++) );

    }

    /**
     *  Sorts this PostingsList in descending order of scores
     */
    public PostingsList sort() {
      Collections.sort(list, null);
      return this;
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
			   
