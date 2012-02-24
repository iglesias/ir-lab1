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

public interface Index {

    /* Index types */
    public static final int HASHED_INDEX = 0;
    public static final int MEGA_INDEX = 1;

    /* Query types */
    public static final int INTERSECTION_QUERY = 0;
    public static final int PHRASE_QUERY = 1;
    public static final int RANKED_QUERY = 2;
	
    /** Doc IDs to complete paths */
    public HashMap<String, String> docIDs = new HashMap<String, String>();
    /** File name to doc IDs */
    public HashMap<String, String> nameToIDs = new HashMap<String, String>();
    public HashMap<String, Integer> docLengths = new HashMap<String, Integer>();
    public HashMap<String, Double> docVectorLengths = new HashMap<String, Double>();
    /** Doc IDs to page rank */
    public HashMap<String, Double> docRanks = new HashMap<String, Double>();

    public void insert( String token, int docID, int offset );
    public void computeDocVectorLengths();
    public PostingsList getPostings( String token );
    public PostingsList search( LinkedList<String> searchterms, int queryType );
    public void cleanup();

}
