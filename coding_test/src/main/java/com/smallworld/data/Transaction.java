package com.smallworld.data;



import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Transaction {
    // Represent your transaction data here.
    public static  String topSenderName="";
    public static String MAX_Amount="MAX";
    public static String UNIQUE_Client="UNIQUE";
    public static String emptyString="";
    public static final String top_3Amount = "amount";
    public static double getTotalTransactionAmount()  { return getAmount(emptyString);}
    public double getTotalTransactionAmountSentBy(String senderFullName) {return getAmount(senderFullName);}
    public double getMaxTransactionAmount(){
        return getAmount(MAX_Amount);
    }
    public long countUniqueClients(){
        return Double.valueOf(getAmount(UNIQUE_Client)).longValue();
    }
    public Map<String, ArrayList<JSONObject>> getTransactionsByBeneficiaryName(String beneficiaryFullName)
    {
        Map<String, ArrayList<JSONObject>> transactoions= new HashMap<>();
        try {String content = new String(Files.readAllBytes(Paths.get("target/transactions.json")));
            JSONArray result = new JSONArray(content);
            ArrayList<JSONObject> jsObList = new ArrayList<JSONObject>();
            for (int i =0; i < result.length(); i++){
                if(result.getJSONObject(i).getString("beneficiaryFullName").equals(beneficiaryFullName))
                {

                    jsObList.add(result.getJSONObject(i));
                    transactoions.put(result.getJSONObject(i).getString("beneficiaryFullName"),jsObList);
                }
//                JSONObject jsOb = result.getJSONObject(i);
//                System.out.println(jsOb.getString("beneficiaryFullName"));
            }
        } catch (JSONException e) {e.printStackTrace();}catch (IOException e) {e.printStackTrace();}
            return transactoions;
    }
    public ArrayList<JSONObject> getTop3TransactionsByAmount() {
        ArrayList<JSONObject> jsObList = new ArrayList<JSONObject>();
        ArrayList<JSONObject> sortedList = new ArrayList<JSONObject>();
        try {String content = new String(Files.readAllBytes(Paths.get("target/transactions.json")));
            JSONArray result = new JSONArray(content);
            for (int i =0,j=1,k=1,l=1; i < result.length(); i++){
                jsObList.add(result.getJSONObject(i));}
            Collections.sort(jsObList, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    try {
                        return o1.getDouble(top_3Amount) > o2.getDouble(top_3Amount) ? -1 : 1;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }catch (JSONException e) {e.printStackTrace();}catch (IOException e) {e.printStackTrace();}
        topSenderName=jsObList.get(0).getString("senderFullName");
        sortedList.add(jsObList.get(0));
        sortedList.add(jsObList.get(1));
        sortedList.add(jsObList.get(2));
        return sortedList;
    }

    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> unsolvedIssueIds = new HashSet<>();
        try {String content = new String(Files.readAllBytes(Paths.get("target/transactions.json")));
            JSONArray result = new JSONArray(content);
            for (int i =0,j=1,k=1,l=1; i < result.length(); i++){
                if(result.getJSONObject(i).getBoolean("issueSolved") == false)
                {
                    unsolvedIssueIds.add(result.getJSONObject(i).getInt("issueId"));
                }
            }
        }catch (JSONException e) {e.printStackTrace();}catch (IOException e) {e.printStackTrace();}
        return unsolvedIssueIds;
    }
    //Solved Issue messages are null if they are present then the following method will be used to fetch them
    public Set<String> getSolvedIssueMessages()
    {
        Set<String> solvedIssueMessages = new HashSet<>();
        try {String content = new String(Files.readAllBytes(Paths.get("target/transactions.json")));
            JSONArray result = new JSONArray(content);
            for (int i =0,j=1,k=1,l=1; i < result.length(); i++){
                if(result.getJSONObject(i).getBoolean("issueSolved") == true)
                {
                    solvedIssueMessages.add(result.getJSONObject(i).getString("issueMessage"));
                }
            }
        }catch (JSONException e) {e.printStackTrace();}catch (IOException e) {e.printStackTrace();}
        return solvedIssueMessages;
    }
    public Optional<String> getTopSender() {
        return Optional.ofNullable(topSenderName);
    }

    public static double getAmount(String senderFullName)
    {
        double amount=0;
        ArrayList<Double> list = new ArrayList<Double>();
        ArrayList<Double> listSentBy= new ArrayList<Double>();
        ArrayList<Double> maxList= new ArrayList<Double>();
        Set<String> uniqueValues = new java.util.HashSet<String>();
        try {String content = new String(Files.readAllBytes(Paths.get("target/transactions.json")));
            JSONArray result = new JSONArray(content);
            for (int i =0,j=1,k=1,l=1; i < result.length(); i++){
                JSONObject jsOb = result.getJSONObject(i);
                if(!result.getJSONObject(i).getString("senderFullName").equals(result.getJSONObject(l-1).getString("senderFullName")) && senderFullName.equals("UNIQUE"))
                {
                    uniqueValues.add(result.getJSONObject(0).getString("senderFullName"));
                    uniqueValues.add(jsOb.getString("senderFullName"));
                    l++;
                    amount=uniqueValues.size();
                }
                if(!senderFullName.equals("")&& senderFullName.equals(jsOb.getString("senderFullName"))){
                    listSentBy.add(jsOb.getDouble("amount"));
                    amount += listSentBy.get(j-1);
                    j++;
                }
                if(senderFullName.equals("")){
                    list.add(jsOb.getDouble("amount"));
                    amount += list.get(k-1);
                    k++;
                }
                if(senderFullName.equals("MAX"))
                {
                    maxList.add(jsOb.getDouble("amount"));
                    amount= Collections.max(maxList);
                }
            }
        } catch (JSONException e) {e.printStackTrace();}catch (IOException e) {e.printStackTrace();}

        return amount;
    }

    public static void main(String[] args) {
        Transaction t = new Transaction();

            System.out.println( t.getTotalTransactionAmount());
            System.out.println( t.getTotalTransactionAmountSentBy("Tom Shelby"));
            System.out.println( t.getMaxTransactionAmount());
            System.out.println( t.countUniqueClients());
            System.out.println( t.getTransactionsByBeneficiaryName("Michael Gray"));
            System.out.println( t.getTop3TransactionsByAmount());
            System.out.println( t.getTopSender());
            System.out.println( t.getUnsolvedIssueIds());
    }
}