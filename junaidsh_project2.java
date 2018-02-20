import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.lucene.index.*;
import org.apache.lucene.store.FSDirectory;

/**
 * @author Junaid
 *
 */

public class junaidsh_project2 {
	static BufferedReader br;
	static BufferedWriter bw;

	public static void getPostings(String[] terms, HashMap<String, ArrayList<Integer>> h) throws Exception {
		for (String t : terms) {
			bw.write("GetPostings" + '\n');
			bw.write(t + '\n');
			bw.write("Postings list: ");
			if (h.get(t) != null) {
				for (int i = 0; i < h.get(t).size(); i++) {
					bw.write(h.get(t).get(i) + " ");
				}
			}
			bw.newLine();
		}
	}

//	public static void alternateGetPostings(String[] terms, HashMap<String, ArrayList<Integer>> h) throws Exception {
//		for (String t : terms) {
//			System.out.print("GetPostings" + '\n');
//			System.out.print(t + '\n');
//			System.out.print("Postings list: ");
//			if (h.get(t) != null) {
//				for (int i = 0; i < h.get(t).size(); i++) {
//					System.out.print(h.get(t).get(i) + " ");
//				}
//			}
//			System.out.println();
//		}
//	}

	public static void getTaatOr(String[] terms, HashMap<String, ArrayList<Integer>> postings) throws Exception {
		HashMap<Integer, Integer> scoringList = new HashMap<Integer, Integer>();
		ArrayList<Integer> results = new ArrayList<Integer>();
		int numberOfResults = 0;
		int numberOfComparisons = 0;
		bw.write("TaatOr" + '\n');
		for (String term : terms)
			bw.write(term + " ");
		bw.newLine();
		for (String term : terms) {
			if (postings.get(term) != null) {
				for (Integer docId : postings.get(term)) {
					numberOfComparisons++;
					if (scoringList.get(docId) != null)
						scoringList.put(docId, scoringList.get(docId) + 1);
					else
						scoringList.put(docId, 1);
				}
			}
		}
		bw.write("Results: ");
		for (Integer docId : scoringList.keySet()) {
			if (scoringList.get(docId) >= 1)
				results.add(docId);
		}
		for (int i = 0; i < results.size(); i++) {
			bw.write(results.get(i) + " ");
		}
		if (results.isEmpty())
			bw.write("Empty");
		numberOfResults = results.size();
		bw.newLine();
		bw.write("Number of documents in results: " + numberOfResults + '\n');
		bw.write("Number of comparisons: " + numberOfComparisons + '\n');
	}

	public static void getTaatAnd(String[] terms, HashMap<String, ArrayList<Integer>> postings) throws Exception {
		HashMap<Integer, Integer> scoringList = new HashMap<Integer, Integer>();
		ArrayList<Integer> results = new ArrayList<Integer>();
		int numberOfResults = 0;
		int numberOfComparisons = 0;
		bw.write("TaatAnd" + '\n');
		for (String term : terms)
			bw.write(term + " ");
		bw.newLine();
		for (String term : terms) {
			if (postings.get(term) != null) {
				for (Integer docId : postings.get(term)) {
					numberOfComparisons++;
					if (scoringList.get(docId) != null)
						scoringList.put(docId, scoringList.get(docId) + 1);
					else
						scoringList.put(docId, 1);
				}
			}
		}
		bw.write("Results: ");
		for (Integer docId : scoringList.keySet()) {
			if (scoringList.get(docId) == terms.length)
				results.add(docId);
		}
		for (int i = 0; i < results.size(); i++) {
			bw.write(results.get(i) + " ");
		}
		if (results.isEmpty())
			bw.write("Empty");
		numberOfResults = results.size();
		bw.newLine();
		bw.write("Number of documents in results: " + numberOfResults + '\n');
		bw.write("Number of comparisons: " + numberOfComparisons + '\n');
	}

	public static void AlternateGetTaatAnd(String[] terms, HashMap<String, ArrayList<Integer>> postings)
			throws Exception {
		ArrayList<Integer> intermediate = new ArrayList<Integer>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		int numberOfResults = 0, numberOfComparisons = 0;
		boolean firstTerm = true;
		bw.write("TaatAnd" + '\n');
		for (String term : terms)
			bw.write(term + " ");
		bw.newLine();
		for (String term : terms) {
			if (postings.get(term) != null) {
				if (firstTerm) {
					intermediate.addAll(postings.get(term));
					result.addAll(intermediate);
					firstTerm = false;
				} else {
					result = new ArrayList<Integer>();
					int i = 0, j = 0;
					while (i < intermediate.size()) {
						if (j > postings.get(term).size() - 1) {
							for (int k = i; k < intermediate.size(); k++)
								result.remove(k);
							break;
						} else if (intermediate.get(i) == postings.get(term).get(j)) {
							i++;
							j++;
							numberOfComparisons++;
						} else if (intermediate.get(i) > postings.get(term).get(j)) {
							j++;
							numberOfComparisons++;
						} else if (intermediate.get(i) < postings.get(term).get(j)) {
							result.remove(i);
							numberOfComparisons++;
						}
					}
				}
			} else {
				result.removeAll(result);
				break;
			}
			intermediate = new ArrayList<Integer>();
			intermediate.addAll(result);
			Collections.sort(intermediate);
		}
		bw.write("Results: ");
		for (int i = 0; i < intermediate.size(); i++) {
			bw.write(intermediate.get(i) + " ");
		}
		if (intermediate.isEmpty())
			bw.write("Empty");
		numberOfResults = intermediate.size();
		bw.newLine();
		bw.write("Number of documents in results: " + numberOfResults + '\n');
		bw.write("Number of comparisons: " + numberOfComparisons + '\n');
	}

	public static void alternateGetTaatOr(String[] terms, HashMap<String, ArrayList<Integer>> postings)
			throws Exception {
		ArrayList<Integer> intermediate = new ArrayList<Integer>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		int numberOfResults = 0, numberOfComparisons = 0;
		bw.write("TaatOr" + '\n');
		for (String term : terms)
			bw.write(term + " ");
		bw.newLine();
		for (String term : terms) {
			if (postings.get(term) != null) {
				if (intermediate.isEmpty()) {
					intermediate.addAll(result);
				} else {
					result = new ArrayList<Integer>();
					int i = 0, j = 0;
					while (j < postings.get(term).size()) {
						if (i > intermediate.size() - 1) {
							for (int k = j; k < postings.get(term).size(); k++)
								result.add(postings.get(term).get(k));
							break;
							// intermediate.add(postings.get(term).get(j));
							// j++;
							// i = 0;
							// numberOfComparisons--;
						} else if (intermediate.get(i) == postings.get(term).get(j)) {
							numberOfComparisons++;
							i++;
							j++;
						} else if (intermediate.get(i) > postings.get(term).get(j)) {
							result.add(postings.get(term).get(j));
							numberOfComparisons++;
							// Collections.sort(intermediate);
							j++;
							// i++;
						} else if (intermediate.get(i) < postings.get(term).get(j)) {
							numberOfComparisons++;
							i++;
						}
					}
				}
			}
			intermediate.addAll(result);
			Collections.sort(intermediate);
		}
		bw.write("Results: ");
		for (int i = 0; i < intermediate.size(); i++) {
			bw.write(intermediate.get(i) + " ");
		}
		if (intermediate.isEmpty())
			bw.write("Empty");
		numberOfResults = intermediate.size();
		bw.newLine();
		bw.write("Number of documents in results: " + numberOfResults + '\n');
		bw.write("Number of comparisons: " + numberOfComparisons + '\n');

	}

	public static void getDaatAnd(String[] terms, HashMap<String, ArrayList<Integer>> postings) throws Exception {
		int timeToBreak = 0, numberOfComparisons = 0, foundMatch = 0;
		int[] arrayPointers = new int[terms.length], arrayPointers1 = new int[terms.length];
		ArrayList<Integer> results = new ArrayList<Integer>();
		bw.write("DaatAnd" + '\n');
		for (String term : terms)
			bw.write(term + " ");
		bw.newLine();
		for (int i = 0; i < terms.length; i++) {
			if (postings.get(terms[i]) != null) {
				arrayPointers[i] = postings.get(terms[i]).size() - 1;
				arrayPointers1[i] = 0;
			}
		}
		while (postings.get(terms[0]) != null) {
			int smallest = Integer.MAX_VALUE;
			for (int i = 0; i < terms.length; i++) {
				if (postings.get(terms[i]) != null) {
					if (arrayPointers1[i] <= arrayPointers[i]) {
						if (postings.get(terms[i]).get(arrayPointers1[i]) < smallest)
							smallest = postings.get(terms[i]).get(arrayPointers1[i]);
					}
				} else
					timeToBreak++;
			}
			for (int i = 0; i < terms.length; i++) {
				if (postings.get(terms[i]) != null) {
					if (arrayPointers1[i] <= arrayPointers[i]) {
						numberOfComparisons++;
						if (postings.get(terms[i]).get(arrayPointers1[i]) == smallest) {
							arrayPointers1[i] = arrayPointers1[i] + 1;
							foundMatch++;
						}
					}
				} else
					timeToBreak++;
			}
			if (foundMatch == terms.length)
				results.add(smallest);
			foundMatch = 0;
			numberOfComparisons--;
			for (int i = 0; i < terms.length; i++) {
				if (arrayPointers[i] < arrayPointers1[i])
					timeToBreak++;
			}
			if (timeToBreak >= 1)
				break;
		}
		bw.write("Results: ");
		for (int i = 0; i < results.size(); i++)
			bw.write(results.get(i) + " ");
		if (results.isEmpty())
			bw.write("Empty");
		bw.newLine();
		int numberOfResults = results.size();
		bw.write("Number of documents in results: " + numberOfResults + '\n');
		bw.write("Number of comparisons: " + numberOfComparisons + '\n');
	}

	public static void getDaatOr(String[] terms, HashMap<String, ArrayList<Integer>> postings) throws Exception {
		int timeToBreak = 0, numberOfComparisons = 0, matchFound = 0;
		int[] arrayPointers = new int[terms.length], arrayPointers1 = new int[terms.length];
		ArrayList<Integer> results = new ArrayList<Integer>();
		bw.write("DaatOr" + '\n');
		for (String term : terms)
			bw.write(term + " ");
		bw.newLine();
		for (int i = 0; i < terms.length; i++) {
			if (postings.get(terms[i]) != null) {
				arrayPointers[i] = postings.get(terms[i]).size() - 1;
				arrayPointers1[i] = 0;
			}
		}
		while (true) {
			int smallest = Integer.MAX_VALUE;
			for (int i = 0; i < terms.length; i++) {
				if (arrayPointers1[i] <= arrayPointers[i]) {
					if (postings.get(terms[i]) != null) {
						if (postings.get(terms[i]).get(arrayPointers1[i]) < smallest)
							smallest = postings.get(terms[i]).get(arrayPointers1[i]);
					}
				}
			}
			for (int i = 0; i < terms.length; i++) {
				if (postings.get(terms[i]) != null) {
					if (arrayPointers1[i] <= arrayPointers[i]) {
						numberOfComparisons++;
						if (postings.get(terms[i]).get(arrayPointers1[i]) == smallest) {
							arrayPointers1[i] = arrayPointers1[i] + 1;
							matchFound++;
						}
					}
				} else
					timeToBreak++;
			}
			if (matchFound > 0)
				results.add(smallest);
			if (numberOfComparisons > 0)
				numberOfComparisons--;
			for (int i = 0; i < terms.length; i++) {
				if (arrayPointers[i] < arrayPointers1[i])
					timeToBreak++;
			}
			if (timeToBreak == terms.length)
				break;
			timeToBreak = 0;
		}
		bw.write("Results: ");
		for (int i = 0; i < results.size(); i++)
			bw.write(results.get(i) + " ");
		if (results.isEmpty())
			bw.write("Empty");
		bw.newLine();
		int numberOfResults = results.size();
		bw.write("Number of documents in results: " + numberOfResults + '\n');
		bw.write("Number of comparisons: " + numberOfComparisons + '\n');
	}

	public static void main(String[] args) throws Exception {
		br = new BufferedReader(new InputStreamReader(new FileInputStream(args[2]), "UTF-8"));
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "UTF-8"));
		String line = null;
		HashMap<String, ArrayList<Integer>> postings = new HashMap<String, ArrayList<Integer>>();
		FileSystem fs = FileSystems.getDefault();
		Path directory = fs.getPath(args[0]);
		IndexReader indexReader = DirectoryReader.open(FSDirectory.open(directory));
		Fields fields = MultiFields.getFields(indexReader);
		for (String field : fields) {
			if (field.equalsIgnoreCase("_version_") || field.equalsIgnoreCase("id"))
				continue;
			TermsEnum termsEnum = MultiFields.getTerms(indexReader, field).iterator();
			while (termsEnum.next() != null) {
				ArrayList<Integer> docIDs = new ArrayList<Integer>();
				PostingsEnum postingsEnum = MultiFields.getTermDocsEnum(indexReader, field, termsEnum.term());
				while (postingsEnum.nextDoc() != postingsEnum.NO_MORE_DOCS) {
					docIDs.add(postingsEnum.docID());
				}
				postings.put(termsEnum.term().utf8ToString(), docIDs);
			}
		}

		while ((line = br.readLine()) != null) {
			String[] terms = line.split(" ");
			// alternateGetPostings(terms, postings);
			getPostings(terms, postings);
			// alternateGetTaatAnd(terms, postings);
			// alternateGetTaatOr(terms, postings);
			getTaatAnd(terms, postings);
			getTaatOr(terms, postings);
			getDaatAnd(terms, postings);
			getDaatOr(terms, postings);
		}
		br.close();
		bw.close();
	}
}